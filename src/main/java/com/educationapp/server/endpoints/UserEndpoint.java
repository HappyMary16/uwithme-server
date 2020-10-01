package com.educationapp.server.endpoints;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.api.admin.AddStudentsToGroupApi;
import com.educationapp.server.models.persistence.StudentDB;
import com.educationapp.server.repositories.StudentDataRepository;
import com.educationapp.server.repositories.StudentRepository;
import com.educationapp.server.repositories.TeacherDataRepository;
import com.educationapp.server.security.UserContextHolder;
import com.educationapp.server.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    private final UserService userService;
    private final TeacherDataRepository teacherDataRepository;
    private final StudentDataRepository studentDataRepository;
    private final StudentRepository studentRepository;

    @GetMapping(value = "/teachers")
    public ResponseEntity<?> getTeachersByUniversityId() {
        final Long universityId = UserContextHolder.getUser().getUniversityId();
        final List<UserApi> users = teacherDataRepository.findAllByUniversityId(universityId)
                                                         .stream()
                                                         .map(userService::mapTeacherDataDbToUserApi)
                                                         .collect(Collectors.toList());

        return new ResponseEntity<>(users, OK);
    }

    @GetMapping
    public ResponseEntity<?> getUserFriends() {
        final UserApi user = UserContextHolder.getUser();
        List<UserApi> users;

        if (user.getRole().equals(STUDENT.getId())) {
            users = teacherDataRepository.findAllByGroupId(user.getStudyGroupId())
                                         .stream()
                                         .map(userService::mapTeacherDataDbToUserApi)
                                         .collect(Collectors.toList());
        } else if (user.getRole().equals(TEACHER.getId())) {
            users = studentDataRepository.findAllByTeacherId(user.getId())
                                         .stream()
                                         .map(userService::mapStudentDataDbToUserApi)
                                         .collect(Collectors.toList());
        } else {
            return new ResponseEntity<>(METHOD_NOT_ALLOWED);
        }

        return new ResponseEntity<>(users, OK);
    }

    @GetMapping(value = "/students/groupId/{groupId}")
    public ResponseEntity<?> getStudentsByGroupId(@PathVariable(value = "groupId") final Long groupId) {
        final List<UserApi> users = studentDataRepository.findAllByStudyGroupId(groupId)
                                                         .stream()
                                                         .map(userService::mapStudentDataDbToUserApi)
                                                         .collect(Collectors.toList());
        return new ResponseEntity<>(users, OK);
    }

    @DeleteMapping(value = "/group/studentId/{studentId}")
    public ResponseEntity<?> removeStudentFromGroup(@PathVariable(value = "studentId") final Long studentId) {
        final StudentDB student = studentRepository.findById(studentId)
                                                   .orElseThrow(NotFoundException::new)
                                                   .toBuilder()
                                                   .studyGroupId(null)
                                                   .build();

        return new ResponseEntity<>(studentRepository.save(student), OK);
    }

    @GetMapping(value = "/students/without/group")
    public ResponseEntity<?> getStudentsWithoutGroup() {
        final Long universityId = UserContextHolder.getUser().getUniversityId();
        final List<UserApi> users = studentDataRepository.findAllByStudyGroupIdAndUniversityId(null, universityId)
                                                         .stream()
                                                         .map(userService::mapStudentDataDbToUserApi)
                                                         .collect(Collectors.toList());

        return new ResponseEntity<>(users, OK);
    }

    @PutMapping(value = "/group")
    public ResponseEntity<?> addStudentToGroup(@RequestBody final AddStudentsToGroupApi addStudentsToGroupApi) {
        final List<StudentDB> students = addStudentsToGroupApi
                .getStudentsIds()
                .stream()
                .map(id -> studentRepository.findById(id).orElseThrow(NotFoundException::new))
                .peek(s -> s.setStudyGroupId(addStudentsToGroupApi.getGroupId()))
                .collect(Collectors.toList());

        studentRepository.saveAll(students);

        return getStudentsByGroupId(addStudentsToGroupApi.getGroupId());
    }
}