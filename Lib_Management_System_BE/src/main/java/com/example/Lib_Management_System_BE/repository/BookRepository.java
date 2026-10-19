package com.example.Lib_Management_System_BE.repository;

import com.example.Lib_Management_System_BE.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

}
