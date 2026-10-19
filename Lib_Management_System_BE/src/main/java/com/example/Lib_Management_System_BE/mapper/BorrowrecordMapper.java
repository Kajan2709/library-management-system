package com.example.Lib_Management_System_BE.mapper;

import com.example.Lib_Management_System_BE.dto.response.BorrowrecordResponseDTO;
import com.example.Lib_Management_System_BE.entities.Borrowrecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowrecordMapper {

    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "student.studentName", target = "studentName")
    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "book.bookTitle", target = "bookTitle")
    BorrowrecordResponseDTO toDto(Borrowrecord borrowrecord);
}