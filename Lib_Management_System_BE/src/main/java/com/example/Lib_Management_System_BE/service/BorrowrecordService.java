package com.example.Lib_Management_System_BE.service;

import com.example.Lib_Management_System_BE.dto.request.BorrowrecordRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.BorrowrecordResponseDTO;

import java.util.List;

public interface BorrowrecordService {

    BorrowrecordResponseDTO borrowBook(BorrowrecordRequestDTO dto);

    List<BorrowrecordResponseDTO> getAllBorrowRecords();

    BorrowrecordResponseDTO returnBook(BorrowrecordRequestDTO dto);
    
}