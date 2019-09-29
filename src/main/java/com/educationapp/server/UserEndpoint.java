package com.educationapp.server;

import com.educationapp.server.models.User;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@RestController
public class UserEndpoint {

    private final UserRepository userRepository;

    public UserEndpoint(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Path(value = "/user/{id:[0-9]+}")
    public User getUser(final @PathParam("id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getAllUser() {
        return (List<User>) userRepository.findAll();
    }

    @POST
    @Path(value = "/user")
    public User createEquipment(final User user) {
        return userRepository.save(user);
    }
}
