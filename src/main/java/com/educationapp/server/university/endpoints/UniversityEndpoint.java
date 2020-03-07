package com.educationapp.server.university.endpoints;

import com.educationapp.server.authorization.endpoints.AuthEndpoint;
import com.educationapp.server.common.api.LoginApi;
import com.educationapp.server.common.api.UserApi;
import com.educationapp.server.common.api.admin.AddUniversityApi;
import com.educationapp.server.university.servises.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/university")
@CrossOrigin("*")
public class UniversityEndpoint {

    @Autowired
    private UniversityService universityService;

    @Autowired
    AuthEndpoint authEndpoint;

    @PostMapping("/add")
    public ResponseEntity<UserApi> addUniversity(@RequestBody AddUniversityApi addUniversityApi) {
        universityService.addUniversity(addUniversityApi);

        LoginApi loginApi = new LoginApi(addUniversityApi.getUsername(), addUniversityApi.getPassword());
        return authEndpoint.authenticateUser(loginApi);
    }

}
