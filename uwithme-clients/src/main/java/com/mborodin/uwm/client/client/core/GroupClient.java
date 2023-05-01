package com.mborodin.uwm.client.client.core;

import java.util.List;

import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.client.config.AuthClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "uwm-service",
        url = "#{'${uwm.api.host:localhost}:${uwm.api.port}'}",
        configuration = {AuthClientConfiguration.class})
public interface GroupClient {

    @GetMapping("/api/groups/{groupId}")
    GroupApi getGroupById(@PathVariable("groupId") Long groupId);

    @GetMapping("/api/groups")
    List<GroupApi> getGroupsByUsersTenant();

    @GetMapping("/api/groups")
    List<GroupApi> getGroupsByDepartment(@RequestParam("departmentId") String departmentId);

    @PostMapping("/api/groups")
    GroupApi createGroup(GroupApi group);

    @DeleteMapping("/api/groups/{groupId}")
    void deleteGroup(@PathVariable("groupId") Long groupId);
}
