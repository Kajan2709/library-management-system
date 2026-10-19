package com.example.Lib_Management_System_BE.controller;

import com.example.Lib_Management_System_BE.dto.request.BorrowrecordRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.BorrowrecordResponseDTO;
import com.example.Lib_Management_System_BE.service.BorrowrecordService;
import com.example.Lib_Management_System_BE.utils.EndpointBundle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.BASEURL)
public class BorrowrecordController {

    private final BorrowrecordService borrowrecordService;

    public BorrowrecordController(BorrowrecordService borrowrecordService) {
        this.borrowrecordService = borrowrecordService;
    }

    @GetMapping(EndpointBundle.BORROW)
    public ResponseEntity<List<BorrowrecordResponseDTO>> getAllBorrowRecords() {

        List<BorrowrecordResponseDTO> response =
                borrowrecordService.getAllBorrowRecords();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping(EndpointBundle.BORROW)
    public ResponseEntity<BorrowrecordResponseDTO> borrowBook(
            @RequestBody BorrowrecordRequestDTO dto) {

        BorrowrecordResponseDTO response =
                borrowrecordService.borrowBook(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(EndpointBundle.RETURNBOOK)
    public ResponseEntity<BorrowrecordResponseDTO> returnBook(
            @RequestBody BorrowrecordRequestDTO dto) {

        BorrowrecordResponseDTO response =
                borrowrecordService.returnBook(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
}