package com.example.Lib_Management_System_BE.dto.request;

import com.example.Lib_Management_System_BE.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowrecordRequestDTO {

    private Long studentId;
    private Long bookId;
    private int numberOfBooks;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private PaymentStatus paymentStatus;
}