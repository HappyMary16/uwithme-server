package com.mborodin.uwm.model.mapper;

import com.mborodin.uwm.api.structure.UniversityApi;
import com.mborodin.uwm.config.MapperConfiguration;
import com.mborodin.uwm.model.persistence.UniversityDb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface UniversityMapper {

    @Mapping(target = "shortName", ignore = true)
    UniversityApi toUniversityApi(UniversityDb university);
}