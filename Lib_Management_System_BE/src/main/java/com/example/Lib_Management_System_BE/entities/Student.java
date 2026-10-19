package com.example.Lib_Management_System_BE.entities;
import com.example.Lib_Management_System_BE.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String studentName;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String course;

    private double fineAmount;

    @OneToMany(mappedBy = "student")
    private List<Borrowrecord> borrowRecords;
}

