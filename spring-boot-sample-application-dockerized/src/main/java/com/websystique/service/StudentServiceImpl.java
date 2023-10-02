package com.websystique.service;

import com.websystique.domain.Student;
import com.websystique.exception.StudentAlreadyExistException;
import com.websystique.exception.StudentIdMismatchException;
import com.websystique.exception.StudentNotFoundException;
import com.websystique.persistence.entity.StudentEntity;
import com.websystique.persistence.repo.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Transactional
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Student> findAll(){
        return StreamSupport.stream(studentRepository.findAll().spliterator(), false).map(this::convertToDto).collect(Collectors.toList());
    }

    public Student findById(Long id){
        return convertToDto(studentRepository.findById(id).orElseThrow(StudentNotFoundException::new));
    }

    public List<Student> findByName(String name){
        return studentRepository.findByName(name).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Student create(Student student){
        if( (student.getId() != null) && studentRepository.findById(student.getId()).isPresent()){
            throw new StudentAlreadyExistException();
        }
        return convertToDto(studentRepository.save(convertToEntity(student)));
    }

    public Student update(Student student, Long id){
        if(student.getId() != id){
            throw new StudentIdMismatchException();
        }
        studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        return convertToDto(studentRepository.save(convertToEntity(student)));
    }

    public void delete(Long id){
        studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentRepository.deleteById(id);
    }

    private Student convertToDto(StudentEntity studentEntity){
        Student student = modelMapper.map(studentEntity, Student.class);
        return student;
    }

    private StudentEntity convertToEntity(Student student){
        StudentEntity studentEntity = modelMapper.map(student, StudentEntity.class);
        return studentEntity;
    }

}
