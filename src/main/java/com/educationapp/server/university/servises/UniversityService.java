package com.educationapp.server.university.servises;

import com.educationapp.server.common.api.UserApi;
import com.educationapp.server.common.api.admin.AddUniversityApi;
import com.educationapp.server.university.models.University;
import com.educationapp.server.university.repositories.UniversityRepository;
import com.educationapp.server.users.servises.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserService userService;

    public UserApi addUniversity(final AddUniversityApi addUniversityApi) {
        final University university = universityRepository.save(new University(addUniversityApi.getUniversityName()));
        return userService.save(addUniversityApi, university);
    }
}
