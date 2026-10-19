package com.example.Lib_Management_System_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private long bookId;
    private String bookTitle;
    private String author;
    private  String category;
    private int availableQuantity;
}
