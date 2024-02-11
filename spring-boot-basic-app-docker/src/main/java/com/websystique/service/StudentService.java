package com.websystique.service;

import com.websystique.dto.StudentCreationDto;
import com.websystique.dto.StudentResponseDto;
import com.websystique.dto.StudentUpdationDto;

import java.util.List;

public interface StudentService {
    List<StudentResponseDto> findAll();

    StudentResponseDto findById(Long id);

    List<StudentResponseDto> findByName(String name);

    StudentResponseDto create(StudentCreationDto studentCreationDto);

    StudentResponseDto update(StudentUpdationDto studentUpdationDto, Long id);

    void delete(Long id);
}
