package com.educationapp.server.model.mapper;

import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.persistence.UserDB;

public class UserMapper {

    public User userDbToUser(final UserDB userDB) {
        return User.builder()
                   .id(userDB.getId())
                   .firstName(userDB.getFirstName())
                   .lastName(userDB.getLastName())
                   .nickname(userDB.getNickname())
                   .password(userDB.getPassword())
                   .email(userDB.getEmail())
                   .phone(userDB.getPhone())
                   .build();
    }

    public UserDB userToUserDB(final User user) {
        return UserDB.builder()
                     .id(user.getId())
                     .firstName(user.getFirstName())
                     .lastName(user.getLastName())
                     .nickname(user.getNickname())
                     .password(user.getPassword())
                     .email(user.getEmail())
                     .phone(user.getPhone())
                     .build();
    }
}
