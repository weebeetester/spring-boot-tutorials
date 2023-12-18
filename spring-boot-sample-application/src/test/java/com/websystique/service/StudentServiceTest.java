package com.websystique.service;

import com.websystique.domain.Student;
import com.websystique.exception.StudentAlreadyExistException;
import com.websystique.exception.StudentIdMismatchException;
import com.websystique.exception.StudentNotFoundException;
import com.websystique.persistence.entity.StudentEntity;
import com.websystique.persistence.mapper.StudentMapper;
import com.websystique.persistence.repo.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/*
    For Unit Tests
    - Testing Controller layer:  We can use @WebMvcTest.
    - Testing Data layer:  We can use @DataJpaTest (also notice data.sql in /test/resources/).
    - Testing other layers: We can just use plain old JUnit+mock frameworks (@ExtendWith(MockitoExtension.class)) without any Spring support.
 */
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentMapper studentMapper;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void whenGetStudents_thenReturnStudentList() {
        List<StudentEntity> studentEntities = List.of(
                StudentEntity.builder().id(1L).name("Sam").major("Maths").build(),
                StudentEntity.builder().id(2L).name("Tim").major("Physics").build(),
                StudentEntity.builder().id(3L).name("Mat").major("English").build());

        List<Student> students = List.of(
                Student.builder().id(1L).name("Sam").major("Maths").build(),
                Student.builder().id(2L).name("Tim").major("Physics").build(),
                Student.builder().id(3L).name("Mat").major("English").build());

        when(studentRepository.findAll()).thenReturn(studentEntities);

        when(studentMapper.mapEntityToModel(studentEntities.get(0))).thenReturn(students.get(0));
        when(studentMapper.mapEntityToModel(studentEntities.get(1))).thenReturn(students.get(1));
        when(studentMapper.mapEntityToModel(studentEntities.get(2))).thenReturn(students.get(2));

        List<Student> allStudents = studentService.findAll();
        assertThat(allStudents).hasSize(3);
        assertThat(allStudents.stream().map(t -> t.getName()).collect(Collectors.toList())).containsExactlyInAnyOrder("Sam", "Tim", "Mat");
    }

    @Test
    public void whenGetStudentsById_thenReturnStudent() {
        List<StudentEntity> studentEntities = List.of(
                StudentEntity.builder().id(1L).name("Sam").major("Maths").build(),
                StudentEntity.builder().id(2L).name("Tim").major("Physics").build(),
                StudentEntity.builder().id(3L).name("Mat").major("English").build());

        List<Student> students = List.of(
                Student.builder().id(1L).name("Sam").major("Maths").build(),
                Student.builder().id(2L).name("Tim").major("Physics").build(),
                Student.builder().id(3L).name("Mat").major("English").build());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(studentEntities.get(0)));

        when(studentMapper.mapEntityToModel(studentEntities.get(0))).thenReturn(students.get(0));

        Student s = studentService.findById(1L);
        assertThat(s.getId()).isEqualTo(1L);
        assertThat(s.getName()).isEqualTo("Sam");
        assertThat(s.getMajor()).isEqualTo("Maths");
    }

    @Test
    public void whenGetStudentsByName_thenReturnStudentList() {
        List<StudentEntity> studentEntities = List.of(
                StudentEntity.builder().id(1L).name("Sam").major("Maths").build(),
                StudentEntity.builder().id(2L).name("Tim").major("Physics").build(),
                StudentEntity.builder().id(3L).name("Mat").major("English").build());

        List<Student> students = List.of(
                Student.builder().id(1L).name("Sam").major("Maths").build(),
                Student.builder().id(2L).name("Tim").major("Physics").build(),
                Student.builder().id(3L).name("Mat").major("English").build());

        when(studentRepository.findByName("Sam")).thenReturn(List.of(studentEntities.get(0)));

        when(studentMapper.mapEntityToModel(studentEntities.get(0))).thenReturn(students.get(0));

        List<Student> allStudents = studentService.findByName("Sam");
        assertThat(allStudents).hasSize(1);
        assertThat(allStudents.stream().map(t -> t.getName()).collect(Collectors.toList())).containsExactly("Sam");
    }

    @Test
    public void whenPostStudent_thenCreateStudent() {
        StudentEntity studentEntity = StudentEntity.builder().id(1L).name("Sam").major("Maths").build();
        Student student = Student.builder().name("Sam").major("Maths").build();

        when(studentRepository.save(any(StudentEntity.class))).thenReturn(studentEntity);

        when(studentMapper.mapModelToEntity(student)).thenReturn(studentEntity);
        when(studentMapper.mapEntityToModel(studentEntity)).thenReturn(student);

        Student savedStudent = studentService.create(student);
        assertThat(savedStudent.getName()).isEqualTo("Sam");
        assertThat(savedStudent.getMajor()).isEqualTo("Maths");
    }

    @Test
    public void whenPostExistingStudent_thenThrowException() {
        StudentEntity studentEntity = StudentEntity.builder().id(1L).name("Sam").major("Maths").build();
        Student student = Student.builder().id(1L).name("Sam").major("Maths").build();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(studentEntity));
        assertThrows(StudentAlreadyExistException.class, () -> studentService.create(student));
    }

    @Test
    public void whenPutStudent_thenUpdateStudent() {
        StudentEntity studentEntity = StudentEntity.builder().id(1L).name("Sam").major("Maths").build();
        Student student = Student.builder().id(1L).name("Sam").major("Maths").build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(studentEntity));
        when(studentRepository.save(any(StudentEntity.class))).thenReturn(studentEntity);


        when(studentMapper.mapModelToEntity(student)).thenReturn(studentEntity);
        when(studentMapper.mapEntityToModel(studentEntity)).thenReturn(student);

        Student savedStudent = studentService.update(student, 1L);
        assertThat(savedStudent.getName()).isEqualTo("Sam");
        assertThat(savedStudent.getMajor()).isEqualTo("Maths");
    }

    @Test
    public void whenPutStudentWithMismatchingID_thenThrowException() {
        Student student = Student.builder().id(1L).name("Sam").major("Maths").build();
        assertThrows(StudentIdMismatchException.class, () -> studentService.update(student, 2L));
    }

    @Test
    public void whenPutStudentWithoutExistingID_thenThrowException() {
        Student student = Student.builder().id(1L).name("Sam").major("Maths").build();
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.update(student, 1L));
    }

    @Test
    public void whenDeleteStudent_thenRemoveStudent() {
        StudentEntity studentEntity = StudentEntity.builder().id(1L).name("Sam").major("Maths").build();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(studentEntity));
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
