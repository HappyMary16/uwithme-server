package com.educationapp.server.university.endpoints;

import com.educationapp.server.common.api.admin.AddInstituteApi;
import com.educationapp.server.university.models.Institute;
import com.educationapp.server.university.repositories.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/institute")
public class InstituteEndpoint {

    @Autowired
    private InstituteRepository instituteRepository;

    @PostMapping("/add")
    public Institute addInstitute(@RequestBody AddInstituteApi addInstituteApi) {
        final Institute institute = Institute.builder()
                                             .name(addInstituteApi.getInstituteName())
                                             .universityId(addInstituteApi.getUniversityId())
                                             .build();
        return instituteRepository.save(institute);
    }

}
