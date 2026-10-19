package com.example.Lib_Management_System_BE.repository;

import com.example.Lib_Management_System_BE.entities.Student;
import com.example.Lib_Management_System_BE.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGender(Gender gender);
}
