package com.websystique.controller;

import com.websystique.domain.Student;
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
    public Iterable findAll(){
        return studentService.findAll();
    }

    @GetMapping("/name/{studentName}")
    public List<Student> findByName(@PathVariable String studentName){
        return studentService.findByName(studentName);
    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable Long id){
        return studentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@RequestBody Student student){
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@RequestBody Student student, @PathVariable Long id){
        return studentService.update(student,id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        studentService.delete(id);
    }
}
