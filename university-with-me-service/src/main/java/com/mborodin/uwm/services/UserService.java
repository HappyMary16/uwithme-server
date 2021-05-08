package com.mborodin.uwm.services;

import static com.mborodin.uwm.enums.Role.ADMIN;
import static com.mborodin.uwm.security.UserContextHolder.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.KeycloakUserApi;
import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UpdateUserApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.clients.KeycloakServiceClient;
import com.mborodin.uwm.enums.Role;
import com.mborodin.uwm.exception.LastAdminCannotBeDeleted;
import com.mborodin.uwm.exception.UserNotFoundException;
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

        keycloakServiceClient.addRole(userId, Role.getById(user.getRole()).name());
        return userRepository.save(toCreate).getId();
    }

    public UserApi getUserApi() {
        final KeycloakUserApi keycloakUser = getKeycloakUser();
        final UserDb userDb = userRepository.findById(keycloakUser.getId())
                                            .orElseThrow(() -> new UserNotFoundException(keycloakUser.getEmail()));

        return mapToUserApi(userDb, keycloakUser);
    }

    public List<UserApi> findTeachersByUniversityId() {
        final Long universityId = UserContextHolder.getUniversityId();
        final List<UserApi> teachers = userRepository.findAllByRoleAndUniversityId(2, universityId)
                                                     .stream()
                                                     .map(this::mapToUserApi)
                                                     .collect(Collectors.toList());

        log.debug("Teachers by university id: {}. Result: {}", universityId, teachers);

        return teachers;
    }

    public List<UserApi> findStudentsByUniversityId() {
        final Long universityId = UserContextHolder.getUniversityId();
        final List<UserApi> students = userRepository.findAllByRoleAndUniversityId(1, universityId)
                                                     .stream()
                                                     .map(this::mapToUserApi)
                                                     .collect(Collectors.toList());

        log.debug("Students by university id: {}. Result: {}", universityId, students);

        return students;
    }

    public List<UserApi> findStudentsByGroupId(final Long groupId) {
        return userRepository.findAllByStudyGroupId(groupId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

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

    public List<UserApi> findUsersWithoutGroup() {
        final Long universityId = UserContextHolder.getUniversityId();
        return userRepository.findAllByStudyGroupIsNullAndRoleAndUniversityId(1, universityId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    public List<UserApi> findStudent(final String teacherId) {
        return userRepository.findStudentsByTeacherId(teacherId)
                             .stream()
                             .map(this::mapToUserApi)
                             .collect(Collectors.toList());
    }

    public List<UserApi> findTeachers() {
        final Long groupId = UserContextHolder.getGroupId();
        return findTeachersByGroupId(groupId);
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
                             .map(this::mapUserDbToUserApi)
                             .stream()
                             .peek(user -> log.info("User with id {}: {}", userId, user))
                             .findFirst()
                             .orElseThrow(UserNotFoundException::new);
    }

    public void deleteUser(final String userId) {
        if (!Objects.equals(getRole(), ADMIN)) {
            userRepository.deleteById(userId);
            return;
        }

        final long adminsNumber = userRepository.findAllByRoleAndUniversityId(ADMIN.getId(), getUniversityId())
                                                .stream()
                                                .map(UserDb::getId)
                                                .filter(Predicate.not(getId()::equals))
                                                .count();

        if (adminsNumber == 0) {
            throw new LastAdminCannotBeDeleted();
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
        final KeycloakUserApi keycloakUser = keycloakServiceClient.getUserById(userDb.getId());
        return mapToUserApi(userDb, keycloakUser);
    }

    private UserApi mapToUserApi(final UserDb userDb, final KeycloakUserApi keycloakUser) {
        return mapUserDbToUserApi(userDb).toBuilder()
                                         .firstName(keycloakUser.getFirstName())
                                         .lastName(keycloakUser.getMiddleName())
                                         .surname(keycloakUser.getLastName())
                                         .email(keycloakUser.getEmail())
                                         .build();
    }

    private UserApi mapUserDbToUserApi(final UserDb userDb) {
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
