package com.example.Lib_Management_System_BE.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {
    //private long bookId;
    private String bookTitle;
    private String author;
    private  String category;
    private int availableQuantity;
}
