package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.educationapp.server.models.api.CreateLessonApi;
import com.educationapp.server.models.api.admin.DeleteLessonApi;
import com.educationapp.server.security.UserContextHolder;
import com.educationapp.server.services.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/lessons")
public class ScheduleEndpoint {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<?> addLesson(@RequestBody CreateLessonApi createLessonApi) {
        scheduleService.createLesson(createLessonApi);
        return new ResponseEntity<>(CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLesson(@RequestBody DeleteLessonApi deleteLessonApi) {
        return new ResponseEntity<>(scheduleService.deleteLesson(deleteLessonApi), OK);
    }

    @GetMapping
    public ResponseEntity<?> getLessons() {
        final String username = UserContextHolder.getUser().getUsername();
        return new ResponseEntity<>(scheduleService.findLessonsByUsername(username), OK);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getLessonsByGroup(@PathVariable("groupId") Long groupId) {
        return new ResponseEntity<>(scheduleService.findLessonsByGroupId(groupId), OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getLessonsByUserId(@PathVariable("username") String username) {
        return new ResponseEntity<>(scheduleService.findLessonsByUsername(username), OK);
    }
}
