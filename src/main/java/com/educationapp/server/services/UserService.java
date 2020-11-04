package com.educationapp.server.services;

import static com.educationapp.server.enums.Role.ADMIN;

import java.util.Objects;

import com.educationapp.server.exception.UserNotFound;
import com.educationapp.server.models.api.RegisterApi;
import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.persistence.*;
import com.educationapp.server.repositories.*;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final SimpleUserRepository simpleUserRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final DepartmentRepository departmentRepository;
    private final InstituteRepository instituteRepository;
    private final UniversityRepository universityRepository;

    public String save(final RegisterApi user) {
        final String userId = UserContextHolder.getUser().getId();

        final SimpleUserDb toCreate = SimpleUserDb.builder()
                                                  .id(userId)
                                                  .role(user.getRole())
                                                  .isAdmin(false)
                                                  .universityId(user.getUniversityId())
                                                  .departmentId(user.getDepartmentId())
                                                  .groupId(user.getGroupId())
                                                  .build();

        if (user.getRole().equals(ADMIN.getId())) {
            final UniversityDb universityToCreate = new UniversityDb(user.getUniversityName());
            final Long universityId = universityRepository.save(universityToCreate).getId();

            toCreate.setUniversityId(universityId);
        }

        return simpleUserRepository.save(toCreate).getId();
    }

    public UserApi getUserApi() {
        final UserApi user = UserContextHolder.getUser();
        final UserApi.UserApiBuilder userBuilder = user.toBuilder();
        final UserDB userDb = userRepository.findById(user.getId())
                                            .orElseThrow(() -> new UserNotFound(user.getEmail()));

        if (!userDb.getRole().equals(ADMIN.getId())) {
            final DepartmentDb department = Objects.nonNull(userDb.getStudyGroup())
                    ? userDb.getStudyGroup().getDepartment()
                    : userDb.getDepartment();
            userBuilder.departmentName(department.getName())
                       .instituteName(department.getInstitute().getName());
        }

        if (Objects.nonNull(userDb.getStudyGroup())) {
            final StudyGroupDataDb studyGroup = userDb.getStudyGroup();
            userBuilder.studyGroupId(studyGroup.getId())
                       .studyGroupName(studyGroup.getName());
        }

        return userBuilder.build();
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
                      .phone(teacher.getPhone())
                      .email(teacher.getEmail())
                      .role(teacher.getRole())
                      .universityId(teacher.getUniversityId())
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
                      .phone(student.getPhone())
                      .email(student.getEmail())
                      .role(student.getRole())
                      .universityId(student.getUniversityId())
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
