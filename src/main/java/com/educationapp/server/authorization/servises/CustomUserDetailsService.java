package com.educationapp.server.authorization.servises;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;
import static com.educationapp.server.exception.ExceptionsMessages.USERNAME_NOT_FOUND;

import com.educationapp.server.authorization.models.RegisterApi;
import com.educationapp.server.users.model.domain.User;
import com.educationapp.server.users.model.mapper.UserMapper;
import com.educationapp.server.users.model.persistence.StudentDB;
import com.educationapp.server.users.model.persistence.TeacherDB;
import com.educationapp.server.users.model.persistence.UserDB;
import com.educationapp.server.users.repositories.StudentRepository;
import com.educationapp.server.users.repositories.TeacherRepository;
import com.educationapp.server.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    public User save(final RegisterApi user) {
        UserDB toCreate = UserDB.builder()
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .surname(user.getSurname())
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .phone(user.getPhone())
                                .email(user.getEmail())
                                .build();
        UserDB created = userRepository.save(toCreate);

        if (user.getRole() == STUDENT.getId()) {
            StudentDB studentToCreate = StudentDB.builder()
                                                 .id(created.getId())
                                                 .studentId(user.getStudentId())
                                                 .studyGroupId(user.getStudyGroupId())
                                                 .build();
            studentRepository.save(studentToCreate);

        } else if (user.getRole() == TEACHER.getId()) {
            TeacherDB teacherToCreate = TeacherDB.builder()
                                                 .id(created.getId())
                                                 .departmentId(user.getDepartmentId())
                                                 .scienceDegreeId(user.getScienceDegreeId())
                                                 .build();
            teacherRepository.save(teacherToCreate);
        }

        return UserMapper.userDbToUser(created);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserDB user =
                userRepository.findByUsername(username)
                              .orElseThrow(() -> new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND,
                                                                                             username)));
        return UserMapper.userDbToUser(user);
    }
}
