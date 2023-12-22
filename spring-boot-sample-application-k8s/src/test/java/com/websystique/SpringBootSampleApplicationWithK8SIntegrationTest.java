package com.websystique;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websystique.domain.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    Integration Tests can be run using MockMvc ( without any mocking for any other layer).
    Using TestRestTemplate to run integration test is not a good idea though(as that become more of System test
    where DB state can become inconsistent between the tests).
    See https://stackoverflow.com/questions/65215214/spring-boot-integration-test-fails-if-transactional.
 */

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class SpringBootSampleApplicationWithK8SIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void whenGetStudents_thenReturnJsonArray() throws Exception{

        this.mockMvc.perform(get("/api/students").contentType(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Sam")))
                .andExpect(jsonPath("$[1].name", is("Tim")))
                .andExpect(jsonPath("$[2].name", is("Mat")));
    }


    @Test
    public void whenGetStudentsById_thenReturnStudent() throws Exception{

        this.mockMvc.perform(get("/api/students/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name", is("Sam")));
    }

    @Test
    public void whenGetStudentsByName_thenReturnJsonArray() throws Exception{

        this.mockMvc.perform(get("/api/students/name/{name}", "Sam").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Sam")));

    }

    @Test
    public void whenPostStudent_thenCreateStudent() throws Exception {

        Student sam = Student.builder().name("Sam").major("Maths").build();
        this.mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(toJson(sam)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Sam")));

    }

    @Test
    public void whenPutStudent_thenUpdateStudent() throws Exception {

        Student sam = Student.builder().id(1L).name("Sam").major("Maths").build();
        this.mockMvc.perform(put("/api/students/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(toJson(sam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Sam")));
    }


    @Test
    public void whenDeleteStudent_thenRemoveStudent() throws Exception {

        this.mockMvc.perform(delete("/api/students/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
