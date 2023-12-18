package com.websystique.service;

import com.websystique.domain.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    Student findById(Long id);

    List<Student> findByName(String name);

    Student create(Student student);

    Student update(Student student, Long id);

    void delete(Long id);
}
