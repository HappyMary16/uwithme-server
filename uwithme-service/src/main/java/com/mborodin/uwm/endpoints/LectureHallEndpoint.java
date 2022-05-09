package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.locations.ClassApi;
import com.mborodin.uwm.model.mapper.ClassMapper;
import com.mborodin.uwm.model.persistence.LectureHallDb;
import com.mborodin.uwm.repositories.LectureHallRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/lectureHalls")
public class LectureHallEndpoint {

    private final ClassMapper classMapper;

    private final LectureHallRepository lectureHallRepository;

    @Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
    @GetMapping
    public List<ClassApi> getLectureHalls() {
        final Long universityId = UserContextHolder.getUniversityId();
        return lectureHallRepository.findAllByUniversityId(universityId)
                                    .stream()
                                    .map(classMapper::toClassApi)
                                    .collect(Collectors.toList());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ClassApi addLectureHall(@RequestBody final ClassApi classApi) {
        final LectureHallDb lectureHallDb = LectureHallDb.builder()
                                                         .name(classApi.getName())
                                                         .buildingId(classApi.getBuildingId())
                                                         .placeNumber(classApi.getPlaceNumber())
                                                         .build();

        return Optional.of(lectureHallRepository.save(lectureHallDb))
                       .map(classMapper::toClassApi)
                       .orElseThrow(() -> new UnknownException(getLanguages()));
    }
}
