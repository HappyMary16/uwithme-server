package com.mborodin.uwm.services;

import static com.mborodin.uwm.api.enums.Role.ROLE_ADMIN;
import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.security.UserContextHolder.getId;
import static com.mborodin.uwm.security.UserContextHolder.getKeycloakUser;
import static com.mborodin.uwm.security.UserContextHolder.getLanguages;
import static com.mborodin.uwm.security.UserContextHolder.getUserDb;
import static com.mborodin.uwm.security.UserContextHolder.hasRole;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.UwmUserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.exceptions.LastAdminCannotBeDeleted;
import com.mborodin.uwm.api.exceptions.UserNotFoundException;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.model.persistence.TenantDb;
import com.mborodin.uwm.model.persistence.UserDb;
import com.mborodin.uwm.repositories.UniversityRepository;
import com.mborodin.uwm.repositories.UserRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
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
    private final UsersResource usersResource;
    private final RolesResource rolesResource;
    private final KeycloakUserService keycloakUserService;

    public boolean userExists() {
        final String userId = getId();
        return userRepository.existsById(userId);
    }

    public String save(final RegisterApi user) {
        final String userId = getId();

        final UserDb toCreate = UserDb.builder().id(userId)
                                      .universityId(user.getUniversityId())
                                      .departmentId(user.getDepartmentId())
                                      .groupId(user.getGroupId())
                                      .build();

        if (Objects.equals(user.getRole(), ROLE_ADMIN)) {
            final TenantDb universityToCreate = new TenantDb(user.getUniversityName());
            final Long universityId = universityRepository.save(universityToCreate).getId();

            toCreate.setUniversityId(universityId);
        }

        unAssignAllRoles(userId);
        final RoleRepresentation roleToAssign = rolesResource.get(user.getRole().name()).toRepresentation();
        usersResource.get(userId).roles().realmLevel().add(List.of(roleToAssign));
        getKeycloakUser().getRealmRoles().add(user.getRole().name());

        return userRepository.save(toCreate).getId();
    }

    public String updateUser(final RegisterApi user) {
        final String userId = getId();

        final UserDb toCreate = UserDb.builder()
                                      .id(userId)
                                      .universityId(user.getUniversityId())
                                      .departmentId(user.getDepartmentId())
                                      .groupId(user.getGroupId())
                                      .build();

        return userRepository.save(toCreate).getId();
    }

    public String assignRole(final String userId, final Role role) {
        final var user = userRepository.findById(userId)
                                       .orElseThrow(() -> new UserNotFoundException(getLanguages()));

        final RoleRepresentation roleToAssign = rolesResource.get(role.name()).toRepresentation();
        usersResource.get(userId).roles().realmLevel().add(List.of(roleToAssign));

        return userRepository.save(user).getId();
    }

    public String unAssignRole(final String userId, final Role role) {
        final var user = userRepository.findById(userId)
                                       .orElseThrow(() -> new UserNotFoundException(getLanguages()));

        final RoleRepresentation roleToUnAssign = rolesResource.get(role.name()).toRepresentation();
        usersResource.get(userId).roles().realmLevel().remove(List.of(roleToUnAssign));

        return userRepository.save(user).getId();
    }

    public UserApi getUserApi() {
        final UserRepresentation keycloakUser = getKeycloakUser();
        final UserDb userDb = userRepository.findById(keycloakUser.getId())
                                            .orElseThrow(() -> new UserNotFoundException(getLanguages(),
                                                                                         keycloakUser.getEmail()));

        return mapToUserApi(userDb, keycloakUser);
    }

    public List<UserApi> findAllUsersByRole(final Role role) {
        final Long universityId = UserContextHolder.getUniversityId();

        final var keycloakUsers = rolesResource.get(role.name())
                                               .getRoleUserMembers(0, Integer.MAX_VALUE)
                                               .stream()
                                               .collect(Collectors.toMap(UserRepresentation::getId,
                                                                         Function.identity()));

        final List<UserApi> users = userRepository.findAllByUniversityIdAndIdIn(universityId, keycloakUsers.keySet())
                                                  .stream()
                                                  .map(user -> mapToUserApi(user, keycloakUsers.get(user.getId())))
                                                  .collect(Collectors.toList());

        log.debug("Users by university id: {} and role: {}. Result: {}", universityId, role, users);

        return users;
    }

    public List<UserApi> findStudentsByGroupId(final Long groupId) {
        return userRepository.findAllByGroupId(groupId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    public List<UserApi> findUsersWithoutGroup() {
        final Long universityId = UserContextHolder.getUniversityId();

        final var keycloakUsersByIds = rolesResource.get(ROLE_STUDENT.name())
                                                    .getRoleUserMembers(0, Integer.MAX_VALUE)
                                                    .stream()
                                                    .collect(Collectors.toMap(UserRepresentation::getId,
                                                                              Function.identity()));

        final var usersByIds = userRepository.findAllByGroupIdIsNullAndUniversityId(universityId)
                                             .stream()
                                             .collect(Collectors.toMap(UserDb::getId,
                                                                       Function.identity()));

        return keycloakUsersByIds.keySet()
                                 .stream()
                                 .filter(usersByIds.keySet()::contains)
                                 .map(userId -> mapToUserApi(usersByIds.get(userId), keycloakUsersByIds.get(userId)))
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
        return userRepository.findById(userId)
                             .map(this::mapToUserApi)
                             .stream()
                             .findFirst()
                             .orElseThrow(() -> new UserNotFoundException(getLanguages()));
    }

    public void deleteUser(final String userId) {
        if (hasRole(ROLE_ADMIN)
                && findAllUsersByRole(ROLE_ADMIN).size() == 1) {
            throw new LastAdminCannotBeDeleted(getLanguages());
        }

        unAssignAllRoles(userId);
        userRepository.deleteById(userId);
    }

    public UserApi updateUser(final UwmUserApi uwmUserApi) {
        log.info("Update user {} {} to {}", getKeycloakUser(), getUserDb(), uwmUserApi);

        final UserDb toUpdate = userRepository.findById(uwmUserApi.getUserId())
                                              .map(userDb -> userDb.toBuilder()
                                                                   .departmentId(uwmUserApi.getDepartmentId())
                                                                   .groupId(uwmUserApi.getGroupId())
                                                                   .universityId(uwmUserApi.getUniversityId())
                                                                   .build())
                                              .orElseThrow(() -> new UserNotFoundException(getId()));

        log.info("User id: {}. User to update {}", getId(), toUpdate);

        userRepository.save(toUpdate);

        return getUserApi();
    }

    private UserApi mapToUserApi(final UserDb userDb) {
        final UserRepresentation keycloakUser = keycloakUserService.getUser(userDb.getId());
        return mapToUserApi(userDb, keycloakUser);
    }

    private UserApi mapToUserApi(final UserDb userDb, final UserRepresentation keycloakUser) {
        final Optional<GroupApi> studyGroup = Optional.ofNullable(userDb.getGroupId())
                                                      .flatMap(groupService::getById);

        final String departmentId = studyGroup.map(GroupApi::getDepartmentId)
                                              .map(String::valueOf)
                                              .orElse(userDb.getDepartmentId());

        final Optional<DepartmentApi> department = Optional.ofNullable(departmentId)
                                                           .map(departmentService::getById);

        final Optional<InstituteApi> institute = department.map(DepartmentApi::getInstituteId)
                                                           .map(instituteService::getById);

        return UserApi.builder()
                      .id(userDb.getId())
                      .universityId(userDb.getUniversityId())
                      .firstName(keycloakUser.getFirstName())
                      .surname(keycloakUser.getLastName())
                      .email(keycloakUser.getEmail())
                      .institute(institute.orElse(null))
                      .department(department.orElse(null))
                      .group(studyGroup.orElse(null))
                      .roles(keycloakUserService.getUserRoles(userDb.getId()))
                      .build();
    }

    private void unAssignAllRoles(final String userId) {
        final var rolesToAnAssign = Arrays.stream(Role.values())
                                          .map(Role::name)
                                          .map(rolesResource::get)
                                          .map(RoleResource::toRepresentation)
                                          .collect(Collectors.toList());

        usersResource.get(userId).roles().realmLevel().remove(rolesToAnAssign);
    }
}
