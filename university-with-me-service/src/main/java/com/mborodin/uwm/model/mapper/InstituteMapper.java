package com.mborodin.uwm.model.mapper;

import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.config.MapperConfiguration;
import com.mborodin.uwm.model.persistence.InstituteDb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface InstituteMapper {

    @Mapping(target = "shortName", ignore = true)
    InstituteApi toInstituteApi(InstituteDb institute);
}
