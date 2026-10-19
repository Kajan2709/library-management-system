package com.example.Lib_Management_System_BE.ServiceImpl;

import com.example.Lib_Management_System_BE.dto.request.BookRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.BookResponseDTO;
import com.example.Lib_Management_System_BE.entities.Book;
import com.example.Lib_Management_System_BE.exception.Bussinessexception;
import com.example.Lib_Management_System_BE.mapper.BookMapper;
import com.example.Lib_Management_System_BE.repository.BookRepository;

import com.example.Lib_Management_System_BE.service.serviceimpl.BookServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceimplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceimpl bookService;

    private BookRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new BookRequestDTO();
        requestDTO.setBookTitle("Java Programming");
        requestDTO.setAuthor("James");
        requestDTO.setCategory("Programming");
        requestDTO.setAvailableQuantity(10);
    }


    @Test
    void createBook_Success() {

        Book book = new Book();
        Book savedBook = new Book();
        BookResponseDTO responseDTO = new BookResponseDTO();

        responseDTO.setBookTitle("Java Programming");

        when(bookMapper.toEntity(requestDTO))
                .thenReturn(book);

        when(bookRepository.save(book))
                .thenReturn(savedBook);

        when(bookMapper.toDto(savedBook))
                .thenReturn(responseDTO);

        BookResponseDTO result = bookService.createBook(requestDTO);

        assertNotNull(result);
        assertEquals("Java Programming", result.getBookTitle());

        verify(bookRepository, times(1)).save(book);
    }


    @Test
    void createBook_EmptyTitle_ThrowsException() {

        requestDTO.setBookTitle("");

        Bussinessexception exception = assertThrows(
                Bussinessexception.class,
                () -> bookService.createBook(requestDTO)
        );

        assertEquals(
                "Book name must required!",
                exception.getMessage()
        );

        verify(bookRepository, never()).save(any());
    }


    @Test
    void createBook_EmptyAuthor_ThrowsException() {

        requestDTO.setAuthor("");

        Bussinessexception exception = assertThrows(
                Bussinessexception.class,
                () -> bookService.createBook(requestDTO)
        );

        assertEquals(
                "Author must required!",
                exception.getMessage()
        );

        verify(bookRepository, never()).save(any());
    }


    @Test
    void createBook_EmptyCategory_ThrowsException() {

        requestDTO.setCategory("");

        Bussinessexception exception = assertThrows(
                Bussinessexception.class,
                () -> bookService.createBook(requestDTO)
        );

        assertEquals(
                "Category must required!",
                exception.getMessage()
        );

        verify(bookRepository, never()).save(any());
    }


    @Test
    void createBook_ZeroQuantity_ThrowsException() {

        requestDTO.setAvailableQuantity(0);

        Bussinessexception exception = assertThrows(
                Bussinessexception.class,
                () -> bookService.createBook(requestDTO)
        );

        assertEquals(
                "AvailableQuantity must be positive!",
                exception.getMessage()
        );

        verify(bookRepository, never()).save(any());
    }


    @Test
    void createBook_NegativeQuantity_ThrowsException() {

        requestDTO.setAvailableQuantity(-5);

        assertThrows(
                Bussinessexception.class,
                () -> bookService.createBook(requestDTO)
        );

        verify(bookRepository, never()).save(any());
    }


    @Test
    void findAll_ReturnsBookList() {

        Book book = new Book();
        book.setBookTitle("Java Programming");

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setBookTitle("Java Programming");

        Sort sort = Sort.by(Sort.Direction.ASC, "bookId");

        when(bookRepository.findAll(sort))
                .thenReturn(List.of(book));

        when(bookMapper.toDto(book))
                .thenReturn(responseDTO);

        List<BookResponseDTO> result = bookService.findAll();

        assertEquals(1, result.size());
        assertEquals(
                "Java Programming",
                result.get(0).getBookTitle()
        );

        verify(bookRepository, times(1)).findAll(sort);
    }
}
