package com.mborodin.uwm;

import java.util.HashMap;
import java.util.Map;

import com.mborodin.uwm.clients.AuthServiceClient;
import com.mborodin.uwm.clients.GroupServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientFactory {

    @Value("${university-with-me.host:localhost}")
    private String host;

    @Value("${university-with-me.port:${server.port}}")
    private int port;

    private final Map<RestTemplate, AuthServiceClient> authServiceClients = new HashMap<>();
    private final Map<RestTemplate, GroupServiceClient> groupServiceClients = new HashMap<>();

    public AuthServiceClient getAuthServiceClient(final RestTemplate restTemplate) {
        if (!authServiceClients.containsKey(restTemplate)) {
            authServiceClients.put(restTemplate, new AuthServiceClient(host, port, restTemplate));
        }

        return authServiceClients.get(restTemplate);
    }

    public GroupServiceClient getGroupServiceClient(final RestTemplate restTemplate) {
        if (!groupServiceClients.containsKey(restTemplate)) {
            groupServiceClients.put(restTemplate, new GroupServiceClient(host, port, restTemplate));
        }

        return groupServiceClients.get(restTemplate);
    }
}
