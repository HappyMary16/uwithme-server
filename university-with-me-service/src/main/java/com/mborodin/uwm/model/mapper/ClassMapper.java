package com.mborodin.uwm.model.mapper;

import com.mborodin.uwm.api.locations.ClassApi;
import com.mborodin.uwm.config.MapperConfiguration;
import com.mborodin.uwm.model.persistence.LectureHallDb;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface ClassMapper {

    ClassApi toClassApi(LectureHallDb lectureHall);
}
