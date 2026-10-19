package com.example.Lib_Management_System_BE.service.serviceimpl;

import com.example.Lib_Management_System_BE.dto.request.StudentRequestDTO;
import com.example.Lib_Management_System_BE.dto.response.StudentResponseDTO;
import com.example.Lib_Management_System_BE.entities.Student;
import com.example.Lib_Management_System_BE.enums.Gender;
import com.example.Lib_Management_System_BE.exception.Bussinessexception;
import com.example.Lib_Management_System_BE.mapper.StudentMapper;
import com.example.Lib_Management_System_BE.repository.StudentRepository;
import com.example.Lib_Management_System_BE.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceimpl implements StudentService {

    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;

    public StudentServiceimpl(StudentMapper studentMapper, StudentRepository studentRepository) {
        this.studentMapper = studentMapper;
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentResponseDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new Bussinessexception("Student not found!"));

        return studentMapper.toDto(student);
    }

    @Override
    public List<StudentResponseDTO> findAll() {

        List<Student> students =
                studentRepository.findAll(
                        Sort.by(Sort.Direction.ASC, "studentId")
                );

        return students.stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        if(dto.getStudentName().isEmpty()){
           throw new Bussinessexception("Student name must required!");
        }

        if (dto.getAge()<=0){
            throw  new Bussinessexception("Age must be greater than 0");
        }

        if(dto.getCourse().isEmpty()){
            throw  new Bussinessexception("Course must required!");
        }

        Student student = studentMapper.toEntity(dto);

        Student savedstudent = studentRepository.save(student);

        return  studentMapper.toDto(savedstudent);
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new Bussinessexception("Student not found!"));

//        if(dto.getStudentName().isEmpty()){
//            throw new Bussinessexception("Student name must required!");
//        }
//
//        if (dto.getAge()<=0){
//            throw  new Bussinessexception("Age must be greater than 0");
//        }
//
//        if(dto.getCourse().isEmpty()){
//            throw  new Bussinessexception("Course must required!");
//        }

        student.setStudentName(dto.getStudentName());
        student.setAge(dto.getAge().intValue());
        student.setGender(dto.getGender());
        student.setCourse(dto.getCourse());

        Student updatedStudent = studentRepository.save(student);

        return studentMapper.toDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new Bussinessexception("Student not found!"));

        studentRepository.delete(student);
    }

    @Override
    public List<StudentResponseDTO> getStudentsByGender(Gender gender) {
        String print = "";
        List<Student> students =studentRepository.findByGender(gender);

        if(gender==Gender.F){
             print = "FEMALE";
        }else  if(gender==Gender.M){
            print = "MALE";
        }

        if (students.isEmpty()) {
            throw new Bussinessexception(
                    "No students found for gender: " + print + "."
            );
        }

        return students.stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public StudentResponseDTO payFine(
            Long studentId,
            double paidAmount) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new Bussinessexception("Student not found"));

        if (!Double.isFinite(paidAmount)
                || paidAmount <= 0) {
            throw new Bussinessexception("Invalid payment amount");
        }

        if (paidAmount > student.getFineAmount()) {
            throw new Bussinessexception(
                    "Payment exceeds outstanding fine"
            );
        }

        student.setFineAmount(
                student.getFineAmount() - paidAmount
        );

        Student savedStudent = studentRepository.save(student);

        return studentMapper.toDto(savedStudent);
    }


}


