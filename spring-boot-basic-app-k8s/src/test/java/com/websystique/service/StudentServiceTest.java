package com.websystique.service;

import com.websystique.dto.StudentCreationDto;
import com.websystique.dto.StudentUpdationDto;
import com.websystique.dto.StudentResponseDto;

import com.websystique.exception.StudentIdMismatchException;
import com.websystique.exception.StudentNotFoundException;
import com.websystique.persistence.entity.Student;
import com.websystique.persistence.mapper.StudentMapper;
import com.websystique.persistence.repo.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentMapper studentMapper;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void whenGetStudents_thenReturnStudentList() {
        List<Student> students = List.of(
                Student.builder().id(1L).name("Sam").major("Maths").build(),
                Student.builder().id(2L).name("Tim").major("Physics").build(),
                Student.builder().id(3L).name("Mat").major("English").build());

        List<StudentResponseDto> studentResponseDtos = List.of(
                StudentResponseDto.builder().id(1L).name("Sam").major("Maths").build(),
                StudentResponseDto.builder().id(2L).name("Tim").major("Physics").build(),
                StudentResponseDto.builder().id(3L).name("Mat").major("English").build());

        when(studentRepository.findAll()).thenReturn(students);

        when(studentMapper.mapEntityToModel(students.get(0))).thenReturn(studentResponseDtos.get(0));
        when(studentMapper.mapEntityToModel(students.get(1))).thenReturn(studentResponseDtos.get(1));
        when(studentMapper.mapEntityToModel(students.get(2))).thenReturn(studentResponseDtos.get(2));

        List<StudentResponseDto> allStudents = studentService.findAll();
        assertThat(allStudents).hasSize(3);
        assertThat(allStudents.stream().map(StudentResponseDto::getName).collect(Collectors.toList())).containsExactlyInAnyOrder("Sam", "Tim", "Mat");
    }

    @Test
    public void whenGetStudentsById_thenReturnStudent() {
        List<Student> students = List.of(
                Student.builder().id(1L).name("Sam").major("Maths").build(),
                Student.builder().id(2L).name("Tim").major("Physics").build(),
                Student.builder().id(3L).name("Mat").major("English").build());

        List<StudentResponseDto> studentResponseDtos = List.of(
                StudentResponseDto.builder().id(1L).name("Sam").major("Maths").build(),
                StudentResponseDto.builder().id(2L).name("Tim").major("Physics").build(),
                StudentResponseDto.builder().id(3L).name("Mat").major("English").build());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(students.get(0)));

        when(studentMapper.mapEntityToModel(students.get(0))).thenReturn(studentResponseDtos.get(0));

        StudentResponseDto s = studentService.findById(1L);
        assertThat(s.getId()).isEqualTo(1L);
        assertThat(s.getName()).isEqualTo("Sam");
        assertThat(s.getMajor()).isEqualTo("Maths");
    }

    @Test
    public void whenGetStudentsByName_thenReturnStudentList() {
        List<Student> students = List.of(
                Student.builder().id(1L).name("Sam").major("Maths").build(),
                Student.builder().id(2L).name("Tim").major("Physics").build(),
                Student.builder().id(3L).name("Mat").major("English").build());

        List<StudentResponseDto> studentResponseDtos = List.of(
                StudentResponseDto.builder().id(1L).name("Sam").major("Maths").build(),
                StudentResponseDto.builder().id(2L).name("Tim").major("Physics").build(),
                StudentResponseDto.builder().id(3L).name("Mat").major("English").build());

        when(studentRepository.findByName("Sam")).thenReturn(List.of(students.get(0)));

        when(studentMapper.mapEntityToModel(students.get(0))).thenReturn(studentResponseDtos.get(0));

        List<StudentResponseDto> allStudents = studentService.findByName("Sam");
        assertThat(allStudents).hasSize(1);
        assertThat(allStudents.stream().map(StudentResponseDto::getName).collect(Collectors.toList())).containsExactly("Sam");
    }

    @Test
    public void whenPostStudent_thenCreateStudent() {
        Student student = Student.builder().id(1L).name("Sam").major("Maths").build();
        StudentCreationDto studentCreationDto = StudentCreationDto.builder().name("Sam").major("Maths").build();
        StudentResponseDto studentResponseDto = StudentResponseDto.builder().name("Sam").major("Maths").build();

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        when(studentMapper.mapModelToEntity(studentCreationDto)).thenReturn(student);
        when(studentMapper.mapEntityToModel(student)).thenReturn(studentResponseDto);

        StudentResponseDto createdStudent = studentService.create(studentCreationDto);
        assertThat(createdStudent.getName()).isEqualTo("Sam");
        assertThat(createdStudent.getMajor()).isEqualTo("Maths");
    }

    @Test
    public void whenPutStudent_thenUpdateStudent() {
        Student student = Student.builder().id(1L).name("Sam").major("Maths").build();
        StudentUpdationDto studentUpdationDto = StudentUpdationDto.builder().id(1L).name("Sam").major("Maths").build();
        StudentResponseDto studentResponseDto = StudentResponseDto.builder().id(1L).name("Sam").major("Maths").build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.mapEntityToModel(student)).thenReturn(studentResponseDto);

        StudentResponseDto updatedStudent = studentService.update(studentUpdationDto, 1L);
        assertThat(updatedStudent.getName()).isEqualTo("Sam");
        assertThat(updatedStudent.getMajor()).isEqualTo("Maths");
    }

    @Test
    public void whenPutStudentWithMismatchingID_thenThrowException() {
        StudentUpdationDto studentUpdationDto = StudentUpdationDto.builder().id(1L).name("Sam").major("Maths").build();
        assertThrows(StudentIdMismatchException.class, () -> studentService.update(studentUpdationDto, 2L));
    }

    @Test
    public void whenDeleteStudent_thenRemoveStudent() {
        Student student = Student.builder().id(1L).name("Sam").major("Maths").build();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).deleteById(Mockito.anyLong());
        studentService.delete(1L);
        verify(studentRepository, VerificationModeFactory.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void whenDeleteStudentWithoutExisting_thenThrowException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.delete(1L));
        verify(studentRepository, VerificationModeFactory.times(0)).deleteById(Mockito.anyLong());
    }

}
