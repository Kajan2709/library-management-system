package com.example.Lib_Management_System_BE.service;

import com.example.Lib_Management_System_BE.dto.request.StudentRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.StudentResponseDTO;
import com.example.Lib_Management_System_BE.enums.Gender;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StudentService {
    StudentResponseDTO findById(Long id);
    List<StudentResponseDTO> findAll();
    StudentResponseDTO createStudent(StudentRequestDTO dto);
    StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto);
    void  deleteStudent(Long id);
    List<StudentResponseDTO> getStudentsByGender(Gender gender);
    StudentResponseDTO payFine(Long studentId, double paidAmount);
}
