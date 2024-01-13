package com.websystique.controller;

import com.websystique.dto.StudentCreationDto;
import com.websystique.dto.StudentResponseDto;
import com.websystique.dto.StudentUpdationDto;
import com.websystique.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Iterable<StudentResponseDto> findAll(){
        return studentService.findAll();
    }

    @GetMapping("/name/{studentName}")
    public List<StudentResponseDto> findByName(@PathVariable String studentName){
        return studentService.findByName(studentName);
    }

    @GetMapping("/{id}")
    public StudentResponseDto findById(@PathVariable Long id){
        return studentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto create(@RequestBody StudentCreationDto dto){
        return studentService.create(dto);
    }

    @PutMapping("/{id}")
    public StudentResponseDto update(@RequestBody StudentUpdationDto dto, @PathVariable Long id){
        return studentService.update(dto,id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        studentService.delete(id);
    }
}
