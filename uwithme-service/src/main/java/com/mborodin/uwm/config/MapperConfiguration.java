package com.mborodin.uwm.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MapperConfiguration {

}
