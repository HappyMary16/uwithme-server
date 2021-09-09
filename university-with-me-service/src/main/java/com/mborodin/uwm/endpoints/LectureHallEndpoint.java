package com.mborodin.uwm.endpoints;

import com.mborodin.uwm.api.AddLectureHallApi;
import com.mborodin.uwm.models.persistence.BuildingDb;
import com.mborodin.uwm.models.persistence.LectureHallDb;
import com.mborodin.uwm.repositories.BuildingsRepository;
import com.mborodin.uwm.repositories.LectureHallRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/lectureHalls")
public class LectureHallEndpoint {

    private final BuildingsRepository buildingsRepository;

    private final LectureHallRepository lectureHallRepository;

    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_ADMIN')")
    @GetMapping
    public List<LectureHallDb> getLectureHalls() {
        final Long universityId = UserContextHolder.getUniversityId();
        return lectureHallRepository.findAllByUniversityId(universityId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public LectureHallDb addLectureHall(@RequestBody final AddLectureHallApi addLectureHallApi) {
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

        return lectureHallRepository.save(lectureHallDb);
    }
}
