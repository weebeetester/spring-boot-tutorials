package com.websystique.persistence.mapper;

import com.websystique.dto.StudentCreationDto;
import com.websystique.dto.StudentResponseDto;
import com.websystique.dto.StudentUpdationDto;
import com.websystique.persistence.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapStructConfig.class)
public interface StudentMapper {
    StudentResponseDto mapEntityToModel(final Student student);
    @Mapping(target = "id", ignore = true)
    Student mapModelToEntity(final StudentCreationDto studentCreationDto);

    Student mapModelToEntity(final StudentUpdationDto studentUpdationDto);

}
