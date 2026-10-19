package com.example.Lib_Management_System_BE.controller;

import com.example.Lib_Management_System_BE.dto.response.LibrarySummaryResponseDTO;
import com.example.Lib_Management_System_BE.service.LibrarySummaryService;
import com.example.Lib_Management_System_BE.utils.EndpointBundle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointBundle.BASEURL)
public class LibrarySummaryController {

    private final LibrarySummaryService librarySummaryService;

    public LibrarySummaryController(
            LibrarySummaryService librarySummaryService) {

        this.librarySummaryService = librarySummaryService;
    }

    @GetMapping(EndpointBundle.LIBSUMMARY)
    public ResponseEntity<LibrarySummaryResponseDTO> getLibrarySummary() {

        LibrarySummaryResponseDTO response =
                librarySummaryService.getLibrarySummary();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
