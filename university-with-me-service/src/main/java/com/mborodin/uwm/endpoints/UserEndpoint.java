package com.mborodin.uwm.endpoints;

import com.mborodin.uwm.api.AddStudentsToGroupApi;
import com.mborodin.uwm.api.UpdateUserApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.clients.KeycloakServiceClient;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.getRole;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserService userService;
    private final KeycloakServiceClient keycloakServiceClient;

    @PreAuthorize("hasAnyRole('ROLE_SERVICE')")
    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "userId") final String userId) {
        return new ResponseEntity<>(userService.findUserById(userId), OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT')")
    @GetMapping(value = "/teachers")
    public ResponseEntity<?> getTeachersByUniversityId() {
        List<UserApi> users;

        if (ROLE_STUDENT.equals(getRole())) {
            users = userService.findTeachers();
        } else {
            users = userService.findTeachersByUniversityId();
        }

        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_ADMIN')")
    @GetMapping(value = "/students")
    public ResponseEntity<?> getStudents() {
        List<UserApi> users;

        if (ROLE_TEACHER.equals(getRole())) {
            users = userService.findStudent(UserContextHolder.getId());
        } else {
            keycloakServiceClient.getUsersByRole("ROLE_STUDENT", Integer.MAX_VALUE);
            users = userService.findStudentsByUniversityId();
        }

        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_SERVICE')")
    @GetMapping(value = "/students/groupId/{groupId}")
    public ResponseEntity<?> getStudentsByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findStudentsByGroupId(groupId);
        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_SERVICE')")
    @GetMapping(value = "/teachers/groupId/{groupId}")
    public ResponseEntity<?> getTeachersByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findTeachersByGroupId(groupId);
        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/group/studentId/{studentId}")
    public ResponseEntity<?> removeStudentFromGroup(@PathVariable(value = "studentId") final String studentId) {
        return new ResponseEntity<>(userService.removeStudentFromGroup(studentId), OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/students/without/group")
    public ResponseEntity<?> getStudentsWithoutGroup() {
        final List<UserApi> users = userService.findUsersWithoutGroup();
        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/group")
    public ResponseEntity<?> addStudentToGroup(@RequestBody final AddStudentsToGroupApi addStudentsToGroupApi) {
        userService.addStudentToGroup(addStudentsToGroupApi.getStudentsIds(), addStudentsToGroupApi.getGroupId());
        return getStudentsByGroupId(addStudentsToGroupApi.getGroupId());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @DeleteMapping()
    public ResponseEntity<?> deleteUser() {
        final String userId = UserContextHolder.getId();
        userService.deleteUser(userId);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @PutMapping()
    public UserApi updateUser(@RequestBody final UpdateUserApi updateUserApi) {
        return userService.updateUser(updateUserApi);
    }
}