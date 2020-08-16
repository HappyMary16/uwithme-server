package com.educationapp.server.services;

import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.api.admin.AddUniversityApi;
import com.educationapp.server.models.persistence.UniversityDb;
import com.educationapp.server.repositories.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserService userService;

    public UserApi addUniversity(final AddUniversityApi addUniversityApi) {
        final UniversityDb
                university = universityRepository.save(new UniversityDb(addUniversityApi.getUniversityName()));
        return userService.save(addUniversityApi, university);
    }
}
