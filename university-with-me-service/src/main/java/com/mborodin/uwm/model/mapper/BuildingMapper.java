package com.mborodin.uwm.model.mapper;

import com.mborodin.uwm.api.locations.BuildingApi;
import com.mborodin.uwm.config.MapperConfiguration;
import com.mborodin.uwm.model.persistence.BuildingDb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface BuildingMapper {

    @Mapping(target = "shortName", ignore = true)
    BuildingApi toBuildingApi(BuildingDb building);
}
