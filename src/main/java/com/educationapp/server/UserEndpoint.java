package com.educationapp.server;

import com.educationapp.server.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path(value = "/university")
@RestController
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @GET
    @Path(value = "/user/{id:[0-9]+}")
    public User getUser(final @PathParam("id") Long id) {
        return userService.selectById(id);
    }

    @GET
    @Path(value = "/users")
    public List<User> getAllUser() {
        return userService.selectAll();
    }

    @POST
    @Path("/user")
    public User createEquipment(final User user) {
        return userService.createUser(user);
    }
}
