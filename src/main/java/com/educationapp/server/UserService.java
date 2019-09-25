package com.educationapp.server;

import com.educationapp.server.dao.UserDao;
import com.educationapp.server.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User selectById(final Long id) {
        return userDao.selectById(id);
    }

    public User createUser(final User user) {
        return userDao.createEntity(user);
    }

    public List<User> selectAll() {
        return userDao.getAllUsers();
    }
}
