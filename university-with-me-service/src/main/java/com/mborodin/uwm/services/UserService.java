package com.mborodin.uwm.services;

import static com.mborodin.uwm.api.enums.Role.ROLE_ADMIN;
import static com.mborodin.uwm.security.UserContextHolder.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.KeycloakUserApi;
import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UpdateUserApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.exceptions.LastAdminCannotBeDeleted;
import com.mborodin.uwm.api.exceptions.UserNotFoundException;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.clients.KeycloakServiceClient;
import com.mborodin.uwm.model.persistence.UniversityDb;
import com.mborodin.uwm.model.persistence.UserDb;
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
    private final GroupService groupService;
    private final DepartmentService departmentService;
    private final UniversityRepository universityRepository;
    private final InstituteService instituteService;
    private final KeycloakServiceClient keycloakServiceClient;

    public String save(final RegisterApi user) {
        final String userId = getId();

        final UserDb toCreate = UserDb.builder().id(userId)
                                      .universityId(user.getUniversityId())
                                      .departmentId(user.getDepartmentId())
                                      .groupId(user.getGroupId())
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

        keycloakServiceClient.assignRole(userId, role);
        return userRepository.save(user).getId();
    }

    public String unAssignRole(final String userId, final Role role) {
        final var user = userRepository.findById(userId)
                                       .orElseThrow(() -> new UserNotFoundException(getLanguages()));

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
        final List<UserApi> teachers = userRepository.findAllByUniversityId(universityId)
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
            users = userRepository.findAllByUniversityId(universityId)
                                  .stream()
                                  .map(user -> mapToUserApi(user, keycloakUsers.get(user.getId())))
                                  .collect(Collectors.toList());
        } else {
            users = userRepository.findAllByUniversityId(universityId)
                                  .stream()
                                  .map(user -> mapToUserApi(user, keycloakUsers.get(user.getId())))
                                  .collect(Collectors.toList());
        }
        log.debug("Users by university id: {} and role: {}. Result: {}", universityId, role, users);

        return users;
    }

    public List<UserApi> findStudentsByGroupId(final Long groupId) {
        return userRepository.findAllByGroupId(groupId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    public UserApi removeStudentFromGroup(final String studentId) {
        final UserDb student = userRepository.findById(studentId)
                                             .orElseThrow(() -> new UserNotFoundException(getLanguages()))
                                             .toBuilder()
                                             .groupId(null)
                                             .build();
        final UserDb updatedUser = userRepository.save(student);
        return mapToUserApi(updatedUser);
    }

    public void addStudentToGroup(final List<String> studentIds, final Long groupId) {
        final List<UserDb> students = studentIds
                .stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .peek(s -> s.setGroupId(groupId))
                .collect(Collectors.toList());

        userRepository.saveAll(students);
    }

    public List<UserApi> findUsersWithoutGroup() {
        final Long universityId = UserContextHolder.getUniversityId();
        //TODO get only students
        return userRepository.findAllByGroupIdIsNullAndUniversityId(universityId)
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
        return userRepository.findAllByDepartmentId(departmentId)
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
        if (hasRole(ROLE_ADMIN)
                && userRepository.findAllByUniversityId(getUniversityId()).size() == 1) {
            throw new LastAdminCannotBeDeleted(getLanguages());
        }

        Arrays.stream(Role.values())
              .forEach(role -> keycloakServiceClient.unAssignRole(userId, role));
        userRepository.deleteById(userId);
    }

    public UserApi updateUser(final UpdateUserApi updateUserApi) {
        log.info("Update user {} {} to {}", getKeycloakUser(), getUserDb(), updateUserApi);

        final UserDb toUpdate = userRepository.findById(getId())
                                              .map(userDb -> userDb.toBuilder()
                                                                   .departmentId(updateUserApi.getDepartmentId())
                                                                   .groupId(updateUserApi.getGroupId())
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
        final Optional<GroupApi> studyGroup = Optional.ofNullable(userDb.getGroupId())
                                                      .map(groupService::getById);

        final Long departmentId = studyGroup.map(GroupApi::getId)
                                            .orElse(userDb.getDepartmentId());

        final Optional<DepartmentApi> department = Optional.ofNullable(departmentId)
                                                           .map(departmentService::getById);

        final Optional<InstituteApi> institute = department.map(DepartmentApi::getInstituteId)
                                                           .map(instituteService::getById);

        return UserApi.builder()
                      .id(userDb.getId())
                      .universityId(userDb.getUniversityId())
                      .studyGroupName(studyGroup.map(GroupApi::getName).orElse(null))
                      .studyGroupId(studyGroup.map(GroupApi::getId).orElse(null))
                      .departmentName(department.map(DepartmentApi::getName).orElse(null))
                      .instituteName(institute.map(InstituteApi::getName).orElse(null))
                      .firstName(keycloakUser.getFirstName())
                      .middleName(keycloakUser.getMiddleName())
                      .surname(keycloakUser.getLastName())
                      .email(keycloakUser.getEmail())
                      .institute(institute.orElse(null))
                      .department(department.orElse(null))
                      .group(studyGroup.orElse(null))
                      .build();
    }
}
