package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.locations.BuildingApi;
import com.mborodin.uwm.model.mapper.BuildingMapper;
import com.mborodin.uwm.model.persistence.BuildingDb;
import com.mborodin.uwm.repositories.BuildingsRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
@RequestMapping("/api/buildings")
public class BuildingsEndpoint {

    private final BuildingMapper buildingMapper;
    private final BuildingsRepository buildingsRepository;

    @GetMapping
    public ResponseEntity<?> getBuildings() {
        final Long universityId = UserContextHolder.getUniversityId();
        return new ResponseEntity<>(buildingsRepository.findAllByUniversityId(universityId), OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public BuildingApi addBuilding(@RequestBody final BuildingApi buildingApi) {
        final Long universityId = UserContextHolder.getUniversityId();
        final String buildingName = buildingApi.getName();

        final BuildingDb buildingDb = buildingsRepository.save(BuildingDb.builder()
                                                                         .universityId(universityId)
                                                                         .name(buildingName)
                                                                         .build());

        return Optional.of(buildingDb)
                       .map(buildingMapper::toBuildingApi)
                       .orElseThrow(() -> new UnknownException(getLanguages()));
    }
}
