package com.educationapp.server.university.endpoints;

import com.educationapp.server.authorization.endpoints.AuthEndpoint;
import com.educationapp.server.common.api.LoginApi;
import com.educationapp.server.common.api.admin.AddUniversityApi;
import com.educationapp.server.university.servises.UniversityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/university")
public class UniversityEndpoint {

    private final UniversityService universityService;
    private final AuthEndpoint authEndpoint;

    @PostMapping("/add")
    public ResponseEntity<?> addUniversity(@RequestBody AddUniversityApi addUniversityApi) {
        universityService.addUniversity(addUniversityApi);

        LoginApi loginApi = new LoginApi(addUniversityApi.getUsername(), addUniversityApi.getPassword());
        return authEndpoint.authenticateUser(loginApi);
    }

}
