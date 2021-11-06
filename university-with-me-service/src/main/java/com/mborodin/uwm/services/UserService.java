package com.mborodin.uwm.services;

import static com.mborodin.uwm.api.enums.Role.ROLE_ADMIN;
import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.KeycloakUserApi;
import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UpdateUserApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.exceptions.LastAdminCannotBeDeleted;
import com.mborodin.uwm.api.exceptions.UserNotFoundException;
import com.mborodin.uwm.clients.KeycloakServiceClient;
import com.mborodin.uwm.models.persistence.*;
import com.mborodin.uwm.repositories.DepartmentRepository;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import com.mborodin.uwm.repositories.UniversityRepository;
import com.mborodin.uwm.repositories.UserRepository;
import com.mborodin.uwm.security.UserContextHolder;
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

    public String save(final RegisterApi user) {
        final String userId = getId();

        final DepartmentDb departmentProxy = departmentRepository.getProxyByIdIfExist(user.getDepartmentId());
        final StudyGroupDataDb groupProxy = studyGroupRepository.getProxyByIdIfExist(user.getGroupId());

        final UserDb toCreate = UserDb.builder().id(userId)
                                      .role(user.getRole())
                                      .isAdmin(Objects.equals(user.getRole(), ROLE_ADMIN))
                                      .universityId(user.getUniversityId())
                                      .department(departmentProxy)
                                      .studyGroup(groupProxy)
                                      .oldRole(1)
                                      .build();

        if (Objects.equals(user.getRole(), ROLE_ADMIN)) {
            final UniversityDb universityToCreate = new UniversityDb(user.getUniversityName());
            final Long universityId = universityRepository.save(universityToCreate).getId();

            toCreate.setUniversityId(universityId);
        }

        keycloakServiceClient.assignRole(userId, user.getRole());
        return userRepository.save(toCreate).getId();
    }

    public String assignRole(final String userId, final Role role) {
        final var user = userRepository.findById(userId)
                                       .orElseThrow(() -> new UserNotFoundException(getLanguages()));

        if (Objects.equals(role, ROLE_ADMIN)) {
            user.setIsAdmin(true);
        } else {
            user.setRole(role);
        }

        keycloakServiceClient.assignRole(userId, role);
        return userRepository.save(user).getId();
    }

    public String unAssignRole(final String userId, final Role role) {
        final var user = userRepository.findById(userId)
                                       .orElseThrow(() -> new UserNotFoundException(getLanguages()));

        if (Objects.equals(role, ROLE_ADMIN)) {
            user.setIsAdmin(false);
        } else {
            user.setRole(null);
        }

        keycloakServiceClient.unAssignRole(userId, role);
        return userRepository.save(user).getId();
    }

    public UserApi getUserApi() {
        final KeycloakUserApi keycloakUser = getKeycloakUser();
        final UserDb userDb = userRepository.findById(keycloakUser.getId())
                                            .orElseThrow(() -> new UserNotFoundException(getLanguages(),
                                                                                         keycloakUser.getEmail()));

        return mapToUserApi(userDb, keycloakUser);
    }

    public List<UserApi> findTeachersByUniversityId() {
        final Long universityId = UserContextHolder.getUniversityId();
        final List<UserApi> teachers = userRepository.findAllByRoleAndUniversityId(ROLE_TEACHER, universityId)
                                                     .stream()
                                                     .map(this::mapToUserApi)
                                                     .collect(Collectors.toList());

        log.debug("Teachers by university id: {}. Result: {}", universityId, teachers);

        return teachers;
    }

    public List<UserApi> findAllUsersByRole(final Role role) {
        final Long universityId = UserContextHolder.getUniversityId();
        final var keycloakUsers = keycloakServiceClient.getUsersByRole(role, Integer.MAX_VALUE)
                                                       .stream()
                                                       .collect(Collectors.toMap(KeycloakUserApi::getId,
                                                                                 Function.identity()));

        final List<UserApi> users;

        if (Objects.equals(role, ROLE_ADMIN)) {
            users = userRepository.findAllByIsAdminAndUniversityId(true, universityId)
                                  .stream()
                                  .map(user -> mapToUserApi(user, keycloakUsers.get(user.getId())))
                                  .collect(Collectors.toList());
        } else {
            users = userRepository.findAllByRoleAndUniversityId(role, universityId)
                                  .stream()
                                  .map(user -> mapToUserApi(user, keycloakUsers.get(user.getId())))
                                  .collect(Collectors.toList());
        }
        log.debug("Users by university id: {} and role: {}. Result: {}", universityId, role, users);

        return users;
    }

    public List<UserApi> findStudentsByGroupId(final Long groupId) {
        return userRepository.findAllByStudyGroupId(groupId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    public UserApi removeStudentFromGroup(final String studentId) {
        final UserDb student = userRepository.findById(studentId)
                                             .orElseThrow(() -> new UserNotFoundException(getLanguages()))
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

    public List<UserApi> findUsersWithoutGroup() {
        final Long universityId = UserContextHolder.getUniversityId();
        return userRepository.findAllByStudyGroupIsNullAndRoleAndUniversityId(ROLE_STUDENT, universityId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    public List<UserApi> findStudentsByTeacherId(final String teacherId) {
        return userRepository.findStudentsByTeacherId(teacherId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    public List<UserApi> findTeachersByDepartmentId(final Long departmentId) {
        return userRepository.findAllByDepartment_Id(departmentId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    public List<UserApi> findTeachersByGroupId(final Long groupId) {
        return userRepository.findTeachersByGroupId(groupId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    /**
     * Returns user info without info in keycloak service
     *
     * @param userId for getting user
     * @return user info without info in keycloak service
     */
    public UserApi findUserById(final String userId) {
        //TODO: doesn't work
        return userRepository.findById(userId)
                             .map(this::mapToUserApi)
                             .stream()
                             .peek(user -> log.info("User with id {}: {}", userId, user))
                             .findFirst()
                             .orElseThrow(() -> new UserNotFoundException(getLanguages()));
    }

    public void deleteUser(final String userId) {
        if (!Objects.equals(getRole(), ROLE_ADMIN)) {
            userRepository.deleteById(userId);
            keycloakServiceClient.unAssignRole(userId, getRole());
            return;
        }

        final long adminsNumber = userRepository.findAllByRoleAndUniversityId(ROLE_ADMIN, getUniversityId())
                                                .stream()
                                                .map(UserDb::getId)
                                                .filter(Predicate.not(getId()::equals))
                                                .count();

        if (adminsNumber == 0) {
            throw new LastAdminCannotBeDeleted(getLanguages());
        }

        Optional.ofNullable(getUniversityId())
                .ifPresent(universityRepository::deleteById);
    }

    public UserApi updateUser(final UpdateUserApi updateUserApi) {
        log.info("Update user {} {} to {}", getKeycloakUser(), getUserDb(), updateUserApi);

        final DepartmentDb departmentProxy = departmentRepository.getProxyByIdIfExist(updateUserApi.getDepartmentId());
        final StudyGroupDataDb groupProxy = studyGroupRepository.getProxyByIdIfExist(updateUserApi.getGroupId());
        final UserDb toUpdate = userRepository.findById(getId())
                                              .map(userDb -> userDb.toBuilder()
                                                                   .department(departmentProxy)
                                                                   .studyGroup(groupProxy)
                                                                   .universityId(updateUserApi.getUniversityId())
                                                                   .build())
                                              .orElseThrow(() -> new UserNotFoundException(getId()));

        log.info("User id: {}. User to update {}", getId(), toUpdate);

        userRepository.save(toUpdate);

        return getUserApi();
    }

    private UserApi mapToUserApi(final UserDb userDb) {
        final KeycloakUserApi keycloakUser = keycloakServiceClient.getUser(userDb.getId());
        return mapToUserApi(userDb, keycloakUser);
    }

    private UserApi mapToUserApi(final UserDb userDb, final KeycloakUserApi keycloakUser) {
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
                      .firstName(keycloakUser.getFirstName())
                      .middleName(keycloakUser.getMiddleName())
                      .surname(keycloakUser.getLastName())
                      .email(keycloakUser.getEmail())
                      .build();
    }
}
