package com.example.Lib_Management_System_BE.dto.request;

import com.example.Lib_Management_System_BE.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDTO {

    //private Long sid;
    private String studentName;
    private Double age;
    @NotNull(message = "Gender must be M or F")
    private Gender gender;
    private String course;

}
