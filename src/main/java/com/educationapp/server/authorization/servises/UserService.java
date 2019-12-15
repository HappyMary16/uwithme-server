package com.educationapp.server.authorization.servises;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;
import static com.educationapp.server.exception.ExceptionsMessages.USERNAME_NOT_FOUND;

import java.util.Objects;

import com.educationapp.server.authorization.models.RegisterApi;
import com.educationapp.server.authorization.models.UserApi;
import com.educationapp.server.enums.Role;
import com.educationapp.server.exception.ResourceNotFoundException;
import com.educationapp.server.university.data.models.Department;
import com.educationapp.server.university.data.models.Institute;
import com.educationapp.server.university.data.models.ScienceDegree;
import com.educationapp.server.university.data.models.StudyGroup;
import com.educationapp.server.university.data.repositories.DepartmentRepository;
import com.educationapp.server.university.data.repositories.InstituteRepository;
import com.educationapp.server.university.data.repositories.ScienceDegreeRepository;
import com.educationapp.server.university.data.repositories.StudyGroupRepository;
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
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudyGroupRepository studyGroupRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    InstituteRepository instituteRepository;

    @Autowired
    ScienceDegreeRepository scienceDegreeRepository;

    public User save(final RegisterApi user) {
        UserDB toCreate = UserDB.builder()
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .surname(user.getSurname())
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .phone(user.getPhone())
                                .email(user.getEmail())
                                .role(user.getRole())
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

    public UserApi findByUserName(final String username) {
        UserDB userDb = userRepository.findByUsername(username).orElse(null);

        if (Objects.isNull(userDb)) {
            throw new ResourceNotFoundException("User", "user name", username);
        }

        UserApi userApi = UserApi.builder()
                                 .firstName(userDb.getFirstName())
                                 .lastName(userDb.getLastName())
                                 .surname(userDb.getSurname())
                                 .username(userDb.getUsername())
                                 .password(userDb.getPassword())
                                 .phone(userDb.getPhone())
                                 .email(userDb.getEmail())
                                 .role(userDb.getRole())
                                 .build();

        Long departmentId = null;

        if (Role.STUDENT.getId() == userDb.getRole()) {
            StudentDB studentDb = studentRepository.findById(userDb.getId()).orElse(null);
            StudyGroup studyGroup = studyGroupRepository.findById(studentDb.getStudyGroupId()).orElse(null);
            departmentId = studyGroup.getDepartmentId();

            userApi = userApi.toBuilder()
                             .studentId(studentDb.getStudentId())
                             .studyGroupName(studyGroup.getName())
                             .build();
        } else if (Role.TEACHER.getId() == userDb.getRole()) {
            TeacherDB teacherDB = teacherRepository.findById(userDb.getId()).orElse(null);
            ScienceDegree scienceDegree = scienceDegreeRepository.findById(teacherDB.getScienceDegreeId()).orElse(null);
            departmentId = teacherDB.getDepartmentId();

            userApi = userApi.toBuilder()
                             .scienceDegreeName(scienceDegree.getName())
                             .build();
        }
        Department department = departmentRepository.findById(departmentId).orElse(null);
        Institute institute = instituteRepository.findById(department.getInstituteId()).orElse(null);

        return userApi.toBuilder()
                      .departmentName(department.getName())
                      .instituteName(institute.getName())
                      .build();
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
