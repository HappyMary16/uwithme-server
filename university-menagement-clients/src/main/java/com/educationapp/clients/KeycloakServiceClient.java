package com.educationapp.clients;

import com.educationapp.api.KeycloakUserApi;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

public interface KeycloakServiceClient {

    @RequestMapping(value = "/auth/admin/realms/{realm}/users/{id}", method = GET)
    @ResponseBody
    KeycloakUserApi getUserById(@PathVariable("id") String id, @PathVariable("realm") String realm);
}
