package com.example.Lib_Management_System_BE.dto.response;

import com.example.Lib_Management_System_BE.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowrecordResponseDTO {

    private Long borrowRecordId;
    private Long studentId;
    private String studentName;
    private Long bookId;
    private  String bookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private int numberOfBooks;
    private PaymentStatus paymentStatus;
    private double finePerBook;
    private double totalFine;
}