package com.example.Lib_Management_System_BE.controller;


import com.example.Lib_Management_System_BE.dto.request.StudentFinePaymentRequestDTO;
import com.example.Lib_Management_System_BE.dto.request.StudentRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.StudentResponseDTO;
import com.example.Lib_Management_System_BE.entities.Student;
import com.example.Lib_Management_System_BE.enums.Gender;
import com.example.Lib_Management_System_BE.service.BookService;
import com.example.Lib_Management_System_BE.service.StudentService;

import com.example.Lib_Management_System_BE.utils.EndpointBundle;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.BASEURL)
//@AllArgsConstructor
public class StudentController {

    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(EndpointBundle.STUDENTS)
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO dto) {
        StudentResponseDTO response = studentService.createStudent(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(EndpointBundle.GETALLSTUDENTS)
    public ResponseEntity<List<StudentResponseDTO>> findAllStudents() {
        List<StudentResponseDTO> response = studentService.findAll();

        return ResponseEntity.
                status(HttpStatus.OK).
                body(response);
    }

    @PutMapping(EndpointBundle.STUDENTBYID)
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDTO dto) {

        StudentResponseDTO response = studentService.updateStudent(id, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(EndpointBundle.STUDENTBYID)
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(EndpointBundle.STUDENTBYID)
    public  ResponseEntity<StudentResponseDTO> findStudentById(@PathVariable Long id) {
        StudentResponseDTO response = studentService.findById(id);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(EndpointBundle.GENDERFILTER)
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByGender(
            @PathVariable Gender gender) {

        List<StudentResponseDTO> response =
                studentService.getStudentsByGender(gender);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping(EndpointBundle.FINEPAY)
    public ResponseEntity<StudentResponseDTO> payFine(
            @PathVariable Long id,
            @RequestBody StudentFinePaymentRequestDTO dto) {

        StudentResponseDTO response =
                studentService.payFine(id, dto.getPaidAmount());

        return ResponseEntity.ok(response);
    }

}


