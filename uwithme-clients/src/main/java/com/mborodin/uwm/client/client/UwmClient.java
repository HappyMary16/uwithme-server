package com.mborodin.uwm.client.client;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.client.config.AuthClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
@FeignClient(name = "uwm-service",
        url = "#{'${uwm.api.host:localhost}:${uwm.api.port}'}",
        configuration = {AuthClientConfiguration.class})
public interface UwmClient {

    @GetMapping("/api/groups/{groupId}")
    GroupApi getGroupById(@PathVariable("groupId") Long groupId);

    @PostMapping("/api/groups")
    Long createGroup(GroupApi group);

    @PostMapping("/api/auth/signUp")
    UserApi register(RegisterApi group);
}
