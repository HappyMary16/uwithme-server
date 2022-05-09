package com.mborodin.uwm.model.mapper;

import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.config.MapperConfiguration;
import com.mborodin.uwm.model.persistence.DepartmentDb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface DepartmentMapper {

    @Mapping(target = "shortName", ignore = true)
    @Mapping(target = "instituteId", source = "instituteId")
    DepartmentApi toDepartmentApi(DepartmentDb department);

    DepartmentDb toDepartmentDb(DepartmentApi department);
}
