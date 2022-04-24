package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.getId;
import static com.mborodin.uwm.security.UserContextHolder.hasRole;

import java.util.List;

import com.mborodin.uwm.api.CreateLessonApi;
import com.mborodin.uwm.api.DeleteLessonApi;
import com.mborodin.uwm.api.LessonApi;
import com.mborodin.uwm.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lessons")
public class ScheduleEndpoint {

    private final ScheduleService scheduleService;

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    @PostMapping
    public void addLesson(@RequestBody CreateLessonApi createLessonApi) {
        final CreateLessonApi toCreate = hasRole(ROLE_TEACHER)
                ? createLessonApi.toBuilder()
                                 .teacherId(getId())
                                 .teacherName(null)
                                 .build()
                : createLessonApi;

        scheduleService.createLesson(toCreate);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping
    public LessonApi deleteLesson(@RequestBody DeleteLessonApi deleteLessonApi) {
        return scheduleService.deleteLesson(deleteLessonApi);
    }

    @Secured({"ROLE_STUDENT", "ROLE_TEACHER"})
    @GetMapping
    public List<LessonApi> getLessons() {
        return scheduleService.findUsersLessons();
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_SERVICE"})
    @GetMapping("/group/{groupId}")
    public List<LessonApi> getLessonsByGroup(@PathVariable("groupId") Long groupId) {
        return scheduleService.findLessonsByGroupId(groupId);
    }

    @GetMapping("/username/{userId}")
    public List<LessonApi> getLessonsByUserId(@PathVariable("userId") String userId) {
        return scheduleService.findLessonsById(userId);
    }
}
