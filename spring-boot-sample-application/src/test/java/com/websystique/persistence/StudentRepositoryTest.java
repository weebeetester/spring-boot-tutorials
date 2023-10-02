package com.websystique.persistence;

import com.websystique.persistence.entity.StudentEntity;
import com.websystique.persistence.repo.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
    For Unit Tests
    - Testing Controller layer:  We can use @WebMvcTest.
    - Testing Data layer:  We can use @DataJpaTest (also notice data.sql in /test/resources/).
    - Testing other layers: We can just use plain old JUnit+mock frameworks (@ExtendWith(MockitoExtension.class)) without any Spring support.
 */
@DataJpaTest
@ActiveProfiles("test")
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    //    @Sql("/fixtures/test_data.sql")
    @Test
    void whenGetStudents_thenReturnStudentList(){
        List<StudentEntity> studentEntities = studentRepository.findAll();
        assertThat(studentEntities).hasSize(3);

        assertThat(studentEntities.get(0).getId()).isEqualTo(1L);
        assertThat(studentEntities.get(0).getName()).isEqualTo("Sam");
        assertThat(studentEntities.get(0).getMajor()).isEqualTo("Maths");

        assertThat(studentEntities.get(1).getId()).isEqualTo(2L);
        assertThat(studentEntities.get(1).getName()).isEqualTo("Tim");
        assertThat(studentEntities.get(1).getMajor()).isEqualTo("Physics");

        assertThat(studentEntities.get(2).getId()).isEqualTo(3L);
        assertThat(studentEntities.get(2).getName()).isEqualTo("Mat");
        assertThat(studentEntities.get(2).getMajor()).isEqualTo("English");
    }

    @Test
    void whenGetStudentsById_thenReturnStudent(){
        Optional<StudentEntity> studentEntity = studentRepository.findById(1L);
        assertThat(studentEntity).isPresent();

        assertThat(studentEntity.get().getName()).isEqualTo("Sam");
        assertThat(studentEntity.get().getMajor()).isEqualTo("Maths");
    }


    @Test
    public void whenGetStudentsByName_thenReturnStudentList() {
        List<StudentEntity> studentEntities = studentRepository.findByName("Sam");
        assertThat(studentEntities).hasSize(1);

        assertThat(studentEntities.get(0).getId()).isEqualTo(1L);
        assertThat(studentEntities.get(0).getName()).isEqualTo("Sam");
        assertThat(studentEntities.get(0).getMajor()).isEqualTo("Maths");
    }

    @Test
    public void whenPostStudent_thenCreateStudent() {
        StudentEntity studentEntity = StudentEntity.builder().name("Hans").major("Chemistry").build();
        studentRepository.save(studentEntity);
        assertThat(studentRepository.findById(studentEntity.getId()).isPresent());
        assertThat(studentEntity.getId()).isEqualTo(4L);
        List<StudentEntity> studentEntities = studentRepository.findAll();
        assertThat(studentEntities).hasSize(4);
    }

    @Test
    public void whenPutStudent_thenUpdateStudent() {
        StudentEntity studentEntity = StudentEntity.builder().id(1L).name("Mat").major("Crypto").build();
        studentRepository.save(studentEntity);
        assertThat(studentRepository.findById(studentEntity.getId()).isPresent());
        assertThat(studentRepository.findById(studentEntity.getId()).get().getMajor()).isEqualTo("Crypto");
        List<StudentEntity> studentEntities = studentRepository.findAll();
        assertThat(studentEntities).hasSize(3);
    }

    @Test
    public void whenDeleteStudent_thenRemoveStudent() {
           studentRepository.deleteById(1L);
        assertThat(studentRepository.findById(1L)).isEmpty();
        List<StudentEntity> studentEntities = studentRepository.findAll();
        assertThat(studentEntities).hasSize(2);
    }

}
