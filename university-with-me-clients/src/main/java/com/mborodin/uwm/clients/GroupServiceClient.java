package com.mborodin.uwm.clients;

import com.mborodin.uwm.api.AddGroupApi;
import com.mborodin.uwm.api.GroupApi;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
public class GroupServiceClient {

    private String host;
    private int port;
    private RestTemplate restTemplate;

    @SneakyThrows
    public GroupApi getGroupById(final Long groupId) {
        log.debug("Register User: {}", groupId);

        final String getUserUri = "http://" + host + ":" + port + "/api/groups/" + groupId;

        final ResponseEntity<GroupApi> response = restTemplate.getForEntity(getUserUri, GroupApi.class);

        log.debug("Created User: {}", response.getBody());

        return response.getBody();
    }

    @SneakyThrows
    public Long createGroup(final AddGroupApi group) {
        log.debug("Register User: {}", group);

        final String getUserUri = "http://" + host + ":" + port + "/api/groups";

        final ResponseEntity<Long> response = restTemplate.postForEntity(getUserUri, group, Long.class);

        log.debug("Created User: {}", response.getBody());

        return response.getBody();
    }
}
