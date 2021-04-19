package com.educationapp.server.endpoints;

import com.educationapp.api.AddLectureHallApi;
import com.educationapp.server.models.persistence.BuildingDb;
import com.educationapp.server.models.persistence.LectureHallDb;
import com.educationapp.server.repositories.BuildingsRepository;
import com.educationapp.server.repositories.LectureHallRepository;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping("/api/lectureHalls")
public class LectureHallEndpoint {

    private final BuildingsRepository buildingsRepository;

    private final LectureHallRepository lectureHallRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getLectureHalls() {
        final Long universityId = UserContextHolder.getUniversityId();
        return new ResponseEntity<>(lectureHallRepository.findAllByUniversityId(universityId), OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addLectureHall(@RequestBody final AddLectureHallApi addLectureHallApi) {
        final Long universityId = UserContextHolder.getUniversityId();
        final String buildingName = addLectureHallApi.getBuildingName();

        final BuildingDb buildingDb = buildingsRepository.findByUniversityIdAndName(universityId, buildingName)
                                                         .orElseGet(() -> BuildingDb.builder()
                                                                                    .universityId(universityId)
                                                                                    .name(buildingName)
                                                                                    .build());
        final LectureHallDb lectureHallDb = LectureHallDb.builder()
                                                         .name(addLectureHallApi.getLectureHallName())
                                                         .building(buildingDb)
                                                         .placeNumber(addLectureHallApi.getPlaceNumber())
                                                         .build();

        return new ResponseEntity<>(lectureHallRepository.save(lectureHallDb), OK);
    }
}
