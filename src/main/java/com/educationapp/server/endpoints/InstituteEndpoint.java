package com.educationapp.server.endpoints;

import com.educationapp.server.models.api.admin.AddInstituteApi;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.repositories.InstituteRepository;
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
    public InstituteDb addInstitute(@RequestBody AddInstituteApi addInstituteApi) {
        final InstituteDb institute = InstituteDb.builder()
                                                 .name(addInstituteApi.getInstituteName())
                                                 .universityId(addInstituteApi.getUniversityId())
                                                 .build();
        return instituteRepository.save(institute);
    }

}
