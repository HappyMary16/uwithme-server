package com.mborodin.uwm.client.client.core;

import com.mborodin.uwm.api.structure.UniversityApi;
import com.mborodin.uwm.client.config.AuthClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "uwm-service",
        url = "#{'${uwm.api.host:localhost}:${uwm.api.port}'}",
        configuration = {AuthClientConfiguration.class})
public interface TenantClient {

    @GetMapping("api/universities")
    UniversityApi getUniversity();
}
