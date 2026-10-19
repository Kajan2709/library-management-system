package com.example.Lib_Management_System_BE.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;
    private String bookTitle;
    private String author;
    private  String category;
    private int availableQuantity;

    @OneToMany(mappedBy = "book")
    private List<Borrowrecord> borrowRecords;
}
