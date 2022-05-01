package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;

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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserService userService;

    @Secured("ROLE_SERVICE")
    @GetMapping(value = "/{userId}")
    public UserApi getUserById(@PathVariable(value = "userId") final String userId) {
        return userService.findUserById(userId);
    }

    @Secured({"ROLE_TEACHER", "ROLE_STUDENT"})
    @GetMapping(value = "/teachers")
    public List<UserApi> getRelativeTeachers() {
        return userService.findAllUsersByRole(ROLE_TEACHER);
    }

    @Secured({"ROLE_TEACHER", "ROLE_STUDENT"})
    @GetMapping(value = "/students")
    public List<UserApi> getStudents() {
        return userService.findAllUsersByRole(ROLE_STUDENT);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<UserApi> getUsersByRole(@QueryParam("role") @NonNull Role role) {
        return userService.findAllUsersByRole(role);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_SERVICE"})
    @GetMapping(value = "/students/groupId/{groupId}")
    public List<UserApi> getStudentsByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findStudentsByGroupId(groupId);
        return users;
    }

    @Secured("ROLE_SERVICE")
    @GetMapping(value = "/teachers/groupId/{groupId}")
    public List<UserApi> getTeachersByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findTeachersByGroupId(groupId);
        return users;
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/group/studentId/{studentId}")
    public UserApi removeStudentFromGroup(@PathVariable(value = "studentId") final String studentId) {
        return userService.removeStudentFromGroup(studentId);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/students/without/group")
    public List<UserApi> getStudentsWithoutGroup() {
        return userService.findUsersWithoutGroup();
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/group")
    public List<UserApi> addStudentToGroup(@RequestBody final AddStudentsToGroupApi addStudentsToGroupApi) {
        userService.addStudentToGroup(addStudentsToGroupApi.getStudentsIds(), addStudentsToGroupApi.getGroupId());
        return getStudentsByGroupId(addStudentsToGroupApi.getGroupId());
    }

    @Secured({"ROLE_TEACHER", "ROLE_ADMIN", "ROLE_STUDENT"})
    @DeleteMapping()
    public void deleteUser() {
        final String userId = UserContextHolder.getId();
        userService.deleteUser(userId);
    }

    @Secured({"ROLE_TEACHER", "ROLE_ADMIN", "ROLE_STUDENT"})
    @PutMapping()
    public UserApi updateUser(@RequestBody final UpdateUserApi updateUserApi) {
        return userService.updateUser(updateUserApi);
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
}