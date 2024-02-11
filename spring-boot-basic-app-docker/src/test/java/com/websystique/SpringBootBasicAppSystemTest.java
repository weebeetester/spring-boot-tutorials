package com.websystique;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
    System tests may bring the database-used-in-test in inconsistent state.
    Below tests works fine individually, but some might fail if running all together.
    See https://stackoverflow.com/questions/65215214/spring-boot-integration-test-fails-if-transactional.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//This will start the server
@ActiveProfiles("test")
public class SpringBootBasicAppSystemTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String apiBaseUrl = "http://localhost";

    @BeforeEach
    public void setUp() {
        apiBaseUrl = apiBaseUrl.concat(":").concat(port + "").concat("/api/students");
    }

    @Test
    @DisplayName("Check if the context is loaded correctly")
    public void contextLoads() throws Exception {

    }

    @Test
    public void whenGetStudents_thenReturnStudentList() {
        ResponseEntity responseEntity = restTemplate.getForEntity(apiBaseUrl, List.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<LinkedHashMap<String, String>> students = (List<LinkedHashMap<String, String>>) responseEntity.getBody();
        assertThat(students).isNotNull();
        assertThat(students).hasSize(3);
        assertThat(students.get(0).get("name")).isEqualTo("Sam");
        assertThat(students.get(1).get("name")).isEqualTo("Tim");
        assertThat(students.get(2).get("name")).isEqualTo("Mat");
    }
/*
    @Test
    public void whenGetStudentsById_thenReturnStudent() {
        ResponseEntity<Student> responseEntity = restTemplate.getForEntity(apiBaseUrl.concat("/{id}"), Student.class, 1);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getName()).isEqualTo("Sam");
    }

    @Test
    public void whenGetStudentsByName_thenReturnStudentList() {
        ResponseEntity responseEntity = restTemplate.getForEntity(apiBaseUrl.concat("/name/{name}"), List.class, "Sam");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<LinkedHashMap<String, String>> students = (List<LinkedHashMap<String, String>>) responseEntity.getBody();
        assertThat(students).isNotNull();
        assertThat(students).hasSize(1);
        assertThat(students.get(0).get("name")).isEqualTo("Sam");
    }

    @Test
    public void whenPostStudent_thenCreateStudent() throws Exception {
        Student hans = Student.builder().name("Hans").major("Chemistry").build();
        ResponseEntity<Student> responseEntity = restTemplate.postForEntity(apiBaseUrl, hans, Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Student student = responseEntity.getBody();
        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getId()).isEqualTo(4L);
        assertThat(student.getName()).isEqualTo("Hans");

        //GET again
        ResponseEntity responseEn = restTemplate.getForEntity(apiBaseUrl, List.class);
        assertThat(responseEn.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<LinkedHashMap<String, String>> students = (List<LinkedHashMap<String, String>>) responseEn.getBody();
        assertThat(students).isNotNull();
        assertThat(students).hasSize(4);
    }

    @Test
    public void whenPostExistingStudent_thenReceive400() {
        Student sam = Student.builder().id(1L).name("Sam").major("Maths").build();
        ResponseEntity<Student> responseEntity = restTemplate.postForEntity(apiBaseUrl, sam, Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void whenPutStudent_thenUpdateStudent() {
        Student sam = Student.builder().id(1L).name("Sam").major("Politics").build();
        //restTemplate.put(apiBaseUrl.concat("/{id}"), sam,  1);
        restTemplate.execute(
                apiBaseUrl.concat("/{id}"),
                HttpMethod.PUT,
                requestCallback(sam),
                clientHttpResponse-> assertThat(clientHttpResponse.getStatusCode()).isEqualTo(HttpStatus.OK), 1);
    }


    @Test
    public void whenPutStudentWithMismatchingID_thenReceive400() {
        Student sam = Student.builder().id(1L).name("Sam").major("Politics").build();
        restTemplate.execute(
                apiBaseUrl.concat("/{id}"),
                HttpMethod.PUT,
                requestCallback(sam),
                clientHttpResponse-> assertThat(clientHttpResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST), 2);
    }

    @Test
    public void whenPutStudentWithoutExistingID_thenReceive404() {
        Student sam = Student.builder().id(4L).name("Sam").major("Politics").build();
        restTemplate.execute(
                apiBaseUrl.concat("/{id}"),
                HttpMethod.PUT,
                requestCallback(sam),
                clientHttpResponse-> assertThat(clientHttpResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND), 4);
    }


    @Test
    public void whenDeleteStudent_thenRemoveStudent() {
        restTemplate.execute(
                apiBaseUrl.concat("/{id}"),
                HttpMethod.DELETE,
                requestCallback(null),
                clientHttpResponse-> assertThat(clientHttpResponse.getStatusCode()).isEqualTo(HttpStatus.OK), 1);
    }


    @Test
    public void whenDeleteStudentWithoutExisting_thenReturnThrowException() {
        restTemplate.execute(
                apiBaseUrl.concat("/{id}"),
                HttpMethod.DELETE,
                requestCallback(null),
                clientHttpResponse-> assertThat(clientHttpResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND), 4);
    }

    private RequestCallback requestCallback(final Student student){
        return clientHttpRequest -> {
            if(student!=null){
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(clientHttpRequest.getBody(), student);
            }
            clientHttpRequest.getHeaders().add(
                    HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        };
    }
*/
}