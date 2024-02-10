package com.websystique.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websystique.config.WebSecurityConfig;
import com.websystique.dto.StudentCreationDto;
import com.websystique.dto.StudentUpdationDto;
import com.websystique.dto.StudentResponseDto;
import com.websystique.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfig.class)
@WebMvcTest(StudentController.class)
@ActiveProfiles("test")
public class StudentControllerTest {

    @MockBean
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetStudents_thenReturnJsonArray() throws Exception{

        List<StudentResponseDto> students = List.of(
                StudentResponseDto.builder().name("Sam").email("sam@hotmail.com").major("Maths").build(),
                StudentResponseDto.builder().name("Tim").email("tim@hotmail.com").major("Physics").build(),
                StudentResponseDto.builder().name("Mat").email("mat@hotmail.com").major("English").build());

        when(studentService.findAll()).thenReturn(students);

        this.mockMvc.perform(get("/api/students").contentType(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Sam")))
                .andExpect(jsonPath("$[1].name", is("Tim")))
                .andExpect(jsonPath("$[2].name", is("Mat")));

        verify(studentService, VerificationModeFactory.times(1)).findAll();
    }


    @Test
    public void whenGetStudentsByName_thenReturnJsonArray() throws Exception{

        List<StudentResponseDto> students = List.of(
                StudentResponseDto.builder().name("Sam").email("sam@hotmail.com").major("Maths").build());

        when(studentService.findByName("Sam")).thenReturn(students);

        this.mockMvc.perform(get("/api/students").param("name", "Sam").contentType(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Sam")));

        verify(studentService, VerificationModeFactory.times(1)).findByName("Sam");

    }

    @Test
    public void whenGetStudentsById_thenReturnStudent() throws Exception{

        StudentResponseDto sam = StudentResponseDto.builder().id(1L).name("Sam").email("sam@hotmail.com").major("Maths").build();

        when(studentService.findById(1L)).thenReturn(sam);

        this.mockMvc.perform(get("/api/students/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name", is("Sam")));
        verify(studentService, VerificationModeFactory.times(1)).findById(1L);
    }

    @Test
    public void whenPostStudent_thenCreateStudent() throws Exception {

        StudentCreationDto sam = StudentCreationDto.builder().name("Sam").email("sam@hotmail.com").major("Maths").build();
        StudentResponseDto samResponse = StudentResponseDto.builder().name("Sam").email("sam@hotmail.com").major("Maths").build();
        when(studentService.create(Mockito.any(StudentCreationDto.class))).thenReturn(samResponse);

        this.mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(toJson(sam)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Sam")));

        verify(studentService, VerificationModeFactory.times(1)).create(Mockito.any());
    }

    @Test
    public void whenPutStudent_thenUpdateStudent() throws Exception {

        StudentUpdationDto sam = StudentUpdationDto.builder().name("Sam").email("sam@hotmail.com").major("Maths").build();
        StudentResponseDto samResponse = StudentResponseDto.builder().name("Sam").major("Maths").build();


        when(studentService.update(Mockito.any(StudentUpdationDto.class), Mockito.anyLong())).thenReturn(samResponse);

        this.mockMvc.perform(put("/api/students/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(toJson(sam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Sam")));

        verify(studentService, VerificationModeFactory.times(1)).update(Mockito.any(StudentUpdationDto.class), Mockito.anyLong());
    }


    @Test
    public void whenDeleteStudent_thenRemoveStudent() throws Exception {

        doNothing().when(studentService).delete(Mockito.anyLong());

        this.mockMvc.perform(delete("/api/students/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentService, VerificationModeFactory.times(1)).delete(Mockito.anyLong());
    }

    private static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
