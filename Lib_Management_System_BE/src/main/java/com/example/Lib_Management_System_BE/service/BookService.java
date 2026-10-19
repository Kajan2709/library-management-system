package com.example.Lib_Management_System_BE.service;

import com.example.Lib_Management_System_BE.dto.request.BookRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.BookResponseDTO;

import java.util.List;

public interface BookService {

    BookResponseDTO createBook(BookRequestDTO dto);
    List<BookResponseDTO> findAll();

}
