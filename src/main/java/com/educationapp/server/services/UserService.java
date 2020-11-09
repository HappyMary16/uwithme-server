package com.educationapp.server.services;

import static com.educationapp.server.enums.Role.ADMIN;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.educationapp.server.clients.KeycloakServiceClient;
import com.educationapp.server.exception.UserNotFoundException;
import com.educationapp.server.models.KeycloakUser;
import com.educationapp.server.models.api.RegisterApi;
import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.persistence.*;
import com.educationapp.server.repositories.DepartmentRepository;
import com.educationapp.server.repositories.StudyGroupDataRepository;
import com.educationapp.server.repositories.UniversityRepository;
import com.educationapp.server.repositories.UserRepository;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudyGroupDataRepository studyGroupRepository;
    private final DepartmentRepository departmentRepository;
    private final UniversityRepository universityRepository;
    private final KeycloakServiceClient keycloakServiceClient;

    @Transactional
    public String save(final RegisterApi user) {
        final String userId = UserContextHolder.getUser().getId();

        final DepartmentDb departmentProxy = departmentRepository.getProxyByIdIfExist(user.getDepartmentId());
        final StudyGroupDataDb groupProxy = studyGroupRepository.getProxyByIdIfExist(user.getGroupId());

        final UserDb toCreate = UserDb.builder().id(userId)
                                      .role(user.getRole())
                                      .isAdmin(false)
                                      .universityId(user.getUniversityId())
                                      .department(departmentProxy)
                                      .studyGroup(groupProxy)
                                      .build();

        if (user.getRole().equals(ADMIN.getId())) {
            final UniversityDb universityToCreate = new UniversityDb(user.getUniversityName());
            final Long universityId = universityRepository.save(universityToCreate).getId();

            toCreate.setUniversityId(universityId);
        }

        return userRepository.save(toCreate).getId();
    }

    @Transactional
    public UserApi getUserApi() {
        final UserApi user = UserContextHolder.getUser();
        final UserApi.UserApiBuilder userBuilder = user.toBuilder();
        final UserDb userDb = userRepository.findById(user.getId())
                                            .orElseThrow(() -> new UserNotFoundException(user.getEmail()));

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

    @Transactional
    public List<UserApi> findTeachersByUniversityId() {
        final Long universityId = UserContextHolder.getUser().getUniversityId();
        final List<UserApi> teachers = userRepository.findAllByRoleAndUniversityId(2, universityId)
                                                     .stream()
                                                     .map(this::mapToUserApi)
                                                     .collect(Collectors.toList());

        log.debug("Teachers by university id: {}. Result: {}", universityId, teachers);

        return teachers;
    }

    @Transactional
    public List<UserApi> findStudentsByGroupId(final Long groupId) {
        return userRepository.findAllByStudyGroupId(groupId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    @Transactional
    public UserApi removeStudentFromGroup(final String studentId) {
        final UserDb student = userRepository.findById(studentId)
                                             .orElseThrow(UserNotFoundException::new)
                                             .toBuilder()
                                             .studyGroup(null)
                                             .build();
        final UserDb updatedUser = userRepository.save(student);
        return mapToUserApi(updatedUser);
    }

    public void addStudentToGroup(final List<String> studentIds, final Long groupId) {
        final List<UserDb> students = studentIds
                .stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .peek(s -> {
                    final StudyGroupDataDb groupProxy = studyGroupRepository.getProxyByIdIfExist(groupId);
                    s.setStudyGroup(groupProxy);
                })
                .collect(Collectors.toList());

        userRepository.saveAll(students);
    }

    @Transactional
    public List<UserApi> findUsersWithoutGroup() {
        final Long universityId = UserContextHolder.getUser().getUniversityId();
        return userRepository.findAllByStudyGroupIsNullAndRoleAndUniversityId(1, universityId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    @Transactional
    public List<UserApi> findStudentByTeacherId(final String teacherId) {
        return userRepository.findStudentsByTeacherId(teacherId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    @Transactional
    public List<UserApi> findTeachersByGroupId(final Long groupId) {
        return userRepository.findTeachersByGroupId(groupId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    private UserApi mapToUserApi(final UserDb userDb) {
        final KeycloakUser user = keycloakServiceClient.getUserById(userDb.getId());

        return mapUserDbToUserApi(userDb).toBuilder()
                                         .firstName(user.getGivenName())
                                         .lastName(user.getMiddleName())
                                         .surname(user.getFamilyName())
                                         .email(user.getEmail())
                                         .build();
    }

    public static UserApi mapUserDbToUserApi(final UserDb userDb) {
        StudyGroupDataDb studyGroup = userDb.getStudyGroup();
        DepartmentDb department = userDb.getDepartment();

        if (department == null) {
            if (studyGroup == null) {
                studyGroup = new StudyGroupDataDb();
                department = new DepartmentDb();
            } else {
                department = studyGroup.getDepartment();
            }
        }

        if (studyGroup == null) {
            studyGroup = new StudyGroupDataDb();
        }

        final InstituteDb institute = department.getInstitute();
        String instituteName = "";
        if (institute != null) {
            instituteName = institute.getName();
        }

        return UserApi.builder()
                      .id(userDb.getId())
                      .role(userDb.getRole())
                      .universityId(userDb.getUniversityId())
                      .studyGroupName(studyGroup.getName())
                      .studyGroupId(studyGroup.getId())
                      .departmentName(department.getName())
                      .instituteName(instituteName)
                      .isAdmin(userDb.getIsAdmin())
                      .build();
    }
}
