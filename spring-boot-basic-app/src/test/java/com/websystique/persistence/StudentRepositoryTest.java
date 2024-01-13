package com.websystique.persistence;

import com.websystique.persistence.entity.Student;
import com.websystique.persistence.repo.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
    - Testing Controller layer:
        - We [can] use @WebMvcTest(Annotation that can be used for a Spring MVC test that focuses only on Spring MVC components).
        - This Autoconfigure annotation only loads beans required for testing the web layer.
        - Arguably they might be called as narrow Integration tests (instead of unit tests) as they rely on Spring application context.
    - Testing Data layer:
        - We [can] use @DataJpaTest (Annotation for a JPA test that focuses only on JPA components).
        - This Autoconfigure annotation only loads beans required for testing the data layer.
        - Arguably they might be called as narrow Integration tests (instead of unit tests) as they rely on Spring application context.
    - Testing other layers:
        - We [can] just write unit tests with plain old JUnit+mock frameworks (@ExtendWith(MockitoExtension.class))
          without any Spring/SpringBoot support.
*/

/*
    Do not test the framework:
    - While testing data layer, it is not required to test the inherited methods (from JpaRepository for example) as this will be a waste
    (we do not want to test the framework itself). So only test what you explicitly added. Even you may want to skip the inferred methods (findByName e.g.)
     even if they are explicitly added.
    Best candidate for testing [data layer] are custom JPQL or SQL Queries.
 */
/*
    Below Hypothetical example just shows that everything is testable (including framework methods).
    In real life scenarios, you will ONLY test the custom JPQL or SQL Queries.
 */

@DataJpaTest
@ActiveProfiles("test")
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void whenGetStudents_thenReturnStudentList(){
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(3);

        assertThat(students.get(0).getId()).isEqualTo(1L);
        assertThat(students.get(0).getName()).isEqualTo("Sam");
        assertThat(students.get(0).getEmail()).isEqualTo("sam@hotmail.com");
        assertThat(students.get(0).getMajor()).isEqualTo("Maths");

        assertThat(students.get(1).getId()).isEqualTo(2L);
        assertThat(students.get(1).getName()).isEqualTo("Tim");
        assertThat(students.get(1).getEmail()).isEqualTo("tim@hotmail.com");
        assertThat(students.get(1).getMajor()).isEqualTo("Physics");

        assertThat(students.get(2).getId()).isEqualTo(3L);
        assertThat(students.get(2).getName()).isEqualTo("Mat");
        assertThat(students.get(2).getEmail()).isEqualTo("mat@hotmail.com");
        assertThat(students.get(2).getMajor()).isEqualTo("English");
    }

    @Test
    void whenGetStudentsById_thenReturnStudent(){
        Optional<Student> student = studentRepository.findById(1L);
        assertThat(student).isPresent();

        assertThat(student.get().getName()).isEqualTo("Sam");
        assertThat(student.get().getEmail()).isEqualTo("sam@hotmail.com");
        assertThat(student.get().getMajor()).isEqualTo("Maths");
    }


    @Test
    public void whenGetStudentsByName_thenReturnStudentList() {
        List<Student> students = studentRepository.findByName("Sam");
        assertThat(students).hasSize(1);

        assertThat(students.get(0).getId()).isEqualTo(1L);
        assertThat(students.get(0).getName()).isEqualTo("Sam");
        assertThat(students.get(0).getEmail()).isEqualTo("sam@hotmail.com");
        assertThat(students.get(0).getMajor()).isEqualTo("Maths");
    }

    @Test
    public void whenPostStudent_thenCreateStudent() {
        Student student = Student.builder().name("Hans").email("hans@hotmail.com").major("Chemistry").build();
        studentRepository.save(student);
        assertThat(studentRepository.findById(student.getId())).isPresent();
        assertThat(student.getId()).isEqualTo(4L);
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(4);
    }

    @Test
    public void whenPutStudent_thenUpdateStudent() {
        Student student = Student.builder().id(3L).name("Mat").email("mat@hotmail.com").major("Crypto").build();
        studentRepository.save(student);
        assertThat(studentRepository.findById(student.getId())).isPresent();
        assertThat(studentRepository.findById(student.getId()).get().getMajor()).isEqualTo("Crypto");
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(3);
    }

    @Test
    public void whenDeleteStudent_thenRemoveStudent() {
           studentRepository.deleteById(1L);
        assertThat(studentRepository.findById(1L)).isEmpty();
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(2);
    }

}
