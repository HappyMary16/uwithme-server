package com.educationapp.server.endpoints;

import java.util.List;

import com.educationapp.server.models.api.admin.AddLectureHallApi;
import com.educationapp.server.models.persistence.BuildingDb;
import com.educationapp.server.models.persistence.LectureHallDb;
import com.educationapp.server.repositories.BuildingsRepository;
import com.educationapp.server.repositories.LectureHallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectureHalls")
public class LectureHallEndpoint {

    @Autowired
    private BuildingsRepository buildingsRepository;

    @Autowired
    private LectureHallRepository lectureHallRepository;

    @GetMapping("/{universityId}")
    public List<LectureHallDb> getLectureHalls(@PathVariable("universityId") final Long universityId) {
        return lectureHallRepository.findAllByUniversityId(universityId);
    }

    @PostMapping
    public LectureHallDb addLectureHall(@RequestBody AddLectureHallApi addLectureHallApi) {
        final Long universityId = addLectureHallApi.getUniversityId();
        final String buildingName = addLectureHallApi.getBuildingName();

        final BuildingDb buildingDb = buildingsRepository.findByUniversityIdAndName(universityId, buildingName)
                                                         .orElseGet(() -> buildingsRepository
                                                                 .save(BuildingDb.builder()
                                                                                 .universityId(universityId)
                                                                                 .name(buildingName)
                                                                                 .build()));
        final LectureHallDb lectureHallDb = LectureHallDb.builder()
                                                         .name(addLectureHallApi.getLectureHallName())
                                                         .buildingId(buildingDb.getId())
                                                         .placeNumber(addLectureHallApi.getPlaceNumber())
                                                         .build();

        return lectureHallRepository.save(lectureHallDb);
    }
}
