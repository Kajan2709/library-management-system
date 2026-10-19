package com.example.Lib_Management_System_BE.service.serviceimpl;

import com.example.Lib_Management_System_BE.dto.response.LibrarySummaryResponseDTO;
import com.example.Lib_Management_System_BE.entities.Book;
import com.example.Lib_Management_System_BE.entities.Borrowrecord;
import com.example.Lib_Management_System_BE.entities.Student;
import com.example.Lib_Management_System_BE.enums.Gender;
import com.example.Lib_Management_System_BE.repository.BookRepository;
import com.example.Lib_Management_System_BE.repository.BorrowrecordRepository;
import com.example.Lib_Management_System_BE.repository.StudentRepository;
import com.example.Lib_Management_System_BE.service.LibrarySummaryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibrarySummaryServiceImpl implements LibrarySummaryService {

    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final BorrowrecordRepository borrowrecordRepository;

    public LibrarySummaryServiceImpl(
            StudentRepository studentRepository,
            BookRepository bookRepository,
            BorrowrecordRepository borrowrecordRepository) {

        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
        this.borrowrecordRepository = borrowrecordRepository;
    }

    @Override
    public LibrarySummaryResponseDTO getLibrarySummary() {

        List<Student> students = studentRepository.findAll();
        List<Book> books = bookRepository.findAll();
        List<Borrowrecord> borrowrecords = borrowrecordRepository.findAll();

        long totalStudents = students.size();

        long totalMaleStudents = students.stream()
                .filter(student -> student.getGender() == Gender.M)
                .count();

        long totalFemaleStudents = students.stream()
                .filter(student -> student.getGender() == Gender.F)
                .count();

        List<Borrowrecord> activeBorrowRecords = borrowrecords.stream()
                .filter(record -> record.getReturnDate() == null)
                .filter(record -> record.getNumberOfBooks() > 0)
                .toList();

        long totalCurrentlyBorrowedBooks = activeBorrowRecords.stream()
                .mapToLong(Borrowrecord::getNumberOfBooks)
                .sum();

        long studentsWith0Books = 0;
        long studentsWith1Books = 0;
        long studentsWith2Books = 0;
        long studentsWith3Books = 0;
        long studentsWith4Books = 0;
        long studentsWith5Books = 0;

        for (Student student : students) {

            long currentBookCount = activeBorrowRecords.stream()
                    .filter(record ->
                            record.getStudent().getStudentId()
                                    .equals(student.getStudentId()))
                    .mapToLong(Borrowrecord::getNumberOfBooks)
                    .sum();

            if (currentBookCount == 0) {
                studentsWith0Books++;
            } else if (currentBookCount == 1) {
                studentsWith1Books++;
            } else if (currentBookCount == 2) {
                studentsWith2Books++;
            } else if (currentBookCount == 3) {
                studentsWith3Books++;
            } else if (currentBookCount == 4) {
                studentsWith4Books++;
            } else if (currentBookCount == 5) {
                studentsWith5Books++;
            }
        }

        long totalAvailableBooks = books.stream()
                .mapToLong(Book::getAvailableQuantity)
                .sum();

        long totalBorrowedBooks = totalCurrentlyBorrowedBooks;

        long totalRegisteredBooks =
                totalAvailableBooks + totalBorrowedBooks;

        LibrarySummaryResponseDTO response =
                new LibrarySummaryResponseDTO();

        response.setTotalStudents(totalStudents);
        response.setTotalMaleStudents(totalMaleStudents);
        response.setTotalFemaleStudents(totalFemaleStudents);

        response.setTotalCurrentlyBorrowedBooks(
                totalCurrentlyBorrowedBooks
        );

        response.setStudentsWith0Books(studentsWith0Books);
        response.setStudentsWith1Books(studentsWith1Books);
        response.setStudentsWith2Books(studentsWith2Books);
        response.setStudentsWith3Books(studentsWith3Books);
        response.setStudentsWith4Books(studentsWith4Books);
        response.setStudentsWith5Books(studentsWith5Books);

        response.setTotalRegisteredBooks(totalRegisteredBooks);
        response.setTotalAvailableBooks(totalAvailableBooks);
        response.setTotalBorrowedBooks(totalBorrowedBooks);

        return response;
    }
}
