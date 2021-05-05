package com.educationapp.server.endpoints;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;
import static com.educationapp.server.security.UserContextHolder.getRole;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.educationapp.api.AddStudentsToGroupApi;
import com.educationapp.api.UpdateUserApi;
import com.educationapp.api.UserApi;
import com.educationapp.server.security.UserContextHolder;
import com.educationapp.server.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
    @GetMapping(value = "/teachers")
    public ResponseEntity<?> getTeachersByUniversityId() {
        List<UserApi> users;

        if (STUDENT.equals(getRole())) {
            users = userService.findTeachers();
        } else {
            users = userService.findTeachersByUniversityId();
        }

        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @GetMapping(value = "/students")
    public ResponseEntity<?> getStudents() {
        List<UserApi> users;

        if (TEACHER.equals(getRole())) {
            users = userService.findStudent(UserContextHolder.getId());
        } else {
            users = userService.findStudentsByUniversityId();
        }

        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER', 'ROLE_SERVICE')")
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/group/studentId/{studentId}")
    public ResponseEntity<?> removeStudentFromGroup(@PathVariable(value = "studentId") final String studentId) {
        return new ResponseEntity<>(userService.removeStudentFromGroup(studentId), OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/students/without/group")
    public ResponseEntity<?> getStudentsWithoutGroup() {
        final List<UserApi> users = userService.findUsersWithoutGroup();
        return new ResponseEntity<>(users, OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/group")
    public ResponseEntity<?> addStudentToGroup(@RequestBody final AddStudentsToGroupApi addStudentsToGroupApi) {
        userService.addStudentToGroup(addStudentsToGroupApi.getStudentsIds(), addStudentsToGroupApi.getGroupId());
        return getStudentsByGroupId(addStudentsToGroupApi.getGroupId());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER', 'STUDENT')")
    @DeleteMapping()
    public ResponseEntity<?> deleteUser() {
        final String userId = UserContextHolder.getId();
        userService.deleteUser(userId);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER', 'STUDENT')")
    @PostMapping()
    public UserApi updateUser(@RequestBody final UpdateUserApi updateUserApi) {
        return userService.updateUser(updateUserApi);
    }
}