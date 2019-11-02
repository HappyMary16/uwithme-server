package com.educationapp.server.model.mapper;

import com.educationapp.server.model.domain.Student;
import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.persistence.StudentDB;
import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentMapper {

    @Autowired
    private UserRepository userRepository;

    public Student studentDBToStudent(final StudentDB studentDB) {

        UserDB userDB = userRepository.findById(studentDB.getId()).orElse(null);

        if (userDB == null) {
            throw new RuntimeException();
        }

        return Student.builder()
                      .id(studentDB.getId())
                      .firstName(userDB.getFirstName())
                      .lastName(userDB.getLastName())
                      .nickname(userDB.getNickname())
                      .password(userDB.getPassword())
                      .email(userDB.getEmail())
                      .phone(userDB.getPhone())
                      .build();
    }

    public UserDB userToUserDB(final User userDB) {
        return UserDB.builder()
                     .id(userDB.getId())
                     .firstName(userDB.getFirstName())
                     .lastName(userDB.getLastName())
                     .nickname(userDB.getNickname())
                     .password(userDB.getPassword())
                     .email(userDB.getEmail())
                     .phone(userDB.getPhone())
                     .build();
    }
}
