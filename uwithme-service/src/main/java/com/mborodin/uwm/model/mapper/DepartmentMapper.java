package com.mborodin.uwm.model.mapper;

import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.config.MapperConfiguration;
import com.mborodin.uwm.model.persistence.DepartmentDb;
import com.mborodin.uwm.model.persistence.TenantDepartmentDb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface DepartmentMapper {

    @Mapping(target = "shortName", ignore = true)
    @Mapping(target = "id", source = "departmentId")
    @Mapping(target = "instituteId", source = "mainDepartmentId")
    @Mapping(target = "universityId", source = "tenantId")
    DepartmentApi toDepartmentApi(TenantDepartmentDb department);

    @Mapping(source = "id", target = "departmentId")
    @Mapping(source = "instituteId", target = "mainDepartmentId")
    @Mapping(source = "universityId", target = "tenantId")
    TenantDepartmentDb toDb(DepartmentApi department);
}
