package com.example.Lib_Management_System_BE.service.serviceimpl;

import com.example.Lib_Management_System_BE.dto.request.BookRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.BookResponseDTO;
import com.example.Lib_Management_System_BE.entities.Book;
import com.example.Lib_Management_System_BE.entities.Student;
import com.example.Lib_Management_System_BE.exception.Bussinessexception;
import com.example.Lib_Management_System_BE.mapper.BookMapper;
import com.example.Lib_Management_System_BE.repository.BookRepository;
import com.example.Lib_Management_System_BE.service.BookService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceimpl implements BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public BookServiceimpl(BookMapper bookMapper, BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }


    @Override
    public BookResponseDTO createBook(BookRequestDTO dto) {
        if (dto.getBookTitle() == null ||
                dto.getBookTitle().isBlank()) {
            throw new Bussinessexception("Book name must required!");
        }

        if (dto.getAuthor().isEmpty()){
            throw  new Bussinessexception("Author must required!");
        }

        if (dto.getCategory().isEmpty()){
            throw   new Bussinessexception("Category must required!");
        }

        if (dto.getAvailableQuantity()<=0){
            throw   new Bussinessexception("AvailableQuantity must be positive!");
        }

        Book book = bookMapper.toEntity(dto);

        Book savedbook = bookRepository.save(book);

        return bookMapper.toDto(savedbook);
    }

    @Override
    public List<BookResponseDTO> findAll() {

        return bookRepository
                .findAll(Sort.by(Sort.Direction.ASC, "bookId"))
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
