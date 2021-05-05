package com.mborodin.uwm.endpoints;

import static org.springframework.http.HttpStatus.OK;

import com.mborodin.uwm.api.AddLectureHallApi;
import com.mborodin.uwm.models.persistence.BuildingDb;
import com.mborodin.uwm.models.persistence.LectureHallDb;
import com.mborodin.uwm.repositories.BuildingsRepository;
import com.mborodin.uwm.repositories.LectureHallRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
