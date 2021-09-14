package com.mborodin.uwm.endpoints;

import com.mborodin.uwm.api.AddStudentsToGroupApi;
import com.mborodin.uwm.api.UpdateUserApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mborodin.uwm.api.enums.Role.*;
import static com.mborodin.uwm.security.UserContextHolder.getRole;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_SERVICE')")
    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "userId") final String userId) {
        return new ResponseEntity<>(userService.findUserById(userId), OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT')")
    @GetMapping(value = "/teachers")
    public List<UserApi> getTeachersByUniversityId() {
        List<UserApi> users;

        if (ROLE_STUDENT.equals(getRole())) {
            users = userService.findTeachers();
        } else {
            users = userService.findAllUsersByRole(ROLE_TEACHER);
        }

        return users;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER', 'ROLE_ADMIN')")
    @GetMapping(value = "/students")
    public List<UserApi> getStudents() {
        List<UserApi> users;

        if (ROLE_TEACHER.equals(getRole())) {
            users = userService.findStudent(UserContextHolder.getId());
        } else {
            users = userService.findAllUsersByRole(ROLE_STUDENT);
        }

        return users;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/admins")
    public List<UserApi> getAdmins() {
        List<UserApi> users;

        if (ROLE_TEACHER.equals(getRole())) {
            users = userService.findStudent(UserContextHolder.getId());
        } else {
            users = userService.findAllUsersByRole(ROLE_ADMIN);
        }

        return users;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_SERVICE')")
    @GetMapping(value = "/students/groupId/{groupId}")
    public ResponseEntity<?> getStudentsByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findStudentsByGroupId(groupId);
        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SERVICE')")
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @DeleteMapping()
    public ResponseEntity<?> deleteUser() {
        final String userId = UserContextHolder.getId();
        userService.deleteUser(userId);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @PutMapping()
    public UserApi updateUser(@RequestBody final UpdateUserApi updateUserApi) {
        return userService.updateUser(updateUserApi);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{userId}")
    public void assignRole(@PathVariable(value = "userId") final String  userId, @RequestBody final Role role) {
        userService.assignRole(userId, role);
    }
}