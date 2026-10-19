package com.example.Lib_Management_System_BE.ServiceImpl;

import com.example.Lib_Management_System_BE.dto.request.StudentRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.StudentResponseDTO;
import com.example.Lib_Management_System_BE.entities.Student;
import com.example.Lib_Management_System_BE.enums.Gender;
import com.example.Lib_Management_System_BE.exception.Bussinessexception;
import com.example.Lib_Management_System_BE.mapper.StudentMapper;
import com.example.Lib_Management_System_BE.repository.StudentRepository;

import com.example.Lib_Management_System_BE.service.serviceimpl.StudentServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceimplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceimpl studentService;

    private Student student;
    private StudentRequestDTO requestDTO;
    private StudentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setStudentName("Kajan");
        student.setAge(22);
        student.setGender(Gender.M);
        student.setCourse("Software Engineering");
        student.setFineAmount(500);

        requestDTO = new StudentRequestDTO();
        requestDTO.setStudentName("Kajan");
        requestDTO.setAge(22.0);
        requestDTO.setGender(Gender.M);
        requestDTO.setCourse("Software Engineering");

        responseDTO = new StudentResponseDTO();
        responseDTO.setStudentName("Kajan");
    }


    @Test
    void createStudent_Success() {
        when(studentMapper.toEntity(requestDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(responseDTO);

        StudentResponseDTO result = studentService.createStudent(requestDTO);

        assertNotNull(result);
        assertEquals("Kajan", result.getStudentName());
        verify(studentRepository).save(student);
    }


    @Test
    void createStudent_EmptyName_ThrowsException() {
        requestDTO.setStudentName("");

        Bussinessexception exception = assertThrows(
                Bussinessexception.class,
                () -> studentService.createStudent(requestDTO)
        );

        assertEquals("Student name must required!", exception.getMessage());
        verify(studentRepository, never()).save(any());
    }


    @Test
    void createStudent_ZeroAge_ThrowsException() {
        requestDTO.setAge(0.0);

        assertThrows(Bussinessexception.class,
                () -> studentService.createStudent(requestDTO));

        verify(studentRepository, never()).save(any());
    }


    @Test
    void createStudent_NegativeAge_ThrowsException() {
        requestDTO.setAge(-5.0);

        assertThrows(Bussinessexception.class,
                () -> studentService.createStudent(requestDTO));

        verify(studentRepository, never()).save(any());
    }


    @Test
    void createStudent_EmptyCourse_ThrowsException() {
        requestDTO.setCourse("");

        Bussinessexception exception = assertThrows(
                Bussinessexception.class,
                () -> studentService.createStudent(requestDTO)
        );

        assertEquals("Course must required!", exception.getMessage());
    }


    @Test
    void findById_Success() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(responseDTO);

        StudentResponseDTO result = studentService.findById(1L);

        assertEquals("Kajan", result.getStudentName());
    }


    @Test
    void findById_NotFound_ThrowsException() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(Bussinessexception.class,
                () -> studentService.findById(99L));
    }


    @Test
    void findAll_ReturnsStudents() {
        Sort sort = Sort.by(Sort.Direction.ASC, "studentId");

        when(studentRepository.findAll(sort))
                .thenReturn(List.of(student));
        when(studentMapper.toDto(student)).thenReturn(responseDTO);

        List<StudentResponseDTO> result = studentService.findAll();

        assertEquals(1, result.size());
        assertEquals("Kajan", result.get(0).getStudentName());
    }


    @Test
    void updateStudent_Success() {
        requestDTO.setStudentName("Updated Kajan");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(responseDTO);

        studentService.updateStudent(1L, requestDTO);

        assertEquals("Updated Kajan", student.getStudentName());
        verify(studentRepository).save(student);
    }


    @Test
    void updateStudent_NotFound_ThrowsException() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(Bussinessexception.class,
                () -> studentService.updateStudent(99L, requestDTO));
    }


    @Test
    void deleteStudent_Success() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(student);
    }


    @Test
    void deleteStudent_NotFound_ThrowsException() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(Bussinessexception.class,
                () -> studentService.deleteStudent(99L));
    }


    @Test
    void getStudentsByGender_Success() {
        when(studentRepository.findByGender(Gender.M))
                .thenReturn(List.of(student));
        when(studentMapper.toDto(student)).thenReturn(responseDTO);

        List<StudentResponseDTO> result =
                studentService.getStudentsByGender(Gender.M);

        assertEquals(1, result.size());
        verify(studentRepository).findByGender(Gender.M);
    }


    @Test
    void getStudentsByGender_Empty_ThrowsException() {
        when(studentRepository.findByGender(Gender.F))
                .thenReturn(List.of());

        Bussinessexception exception = assertThrows(
                Bussinessexception.class,
                () -> studentService.getStudentsByGender(Gender.F)
        );

        assertEquals(
                "No students found for gender: FEMALE.",
                exception.getMessage()
        );
    }


    @Test
    void payFine_Success() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(responseDTO);

        studentService.payFine(1L, 200);

        assertEquals(300, student.getFineAmount(), 0.001);
        verify(studentRepository).save(student);
    }


    @Test
    void payFine_ExceedsFine_ThrowsException() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        Bussinessexception exception = assertThrows(
                Bussinessexception.class,
                () -> studentService.payFine(1L, 600)
        );

        assertEquals(
                "Payment exceeds outstanding fine",
                exception.getMessage()
        );

        verify(studentRepository, never()).save(any());
    }
}

