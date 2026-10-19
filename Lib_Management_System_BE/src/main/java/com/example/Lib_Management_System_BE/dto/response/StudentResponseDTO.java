package com.example.Lib_Management_System_BE.dto.response;

import com.example.Lib_Management_System_BE.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {

    private Long studentId;
    private String studentName;
    private int age;
    private Gender gender;
    private String course;
    private double fineAmount;
}
