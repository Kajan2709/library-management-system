package com.example.Lib_Management_System_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibrarySummaryResponseDTO {

    private long totalStudents;
    private long totalMaleStudents;
    private long totalFemaleStudents;

    private long totalCurrentlyBorrowedBooks;

    private long studentsWith0Books;
    private long studentsWith1Books;
    private long studentsWith2Books;
    private long studentsWith3Books;
    private long studentsWith4Books;
    private long studentsWith5Books;

    private long totalRegisteredBooks;
    private long totalAvailableBooks;
    private long totalBorrowedBooks;
}
