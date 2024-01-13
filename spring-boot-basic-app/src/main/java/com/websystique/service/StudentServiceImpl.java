package com.websystique.service;

import com.websystique.dto.StudentCreationDto;
import com.websystique.dto.StudentResponseDto;
import com.websystique.dto.StudentUpdationDto;
import com.websystique.exception.StudentIdMismatchException;
import com.websystique.exception.StudentNotFoundException;
import com.websystique.persistence.entity.Student;
import com.websystique.persistence.mapper.StudentMapper;
import com.websystique.persistence.repo.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public List<StudentResponseDto> findAll(){
        return studentRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public StudentResponseDto findById(Long id){
        return convertToDto(studentRepository.findById(id).orElseThrow(StudentNotFoundException::new));
    }

    public List<StudentResponseDto> findByName(String name){
        return studentRepository.findByName(name).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public StudentResponseDto create(StudentCreationDto studentCreationDto){
        // If there can be other unique attributes of a student, add the check here
        // throw new StudentAlreadyExistException(); // in case of violation
        return convertToDto(studentRepository.save(convertToEntity(studentCreationDto)));
    }

    public StudentResponseDto update(StudentUpdationDto studentUpdationDto, Long id){
        if(!Objects.equals(studentUpdationDto.getId(), id)){
            throw new StudentIdMismatchException();
        }
        studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        return convertToDto(studentRepository.save(convertToEntity(studentUpdationDto)));
    }

    public void delete(Long id){
        studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentRepository.deleteById(id);
    }

    private StudentResponseDto convertToDto(Student student){
        return this.studentMapper.mapEntityToModel(student);
    }

    private Student convertToEntity(StudentCreationDto studentCreationDto){
        return this.studentMapper.mapModelToEntity(studentCreationDto);
    }

    private Student convertToEntity(StudentUpdationDto studentUpdationDto){
        return this.studentMapper.mapModelToEntity(studentUpdationDto);
    }

}
