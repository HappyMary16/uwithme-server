package com.educationapp.server.endpoints;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.api.admin.AddStudentsToGroupApi;
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

    @GetMapping(value = "/teachers")
    public ResponseEntity<?> getTeachersByUniversityId() {
        final List<UserApi> users = userService.findTeachersByUniversityId();

        return new ResponseEntity<>(users, OK);
    }

    @GetMapping
    public ResponseEntity<?> getUserFriends() {
        final UserApi user = UserContextHolder.getUser();
        List<UserApi> users;

        if (user.getRole().equals(STUDENT.getId())) {
            users = userService.findTeachersByGroupId(user.getStudyGroupId());
        } else if (user.getRole().equals(TEACHER.getId())) {
            users = userService.findStudentByTeacherId(user.getId());
        } else {
            return new ResponseEntity<>(METHOD_NOT_ALLOWED);
        }

        return new ResponseEntity<>(users, OK);
    }

    @GetMapping(value = "/students/groupId/{groupId}")
    public ResponseEntity<?> getStudentsByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = userService.findStudentsByGroupId(groupId);
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
}