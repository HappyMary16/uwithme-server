package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.getGroupId;
import static com.mborodin.uwm.security.UserContextHolder.hasRole;

import java.util.List;

import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.AddStudentsToGroupApi;
import com.mborodin.uwm.api.UpdateUserApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_SERVICE')")
    @GetMapping(value = "/{userId}")
    public UserApi getUserById(@PathVariable(value = "userId") final String userId) {
        return userService.findUserById(userId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER', 'ROLE_STUDENT')")
    @GetMapping(value = "/teachers")
    public List<UserApi> getRelativeTeachers() {
        List<UserApi> users;

        if (hasRole(ROLE_STUDENT)) {
            final Long groupId = UserContextHolder.getGroupId();
            users = userService.findTeachersByGroupId(groupId);
        } else {
            final Long departmentId = UserContextHolder.getUserDb().getDepartmentId();
            users = userService.findTeachersByDepartmentId(departmentId);
        }

        return users;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER', 'ROLE_STUDENT')")
    @GetMapping(value = "/students")
    public List<UserApi> getStudents() {
        List<UserApi> users;

        if (hasRole(ROLE_TEACHER)) {
            users = userService.findStudentsByTeacherId(UserContextHolder.getId());
        } else {
            users = userService.findStudentsByGroupId(getGroupId());
        }

        return users;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<UserApi> getUsersByRole(@QueryParam("role") @NonNull Role role) {
        return userService.findAllUsersByRole(role);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_SERVICE')")
    @GetMapping(value = "/students/groupId/{groupId}")
    public List<UserApi> getStudentsByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findStudentsByGroupId(groupId);
        return users;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SERVICE')")
    @GetMapping(value = "/teachers/groupId/{groupId}")
    public List<UserApi> getTeachersByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findTeachersByGroupId(groupId);
        return users;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/group/studentId/{studentId}")
    public UserApi removeStudentFromGroup(@PathVariable(value = "studentId") final String studentId) {
        return userService.removeStudentFromGroup(studentId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/students/without/group")
    public List<UserApi> getStudentsWithoutGroup() {
        final List<UserApi> users = userService.findUsersWithoutGroup();
        return users;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/group")
    public List<UserApi> addStudentToGroup(@RequestBody final AddStudentsToGroupApi addStudentsToGroupApi) {
        userService.addStudentToGroup(addStudentsToGroupApi.getStudentsIds(), addStudentsToGroupApi.getGroupId());
        return getStudentsByGroupId(addStudentsToGroupApi.getGroupId());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @DeleteMapping()
    public void deleteUser() {
        final String userId = UserContextHolder.getId();
        userService.deleteUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @PutMapping()
    public UserApi updateUser(@RequestBody final UpdateUserApi updateUserApi) {
        return userService.updateUser(updateUserApi);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{userId}/roles/{role}")
    public String assignRole(@PathVariable(value = "userId") final String userId,
                             @PathVariable(value = "role") final Role role) {
        return userService.assignRole(userId, role);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{userId}/roles/{role}")
    public String unAssignRole(@PathVariable(value = "userId") final String userId,
                               @PathVariable(value = "role") final Role role) {
        return userService.unAssignRole(userId, role);
    }
}