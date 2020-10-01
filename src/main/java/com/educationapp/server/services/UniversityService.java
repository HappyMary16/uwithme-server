package com.educationapp.server.services;

import com.educationapp.server.models.api.admin.AddUniversityApi;
import com.educationapp.server.models.persistence.UniversityDb;
import com.educationapp.server.repositories.UniversityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UserService userService;

    public void addUniversity(final AddUniversityApi addUniversityApi) {
        final UniversityDb toCreate = new UniversityDb(addUniversityApi.getUniversityName());
        final UniversityDb university = universityRepository.save(toCreate);
        userService.save(addUniversityApi, university);
    }
}
