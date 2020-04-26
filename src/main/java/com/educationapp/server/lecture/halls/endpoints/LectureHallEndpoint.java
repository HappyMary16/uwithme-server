package com.educationapp.server.lecture.halls.endpoints;

import java.util.List;

import com.educationapp.server.common.api.admin.AddLectureHallApi;
import com.educationapp.server.lecture.halls.models.BuildingDb;
import com.educationapp.server.lecture.halls.models.LectureHallDb;
import com.educationapp.server.lecture.halls.repositoryes.BuildingsRepository;
import com.educationapp.server.lecture.halls.repositoryes.LectureHallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
