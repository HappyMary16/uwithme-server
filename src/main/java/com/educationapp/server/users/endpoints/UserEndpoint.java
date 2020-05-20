package com.educationapp.server.users.endpoints;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import com.educationapp.server.common.api.UserApi;
import com.educationapp.server.common.api.admin.AddStudentsToGroupApi;
import com.educationapp.server.users.model.persistence.StudentDB;
import com.educationapp.server.users.model.persistence.UserDB;
import com.educationapp.server.users.repositories.StudentDataRepository;
import com.educationapp.server.users.repositories.StudentRepository;
import com.educationapp.server.users.repositories.TeacherDataRepository;
import com.educationapp.server.users.repositories.UserRepository;
import com.educationapp.server.users.servises.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserEndpoint {

    private UserService userService;
    private UserRepository userRepository;
    private TeacherDataRepository teacherDataRepository;
    private StudentDataRepository studentDataRepository;
    private StudentRepository studentRepository;

    @GetMapping(value = "/{id}")
    public UserDB getUser(@PathVariable(value = "id") Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping(value = "/{username}")
    public UserDB getUser(@PathVariable(value = "username") String username) {
        return userRepository.findByUsername(username).get();
    }

    @GetMapping(value = "/teachers/{universityId}")
    public List<UserApi> getTeachersByUniversityId(@PathVariable(value = "universityId") Long universityId) {
        return teacherDataRepository.findAllByUniversityId(universityId)
                                    .stream()
                                    .map(userService::mapTeacherDataDbToUserApi)
                                    .collect(Collectors.toList());
    }

    @GetMapping(value = "/teachers/{groupId}/group")
    public List<UserApi> getTeachersByGroupId(@PathVariable(value = "groupId") Long groupId) {
        return teacherDataRepository.findAllByGroupId(groupId)
                                    .stream()
                                    .map(userService::mapTeacherDataDbToUserApi)
                                    .collect(Collectors.toList());
    }

    @GetMapping(value = "/students/{teacherId}/teacherId")
    public List<UserApi> getStudentsByTeacherId(@PathVariable(value = "teacherId") Long teacherId) {
        return studentDataRepository.findAllByTeacherId(teacherId)
                                    .stream()
                                    .map(userService::mapStudentDataDbToUserApi)
                                    .collect(Collectors.toList());
    }

    @GetMapping(value = "/students/{groupId}/group")
    public ResponseEntity<?> getStudentsByGroupId(@PathVariable(value = "groupId") Long groupId) {
        return new ResponseEntity<>(studentDataRepository.findAllByStudyGroupId(groupId)
                                                         .stream()
                                                         .map(userService::mapStudentDataDbToUserApi)
                                                         .collect(Collectors.toList()),
                                    HttpStatus.OK);
    }

    @PutMapping(value = "/student/{studentId}/group/remove")
    public ResponseEntity<?> removeStudentFromGroup(@PathVariable(value = "studentId") Long studentId) {
        StudentDB student = studentRepository.findById(studentId).orElseThrow(NotFoundException::new);

        student.setStudyGroupId(null);
        return new ResponseEntity<>(studentRepository.save(student), HttpStatus.OK);
    }

    @GetMapping(value = "/students/{universityId}/without/group")
    public List<UserApi> getStudentsWithoutGroupByUniversityId(
            @PathVariable(value = "universityId") Long universityId) {
        return studentDataRepository.findAllByStudyGroupIdAndUniversityId(null, universityId)
                                    .stream()
                                    .map(userService::mapStudentDataDbToUserApi)
                                    .collect(Collectors.toList());
    }

    @PutMapping(value = "/student/add/group")
    public ResponseEntity<?> addStudentToGroup(@RequestBody AddStudentsToGroupApi addStudentsToGroupApi) {
        List<StudentDB> students = addStudentsToGroupApi
                .getStudentsIds()
                .stream()
                .map(id -> studentRepository.findById(id).orElseThrow(NotFoundException::new))
                .peek(s -> s.setStudyGroupId(addStudentsToGroupApi.getGroupId()))
                .collect(Collectors.toList());

        studentRepository.saveAll(students);

        return getStudentsByGroupId(addStudentsToGroupApi.getGroupId());
    }
}