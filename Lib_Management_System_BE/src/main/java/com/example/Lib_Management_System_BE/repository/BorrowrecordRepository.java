package com.example.Lib_Management_System_BE.repository;

import com.example.Lib_Management_System_BE.entities.Borrowrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowrecordRepository extends JpaRepository<Borrowrecord, Long> {

    @Query("""
            SELECT COALESCE(SUM(b.numberOfBooks), 0)
            FROM Borrowrecord b
            WHERE b.student.studentId = :studentId
            AND b.returnDate IS NULL
            """)
    Long getCurrentBorrowedBookCount(@Param("studentId") Long studentId);

    Optional<Borrowrecord>
    findFirstByStudentStudentIdAndBookBookIdAndReturnDateIsNull(
            Long studentId,
            Long bookId
    );
}