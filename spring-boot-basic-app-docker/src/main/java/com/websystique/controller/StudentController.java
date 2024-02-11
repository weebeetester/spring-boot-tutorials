package com.websystique.controller;

import com.websystique.dto.StudentCreationDto;
import com.websystique.dto.StudentResponseDto;
import com.websystique.dto.StudentUpdationDto;
import com.websystique.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public Iterable<StudentResponseDto> findAll(@RequestParam(required = false) String name){
        return name != null ? studentService.findByName(name) : studentService.findAll();
    }

    @GetMapping("/{id}")
    public StudentResponseDto findById(@PathVariable Long id){
        return studentService.findById(id);
    }

    @PostMapping
    public ResponseEntity<StudentResponseDto> create(@RequestBody @Valid StudentCreationDto dto, UriComponentsBuilder uriComponentsBuilder){
        StudentResponseDto studentResponseDto = studentService.create(dto);
        URI location = uriComponentsBuilder.path("/api/students/{id}").buildAndExpand(studentResponseDto.getId()).toUri();
        return ResponseEntity.created(location).body(studentResponseDto);
    }

    @PutMapping("/{id}")
    public StudentResponseDto update(@RequestBody @Valid StudentUpdationDto dto, @PathVariable Long id){
        return studentService.update(dto,id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        studentService.delete(id);
    }
}
