package com.educationapp.server.services;

import static com.educationapp.server.enums.Role.ADMIN;
import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;

import java.util.Objects;

import com.educationapp.server.enums.Role;
import com.educationapp.server.exception.ResourceNotFoundException;
import com.educationapp.server.models.api.RegisterApi;
import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.api.admin.AddUniversityApi;
import com.educationapp.server.models.persistence.*;
import com.educationapp.server.repositories.*;
import com.educationapp.server.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final DepartmentRepository departmentRepository;
    private final InstituteRepository instituteRepository;
    private final ScienceDegreeRepository scienceDegreeRepository;
    private final FileService fileService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserDB user = userRepository.findByUsername(username)
                                          .orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserDetailsImpl(user);
    }

    public Long save(final RegisterApi user) {
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
            return studentRepository.save(studentToCreate).getId();

        } else if (user.getRole() == TEACHER.getId()) {
            TeacherDB teacherToCreate = TeacherDB.builder()
                                                 .id(created.getId())
                                                 .departmentId(user.getDepartmentId())
                                                 .scienceDegreeId(user.getScienceDegreeId())
                                                 .build();
            return teacherRepository.save(teacherToCreate).getId();
        }
        return null;
    }

    public UserApi save(final AddUniversityApi addUniversityApi, final UniversityDb university) {
        final UserDB toCreate = UserDB.builder()
                                      .username(addUniversityApi.getUsername())
                                      .password(addUniversityApi.getPassword())
                                      .isAdmin(true)
                                      .role(ADMIN.getId())
                                      .universityId(university.getId())
                                      .build();

        userRepository.save(toCreate);

        return UserApi.builder()
                      .id(toCreate.getId())
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
                                                .id(userDb.getId())
                                                .firstName(userDb.getFirstName())
                                                .lastName(userDb.getLastName())
                                                .surname(userDb.getSurname())
                                                .username(userDb.getUsername())
                                                .password(userDb.getPassword())
                                                .phone(userDb.getPhone())
                                                .email(userDb.getEmail())
                                                .role(userDb.getRole())
                                                .universityId(userDb.getUniversityId())
                                                .isAdmin(userDb.getIsAdmin())
                                                .avatar(fileService.loadAvatar(userDb.getId()));
        Long departmentId = null;

        if (Role.STUDENT.getId() == userDb.getRole()) {
            StudentDB studentDb = studentRepository.findById(userDb.getId())
                                                   .orElse(new StudentDB());
            StudyGroupDb studyGroup = studyGroupRepository.findById(studentDb.getStudyGroupId())
                                                          .orElse(new StudyGroupDb());
            departmentId = studyGroup.getDepartmentId();

            userApi = userApi.studentId(studentDb.getStudentId())
                             .studyGroupName(studyGroup.getName())
                             .studyGroupId(studyGroup.getId());
        } else if (Role.TEACHER.getId() == userDb.getRole()) {
            TeacherDB teacherDB = teacherRepository.findById(userDb.getId())
                                                   .orElse(new TeacherDB());
            ScienceDegreeDb scienceDegree = scienceDegreeRepository.findById(teacherDB.getScienceDegreeId())
                                                                   .orElse(new ScienceDegreeDb());
            departmentId = teacherDB.getDepartmentId();

            userApi = userApi.scienceDegreeName(scienceDegree.getName());
        }
        if (Role.STUDENT.getId() == userDb.getRole() || Role.TEACHER.getId() == userDb.getRole()) {
            DepartmentDb department = departmentRepository.findById(Objects.requireNonNull(departmentId))
                                                          .orElse(new DepartmentDb());
            InstituteDb institute = instituteRepository.findById(department.getInstitute().getId())
                                                       .orElse(new InstituteDb());

            return userApi.departmentName(department.getName())
                          .instituteName(institute.getName())
                          .build();
        } else {
            return userApi.build();
        }
    }

    public UserApi mapTeacherDataDbToUserApi(final TeacherDataDb teacher) {
        final DepartmentDb department = departmentRepository.findById(teacher.getDepartmentId())
                                                            .orElse(new DepartmentDb());

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
                                                                .map(ScienceDegreeDb::getName)
                                                                .orElse(null))
                      .departmentName(department.getName())
                      .instituteName(instituteRepository.findById(department.getInstitute().getId())
                                                        .map(InstituteDb::getName)
                                                        .orElse(null))
                      //TODO
//                      .isAdmin(teacher.getIsAdmin())
                      .build();
    }

    public UserApi mapStudentDataDbToUserApi(final StudentDataDb student) {
        StudyGroupDb studyGroup;
        if (student.getStudyGroupId() != null) {
            studyGroup = studyGroupRepository.findById(student.getStudyGroupId())
                                             .orElse(new StudyGroupDb());
        } else {
            studyGroup = new StudyGroupDb();
        }

        DepartmentDb department;
        if (studyGroup.getDepartmentId() != null) {
            department = departmentRepository.findById(studyGroup.getDepartmentId())
                                             .orElse(new DepartmentDb());
        } else {
            department = new DepartmentDb();
        }

        return UserApi.builder()
                      .id(student.getId())
                      .firstName(student.getFirstName())
                      .lastName(student.getLastName())
                      .surname(student.getSurname())
                      .username(student.getUsername())
                      .password(student.getPassword())
                      .phone(student.getPhone())
                      .email(student.getEmail())
                      .role(student.getRole())
                      .universityId(student.getUniversityId())
                      .studentId(student.getStudentId())
                      .studyGroupName(studyGroup.getName())
                      .studyGroupId(studyGroup.getId())
                      .departmentName(department.getName())
                      .instituteName(department.getInstitute() != null
                                             ? instituteRepository.findById(department.getId())
                                                                  .map(InstituteDb::getName)
                                                                  .orElse(null)
                                             : null)
                      //TODO
//                      .isAdmin(student.getIsAdmin())
                      .build();
    }
}
