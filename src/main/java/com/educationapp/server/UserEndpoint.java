package com.educationapp.server;

import com.educationapp.server.models.UserDB;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/users")
public class UserEndpoint {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDB getUser(@PathVariable(value = "id") Long id) {
        return userRepository.findById(id).orElse(new UserDB());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public UserDB saveUser(@RequestBody UserDB user) {
        return userRepository.save(user);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<UserDB> getAll() {
        return (List<UserDB>) userRepository.findAll();
    }
}