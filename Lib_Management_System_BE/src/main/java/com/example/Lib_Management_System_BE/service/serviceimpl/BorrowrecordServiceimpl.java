package com.example.Lib_Management_System_BE.service.serviceimpl;

import com.example.Lib_Management_System_BE.dto.request.BorrowrecordRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.BorrowrecordResponseDTO;
import com.example.Lib_Management_System_BE.entities.Book;
import com.example.Lib_Management_System_BE.entities.Borrowrecord;
import com.example.Lib_Management_System_BE.entities.Student;
import com.example.Lib_Management_System_BE.enums.PaymentStatus;
import com.example.Lib_Management_System_BE.exception.Bussinessexception;
import com.example.Lib_Management_System_BE.mapper.BorrowrecordMapper;
import com.example.Lib_Management_System_BE.repository.BookRepository;
import com.example.Lib_Management_System_BE.repository.BorrowrecordRepository;
import com.example.Lib_Management_System_BE.repository.StudentRepository;
import com.example.Lib_Management_System_BE.service.BorrowrecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.List;

@Service
public class BorrowrecordServiceimpl implements BorrowrecordService {

    private final BorrowrecordMapper borrowrecordMapper;
    private final BorrowrecordRepository borrowrecordRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    public BorrowrecordServiceimpl(BorrowrecordMapper borrowrecordMapper, BorrowrecordRepository borrowrecordRepository, StudentRepository studentRepository, BookRepository bookRepository) {
        this.borrowrecordMapper = borrowrecordMapper;
        this.borrowrecordRepository = borrowrecordRepository;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    public BorrowrecordResponseDTO borrowBook(BorrowrecordRequestDTO dto) {

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() ->
                        new Bussinessexception("Student not found"));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() ->
                        new Bussinessexception("Book not found"));

        if (dto.getNumberOfBooks() <= 0) {
            throw new Bussinessexception(
                    "Number of books must be greater than zero"
            );
        }

        Long currentBorrowedBooks =
                borrowrecordRepository.getCurrentBorrowedBookCount(
                        dto.getStudentId()
                );

        long newTotal =
                currentBorrowedBooks + dto.getNumberOfBooks();

        if (newTotal > 5) {
            throw new Bussinessexception(
                    "Student cannot borrow more than 5 books"
            );
        }

        System.out.println("========== BORROW DEBUG ==========");
        System.out.println("Selected Book ID: " + dto.getBookId());
        System.out.println("Database Book ID: " + book.getBookId());
        System.out.println("Available Quantity: " + book.getAvailableQuantity());
        System.out.println("Requested Quantity: " + dto.getNumberOfBooks());
        System.out.println("==================================");

        if (book.getAvailableQuantity() < dto.getNumberOfBooks()) {
            throw new Bussinessexception(
                    "Insufficient book quantity"
            );
        }

        book.setAvailableQuantity(
                book.getAvailableQuantity() - dto.getNumberOfBooks()
        );

        bookRepository.save(book);

        Borrowrecord borrowrecord = new Borrowrecord();

        borrowrecord.setStudent(student);
        borrowrecord.setBook(book);
        borrowrecord.setNumberOfBooks(dto.getNumberOfBooks());
        borrowrecord.setBorrowDate(LocalDate.now());
        borrowrecord.setReturnDate(null);

        Borrowrecord savedBorrowrecord =
                borrowrecordRepository.save(borrowrecord);

        return borrowrecordMapper.toDto(savedBorrowrecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowrecordResponseDTO> getAllBorrowRecords() {

        System.out.println("STEP 1: GET BORROW STARTED");

        List<Borrowrecord> records =
                borrowrecordRepository.findAll();

        System.out.println("STEP 2: TOTAL RECORDS = " + records.size());

        return records.stream()
                .map(record -> {

                    System.out.println(
                            "STEP 3: MAPPING RECORD = "
                                    + record.getBorrowRecordId()
                    );

                    BorrowrecordResponseDTO response =
                            borrowrecordMapper.toDto(record);

                    System.out.println("STEP 4: MAPPING SUCCESS");

                    return response;
                })
                .toList();
    }

    @Override
    @Transactional
    public BorrowrecordResponseDTO returnBook(
            BorrowrecordRequestDTO dto) {

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() ->
                        new Bussinessexception("Student not found"));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() ->
                        new Bussinessexception("Book not found"));

        Borrowrecord borrowrecord = borrowrecordRepository
                .findFirstByStudentStudentIdAndBookBookIdAndReturnDateIsNull(
                        dto.getStudentId(),
                        dto.getBookId()
                )
                .orElseThrow(() ->
                        new Bussinessexception("Active borrow record not found"));

        int returnQuantity = dto.getNumberOfBooks();

        if (returnQuantity <= 0 ||
                returnQuantity > borrowrecord.getNumberOfBooks()) {
            throw new Bussinessexception("Invalid return quantity");
        }

        if (dto.getPaymentStatus() == null) {
            throw new Bussinessexception("Payment status required");
        }

        LocalDate borrowDate = borrowrecord.getBorrowDate();

        LocalDate returnDate = dto.getReturnDate() != null
                ? dto.getReturnDate()
                : LocalDate.now();

        if (borrowDate == null || returnDate.isBefore(borrowDate)) {
            throw new Bussinessexception("Invalid return date");
        }

        long days = ChronoUnit.DAYS.between(borrowDate, returnDate);

        double finePerBook;

        if (days <= 3) {
            finePerBook = 0;
        } else if (days <= 7) {
            finePerBook = 50;
        } else if (days <= 15) {
            finePerBook = 200;
        } else if (days <= 25) {
            finePerBook = 400;
        } else if (days <= 30) {
            finePerBook = 500;
        } else {
            finePerBook = 1000;
        }

        double totalFine = finePerBook * returnQuantity;

        if (dto.getPaymentStatus() == PaymentStatus.UNPAID) {
            student.setFineAmount(
                    student.getFineAmount() + totalFine
            );

            studentRepository.save(student);
        }

        book.setAvailableQuantity(
                book.getAvailableQuantity() + returnQuantity
        );

        bookRepository.save(book);

        int remainingBooks =
                borrowrecord.getNumberOfBooks() - returnQuantity;

        if (remainingBooks == 0) {
            borrowrecord.setNumberOfBooks(0);
            borrowrecord.setReturnDate(returnDate);
            borrowrecord.setPaymentStatus(dto.getPaymentStatus());
            borrowrecord.setFinePerBook(finePerBook);
            borrowrecord.setTotalFine(totalFine);
        } else {
            borrowrecord.setNumberOfBooks(remainingBooks);
        }

        Borrowrecord savedRecord =
                borrowrecordRepository.save(borrowrecord);

        return borrowrecordMapper.toDto(savedRecord);
    }
}





