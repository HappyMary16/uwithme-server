package com.educationapp.server.university.servises;

import com.educationapp.server.common.api.admin.AddUniversityApi;
import com.educationapp.server.university.models.University;
import com.educationapp.server.university.repositories.UniversityRepository;
import com.educationapp.server.users.model.User;
import com.educationapp.server.users.servises.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserService userService;

    public User addUniversity(final AddUniversityApi addUniversityApi) {
        universityRepository.save(new University(addUniversityApi.getUniversityName()));
        return userService.save(addUniversityApi);
    }
}
