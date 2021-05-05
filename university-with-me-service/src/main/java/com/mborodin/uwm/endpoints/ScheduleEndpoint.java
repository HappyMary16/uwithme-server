package com.mborodin.uwm.endpoints;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.mborodin.uwm.api.CreateLessonApi;
import com.mborodin.uwm.api.DeleteLessonApi;
import com.mborodin.uwm.services.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/lessons")
public class ScheduleEndpoint {

    private final ScheduleService scheduleService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addLesson(@RequestBody CreateLessonApi createLessonApi) {
        scheduleService.createLesson(createLessonApi);
        return new ResponseEntity<>(CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteLesson(@RequestBody DeleteLessonApi deleteLessonApi) {
        return new ResponseEntity<>(scheduleService.deleteLesson(deleteLessonApi), OK);
    }

    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER')")
    @GetMapping
    public ResponseEntity<?> getLessons() {
        return new ResponseEntity<>(scheduleService.findUsersLessons(), OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER', 'ROLE_SERVICE')")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getLessonsByGroup(@PathVariable("groupId") Long groupId) {
        return new ResponseEntity<>(scheduleService.findLessonsByGroupId(groupId), OK);
    }

    @GetMapping("/username/{userId}")
    public ResponseEntity<?> getLessonsByUserId(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(scheduleService.findLessonsById(userId), OK);
    }
}
