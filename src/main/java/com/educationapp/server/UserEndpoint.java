package com.educationapp.server;

import java.util.List;

import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserEndpoint {

    @Autowired
    private UserRepository userService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDB getUser(@PathVariable(value = "id") Long id) {
        return userService.findById(id).orElse(null);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<UserDB> getAll() {
        return (List<UserDB>) userService.findAll();
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public UserDB getUser(@PathVariable(value = "username") String username) {
        return userService.findByNickname(username);
    }
}