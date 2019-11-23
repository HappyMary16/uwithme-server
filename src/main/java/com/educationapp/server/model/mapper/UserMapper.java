package com.educationapp.server.model.mapper;

import com.educationapp.server.model.Role;
import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.persistence.UserDB;

public class UserMapper {

    public static User userDbToUser(final UserDB userDB) {
        return new User(userDB.getId(),
                        userDB.getFirstName(),
                        userDB.getLastName(),
                        userDB.getNickname(),
                        userDB.getPassword(),
                        userDB.getPassword(),
                        userDB.getEmail(),
                        userDB.getPhone(),
                        Role.USER);
    }

    public static UserDB userToUserDB(final User user) {
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