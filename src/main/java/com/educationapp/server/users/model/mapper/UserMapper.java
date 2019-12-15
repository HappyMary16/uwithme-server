package com.educationapp.server.users.model.mapper;

import com.educationapp.server.enums.Role;
import com.educationapp.server.users.model.domain.User;
import com.educationapp.server.users.model.persistence.UserDB;

public class UserMapper {

    public static User userDbToUser(final UserDB userDB) {
        return new User(userDB.getId(),
                        userDB.getFirstName(),
                        userDB.getLastName(),
                        userDB.getSurname(),
                        userDB.getUsername(),
                        userDB.getPassword(),
                        userDB.getPhone(),
                        userDB.getEmail(),
                        Role.getById(userDB.getRole()),
                        null);
    }

    public static UserDB userToUserDB(final User user) {
        return UserDB.builder()
                     .id(user.getId())
                     .firstName(user.getFirstName())
                     .lastName(user.getLastName())
                     .surname(user.getSurname())
                     .username(user.getUsername())
                     .password(user.getPassword())
                     .email(user.getEmail())
                     .phone(user.getPhone())
                     .build();
    }
}
