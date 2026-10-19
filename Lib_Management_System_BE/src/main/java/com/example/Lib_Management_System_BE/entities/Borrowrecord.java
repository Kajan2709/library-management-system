package com.example.Lib_Management_System_BE.entities;

import com.example.Lib_Management_System_BE.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "borrowrecord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Borrowrecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowRecordId;

    private LocalDate borrowDate;

    private LocalDate returnDate;

    private int numberOfBooks;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private double finePerBook;

    private double totalFine;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}