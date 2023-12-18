package com.websystique.persistence.mapper;

import com.websystique.domain.Student;
import com.websystique.persistence.entity.StudentEntity;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapStructConfig.class)
public interface StudentMapper {
    Student mapEntityToModel(final StudentEntity studentEntity);

    StudentEntity mapModelToEntity(final Student student);
}
