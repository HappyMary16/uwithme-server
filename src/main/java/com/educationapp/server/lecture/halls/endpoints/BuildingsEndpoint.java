package com.educationapp.server.lecture.halls.endpoints;

import java.util.List;

import com.educationapp.server.lecture.halls.models.BuildingDb;
import com.educationapp.server.lecture.halls.repositoryes.BuildingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/buildings")
public class BuildingsEndpoint {

    @Autowired
    private BuildingsRepository buildingsRepository;

    @GetMapping("/{universityId}")
    public List<BuildingDb> getBuildings(@PathVariable("universityId") final Long universityId) {
        return buildingsRepository.findAllByUniversityId(universityId);
    }
}
