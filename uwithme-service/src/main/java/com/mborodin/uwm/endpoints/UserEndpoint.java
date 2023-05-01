package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.api.enums.Role.ROLE_ADMIN;
import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.UwmUserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.exceptions.EmptyFieldsException;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserService userService;

    @GetMapping(value = "/{userId}")
    public UserApi getUserById(@PathVariable(value = "userId") final String userId) {
        return userService.findUserById(userId);
    }

    @GetMapping
    public List<UserApi> getUsersByFilter(@QueryParam("role") Role role,
                                          @QueryParam("groupId") final Long groupId,
                                          @QueryParam("hasGroup") final Boolean hasGroup) {
        if (Objects.nonNull(role)
                && Objects.equals(role, ROLE_ADMIN)
                && !UserContextHolder.hasRole(ROLE_ADMIN)) {
            throw new AccessDeniedException("");
        }

        if (Objects.nonNull(role)) {
            return userService.findAllUsersByRole(role);
        }

        if (Objects.nonNull(groupId)) {
            return userService.findStudentsByGroupId(groupId);
        }

        if (Objects.nonNull(hasGroup) && !hasGroup) {
            return userService.findUsersWithoutGroup();
        }

        throw new UnsupportedOperationException();
    }

    @Secured("ROLE_SERVICE")
    @GetMapping(value = "/teachers/groupId/{groupId}")
    public List<UserApi> getTeachersByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findTeachersByGroupId(groupId);
        return users;
    }

    @PostMapping
    public UserApi saveUser(@RequestBody final RegisterApi registerApi) {
        if (userService.userExists()) {
            validateDepartments(registerApi);
            userService.updateUser(registerApi);
        } else {
            validateRegistrationApiValid(registerApi);
            userService.save(registerApi);
        }

        return userService.getUserApi();
    }

    @PutMapping()
    public UserApi updateUser(@RequestBody final UwmUserApi uwmUser) {
        if (!UserContextHolder.hasRole(ROLE_ADMIN)
                && !Objects.equals(uwmUser.getUserId(), UserContextHolder.getId())) {
            throw new ForbiddenException();
        }

        return userService.updateUser(uwmUser);
    }

    @DeleteMapping
    public void deleteUser() {
        final String userId = UserContextHolder.getId();
        userService.deleteUser(userId);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{userId}/roles/{role}")
    public String assignRole(@PathVariable(value = "userId") final String userId,
                             @PathVariable(value = "role") final Role role) {
        return userService.assignRole(userId, role);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{userId}/roles/{role}")
    public String unAssignRole(@PathVariable(value = "userId") final String userId,
                               @PathVariable(value = "role") final Role role) {
        return userService.unAssignRole(userId, role);
    }

    private void validateRegistrationApiValid(final RegisterApi registerApi) {
        final Role role = registerApi.getRole();
        if (role == null) {
            throw new EmptyFieldsException(getLanguages());
        }

        if (Objects.equals(role, ROLE_ADMIN) && StringUtils.isEmpty(registerApi.getUniversityName())) {
            throw new EmptyFieldsException(getLanguages());
        }

        if (!Objects.equals(role, ROLE_ADMIN)) {
            validateDepartments(registerApi);
        }
    }

    private void validateDepartments(final RegisterApi registerApi) {
        boolean result = registerApi.getUniversityId() != null;
        result &= registerApi.getDepartmentId() != null;

        if (!result) {
            throw new EmptyFieldsException(getLanguages());
        }
    }
}