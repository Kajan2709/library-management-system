package com.example.Lib_Management_System_BE.controller;

import com.example.Lib_Management_System_BE.dto.request.BookRequestDTO;
import com.example.Lib_Management_System_BE.dto.request.StudentRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.BookResponseDTO;
import com.example.Lib_Management_System_BE.dto.response.StudentResponseDTO;
import com.example.Lib_Management_System_BE.service.BookService;
import com.example.Lib_Management_System_BE.service.StudentService;
import com.example.Lib_Management_System_BE.utils.EndpointBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.BASEURL)
public class BookController {

    @Autowired
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(EndpointBundle.BOOKS)
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO dto) {
        BookResponseDTO response = bookService.createBook(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(EndpointBundle.GETALLBOOKS)
    public ResponseEntity<List<BookResponseDTO>> findAllBooks() {
        List<BookResponseDTO> response = bookService.findAll();

        return ResponseEntity.
                status(HttpStatus.OK).
                body(response);
    }
}
