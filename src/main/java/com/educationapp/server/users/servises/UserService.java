package com.educationapp.server.users.servises;

import static com.educationapp.server.common.enums.Role.ADMIN;
import static com.educationapp.server.common.enums.Role.STUDENT;
import static com.educationapp.server.common.enums.Role.TEACHER;
import static com.educationapp.server.common.exception.ExceptionsMessages.USERNAME_NOT_FOUND;

import java.util.Objects;

import com.educationapp.server.common.api.RegisterApi;
import com.educationapp.server.common.api.UserApi;
import com.educationapp.server.common.api.admin.AddUniversityApi;
import com.educationapp.server.common.enums.Role;
import com.educationapp.server.common.exception.ResourceNotFoundException;
import com.educationapp.server.university.models.Department;
import com.educationapp.server.university.models.Institute;
import com.educationapp.server.university.models.ScienceDegree;
import com.educationapp.server.university.models.StudyGroup;
import com.educationapp.server.university.models.University;
import com.educationapp.server.university.repositories.DepartmentRepository;
import com.educationapp.server.university.repositories.InstituteRepository;
import com.educationapp.server.university.repositories.ScienceDegreeRepository;
import com.educationapp.server.university.repositories.StudyGroupRepository;
import com.educationapp.server.users.model.User;
import com.educationapp.server.users.model.persistence.StudentDB;
import com.educationapp.server.users.model.persistence.StudentDataDb;
import com.educationapp.server.users.model.persistence.TeacherDB;
import com.educationapp.server.users.model.persistence.TeacherDataDb;
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

    public void save(final RegisterApi user) {
        UserDB toCreate = UserDB.builder()
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .surname(user.getSurname())
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .phone(user.getPhone())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .isAdmin(false)
                                .universityId(user.getUniversityId())
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
    }

    public UserApi save(final AddUniversityApi addUniversityApi, final University university) {
        UserDB toCreate = UserDB.builder()
                                .username(addUniversityApi.getUsername())
                                .password(addUniversityApi.getPassword())
                                .isAdmin(true)
                                .role(ADMIN.getId())
                                .universityId(university.getId())
                                .build();

        userRepository.save(toCreate);

        return UserApi.builder()
                      .username(addUniversityApi.getUsername())
                      .password(addUniversityApi.getPassword())
                      .isAdmin(true)
                      .role(ADMIN.getId())
                      .universityId(university.getId())
                      .build();
    }

    public UserApi findByUserName(final String username) {
        UserDB userDb = userRepository.findByUsername(username).orElse(null);

        if (Objects.isNull(userDb)) {
            throw new ResourceNotFoundException("User", "user name", username);
        }

        UserApi.UserApiBuilder userApi = UserApi.builder()
                                                .firstName(userDb.getFirstName())
                                                .lastName(userDb.getLastName())
                                                .surname(userDb.getSurname())
                                                .username(userDb.getUsername())
                                                .password(userDb.getPassword())
                                                .phone(userDb.getPhone())
                                                .email(userDb.getEmail())
                                                .role(userDb.getRole())
                                                .universityId(userDb.getUniversityId())
                                                .isAdmin(userDb.getIsAdmin());
        Long departmentId = null;

        if (Role.STUDENT.getId() == userDb.getRole()) {
            StudentDB studentDb = studentRepository.findById(userDb.getId())
                                                   .orElse(new StudentDB());
            StudyGroup studyGroup = studyGroupRepository.findById(studentDb.getStudyGroupId())
                                                        .orElse(new StudyGroup());
            departmentId = studyGroup.getDepartmentId();

            userApi = userApi.studentId(studentDb.getStudentId())
                             .studyGroupName(studyGroup.getName())
                             .studyGroupId(studyGroup.getId());
        } else if (Role.TEACHER.getId() == userDb.getRole()) {
            TeacherDB teacherDB = teacherRepository.findById(userDb.getId())
                                                   .orElse(new TeacherDB());
            ScienceDegree scienceDegree = scienceDegreeRepository.findById(teacherDB.getScienceDegreeId())
                                                                 .orElse(new ScienceDegree());
            departmentId = teacherDB.getDepartmentId();

            userApi = userApi.scienceDegreeName(scienceDegree.getName());
        }
        if (Role.STUDENT.getId() == userDb.getRole() || Role.TEACHER.getId() == userDb.getRole()) {
            Department department = departmentRepository.findById(Objects.requireNonNull(departmentId))
                                                        .orElse(new Department());
            Institute institute = instituteRepository.findById(department.getInstituteId())
                                                     .orElse(new Institute());

            return userApi.departmentName(department.getName())
                          .instituteName(institute.getName())
                          .build();
        } else {
            return userApi.build();
        }
    }

    public UserApi mapTeacherDataDbToUserApi(final TeacherDataDb teacher) {
        final Department department = departmentRepository.findById(teacher.getDepartmentId())
                                                          .orElse(new Department());

        return UserApi.builder()
                      .id(teacher.getId())
                      .firstName(teacher.getFirstName())
                      .lastName(teacher.getLastName())
                      .surname(teacher.getSurname())
                      .username(teacher.getUsername())
                      .password(teacher.getPassword())
                      .phone(teacher.getPhone())
                      .email(teacher.getEmail())
                      .role(teacher.getRole())
                      .universityId(teacher.getUniversityId())
                      .scienceDegreeName(scienceDegreeRepository.findById(teacher.getScienceDegreeId())
                                                                .map(ScienceDegree::getName)
                                                                .orElse(null))
                      .departmentName(department.getName())
                      .instituteName(instituteRepository.findById(department.getInstituteId())
                                                        .map(Institute::getName)
                                                        .orElse(null))
                      //TODO
//                      .isAdmin(teacher.getIsAdmin())
                      .build();
    }

    public UserApi mapStudentDataDbToUserApi(final StudentDataDb teacher) {
        final StudyGroup studyGroup = studyGroupRepository.findById(teacher.getStudyGroupId())
                                                          .orElse(new StudyGroup());
        final Department department = departmentRepository.findById(studyGroup.getDepartmentId())
                                                          .orElse(new Department());

        return UserApi.builder()
                      .id(teacher.getId())
                      .firstName(teacher.getFirstName())
                      .lastName(teacher.getLastName())
                      .surname(teacher.getSurname())
                      .username(teacher.getUsername())
                      .password(teacher.getPassword())
                      .phone(teacher.getPhone())
                      .email(teacher.getEmail())
                      .role(teacher.getRole())
                      .universityId(teacher.getUniversityId())
                      .studentId(teacher.getStudentId())
                      .studyGroupName(studyGroup.getName())
                      .studyGroupId(studyGroup.getId())
                      .departmentName(department.getName())
                      .instituteName(instituteRepository.findById(department.getInstituteId())
                                                        .map(Institute::getName)
                                                        .orElse(null))
                      //TODO
//                      .isAdmin(teacher.getIsAdmin())
                      .build();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserDB user =
                userRepository.findByUsername(username)
                              .orElseThrow(() -> new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND,
                                                                                             username)));
        return userDbToUser(user);
    }

    private static User userDbToUser(final UserDB userDB) {
        return new User(userDB.getId(),
                        userDB.getFirstName(),
                        userDB.getLastName(),
                        userDB.getSurname(),
                        userDB.getUsername(),
                        userDB.getPassword(),
                        userDB.getPhone(),
                        userDB.getEmail(),
                        Role.getById(userDB.getRole()),
                        userDB.getIsAdmin(),
                        userDB.getUniversityId(),
                        null);
    }
}
