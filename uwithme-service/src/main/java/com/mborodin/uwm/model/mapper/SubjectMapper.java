package com.mborodin.uwm.model.mapper;

import com.mborodin.uwm.api.SubjectApi;
import com.mborodin.uwm.config.MapperConfiguration;
import com.mborodin.uwm.model.persistence.SubjectDB;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface SubjectMapper {


    @Mapping(target = "subjectId", source = "id")
    @Mapping(target = "subjectName", source = "name")
    SubjectApi toApi(SubjectDB subject);
}
