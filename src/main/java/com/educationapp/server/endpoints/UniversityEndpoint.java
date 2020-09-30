package com.educationapp.server.endpoints;

import com.educationapp.server.models.api.LoginApi;
import com.educationapp.server.models.api.admin.AddUniversityApi;
import com.educationapp.server.services.UniversityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/universities")
public class UniversityEndpoint {

    private final UniversityService universityService;
    private final AuthEndpoint authEndpoint;

    @PostMapping
    public ResponseEntity<?> addUniversity(@RequestBody final AddUniversityApi addUniversityApi) {
        universityService.addUniversity(addUniversityApi);

        final LoginApi loginApi = new LoginApi(addUniversityApi.getUsername(), addUniversityApi.getPassword());
        return authEndpoint.authenticateUser(loginApi);
    }
}
