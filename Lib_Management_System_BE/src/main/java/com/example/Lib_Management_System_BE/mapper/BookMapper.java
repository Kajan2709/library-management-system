package com.example.Lib_Management_System_BE.mapper;

import com.example.Lib_Management_System_BE.controller.BookController;
import com.example.Lib_Management_System_BE.dto.request.BookRequestDTO;
import com.example.Lib_Management_System_BE.dto.request.StudentRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.BookResponseDTO;
import com.example.Lib_Management_System_BE.dto.response.StudentResponseDTO;
import com.example.Lib_Management_System_BE.entities.Book;
import com.example.Lib_Management_System_BE.entities.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toEntity(BookRequestDTO dto);

    BookResponseDTO toDto (Book book);
}
