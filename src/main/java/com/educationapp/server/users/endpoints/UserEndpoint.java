package com.educationapp.server.users.endpoints;

import java.util.List;
import java.util.stream.Collectors;

import com.educationapp.server.common.api.UserApi;
import com.educationapp.server.users.model.persistence.UserDB;
import com.educationapp.server.users.repositories.TeacherDataRepository;
import com.educationapp.server.users.repositories.UserRepository;
import com.educationapp.server.users.servises.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherDataRepository teacherDataRepository;

    @GetMapping(value = "/{id}")
    public UserDB getUser(@PathVariable(value = "id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping(value = "/{username}")
    public UserDB getUser(@PathVariable(value = "username") String username) {
        return userRepository.findByUsername(username).get();
    }

    @GetMapping(value = "/teachers/{universityId}")
    public List<UserApi> getTeachersByUniversityId(
            @PathVariable(value = "universityId") Long universityId) {
        return teacherDataRepository.findAllByUniversityId(universityId)
                                    .stream()
                                    .map(userService::mapTeacherDataDbToUserApi)
                                    .collect(Collectors.toList());
    }

    @GetMapping(value = "/teachers/{groupId}/group")
    public List<UserApi> getTeachersByGroupId(
            @PathVariable(value = "groupId") Long groupId) {
        return teacherDataRepository.findAllByGroupId(groupId)
                                    .stream()
                                    .map(userService::mapTeacherDataDbToUserApi)
                                    .collect(Collectors.toList());
    }
}