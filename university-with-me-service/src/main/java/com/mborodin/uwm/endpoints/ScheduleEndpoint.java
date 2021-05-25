package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.enums.Role.TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.getId;
import static com.mborodin.uwm.security.UserContextHolder.getRole;
import static org.springframework.http.HttpStatus.OK;

import java.util.Objects;

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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    @PostMapping
    public void addLesson(@RequestBody CreateLessonApi createLessonApi) {
        final CreateLessonApi toCreate = Objects.equals(getRole(), TEACHER)
                ? createLessonApi.toBuilder()
                                 .teacherId(getId())
                                 .teacherName(null)
                                 .build()
                : createLessonApi;

        scheduleService.createLesson(toCreate);
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
