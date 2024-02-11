package com.websystique.service;

import com.websystique.dto.StudentCreationDto;
import com.websystique.dto.StudentResponseDto;
import com.websystique.dto.StudentUpdationDto;
import com.websystique.exception.StudentWithGivenEmailAlreadyExistException;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public List<StudentResponseDto> findAll(){
        return studentRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public StudentResponseDto findById(Long id){
        return convertToDto(studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)));
    }

    public List<StudentResponseDto> findByName(String name){
        return studentRepository.findByName(name).stream().map(this::convertToDto).toList();
    }

    public StudentResponseDto create(StudentCreationDto studentCreationDto){
        List<StudentResponseDto> students = studentRepository.findByEmail(studentCreationDto.getEmail()).stream().map(this::convertToDto).toList();
        if(!students.isEmpty()){
            throw new StudentWithGivenEmailAlreadyExistException("A Student with "+ studentCreationDto.getEmail() + " already exist");
        }
        return convertToDto(studentRepository.save(convertToEntity(studentCreationDto)));
    }

    public StudentResponseDto update(StudentUpdationDto studentUpdationDto, Long id){
        //Make sure if id is present in the body, it is same as from url
        if(studentUpdationDto.getId() != null &&
                !Objects.equals(studentUpdationDto.getId(), id)){
            throw new StudentIdMismatchException("Mismatching id in body and url");
        }

        //As email is unique (and we are allowing a change), we must make sure this email does not exist in DB with a different user.
        List<StudentResponseDto> studentsFromDb = studentRepository.findByEmail(studentUpdationDto.getEmail()).stream().map(this::convertToDto).toList();
        if((!studentsFromDb.isEmpty()) && !Objects.equals(studentsFromDb.get(0).getId(), id)){
            throw new StudentWithGivenEmailAlreadyExistException("Another Student with "+ studentUpdationDto.getEmail() + " already exist");
        }

        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(studentUpdationDto.getName());
                    student.setEmail(studentUpdationDto.getEmail());
                    student.setMajor(studentUpdationDto.getMajor());
                    return convertToDto(studentRepository.save(student));
                })
                .orElseGet(()->{
                    return convertToDto(studentRepository.save(convertToEntity(studentUpdationDto)));
                });
    }

    public void delete(Long id){
        studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
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
