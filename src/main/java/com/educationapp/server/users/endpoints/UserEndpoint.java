package com.educationapp.server.users.endpoints;

import static com.educationapp.server.common.enums.Role.TEACHER;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.educationapp.server.users.model.persistence.UserDB;
import com.educationapp.server.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private UserRepository userRepository;

    @GetMapping(value = "/{id}")
    public UserDB getUser(@PathVariable(value = "id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping(value = "/{username}")
    public UserDB getUser(@PathVariable(value = "username") String username) {
        return userRepository.findByUsername(username).get();
    }

    @GetMapping(value = "/teachers/{universityId}")
    public ResponseEntity<List<UserDB>> getTeachersByUniversityId(
            @PathVariable(value = "universityId") Long universityId) {
        return new ResponseEntity<>(userRepository.findAllByRoleAndUniversityId(TEACHER.getId(), universityId), OK);
    }
}