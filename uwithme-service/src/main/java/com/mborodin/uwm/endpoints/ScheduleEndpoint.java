package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.getId;
import static com.mborodin.uwm.security.UserContextHolder.hasRole;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.CreateLessonApi;
import com.mborodin.uwm.api.DeleteLessonApi;
import com.mborodin.uwm.api.LessonApi;
import com.mborodin.uwm.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public Collection<LessonApi> getLessons(@QueryParam("groupId") Long groupId,
                                            @QueryParam("userId") String userId) {
        final HashSet<LessonApi> lessons = new HashSet<>();

        if (Objects.nonNull(userId)) {
            lessons.addAll(scheduleService.findLessonsById(userId));
        }

        if (Objects.nonNull(groupId) && Objects.nonNull(userId)) {
            lessons.retainAll(scheduleService.findLessonsByGroupId(groupId));
            return lessons;
        }

        if (Objects.nonNull(groupId)) {
            lessons.addAll(scheduleService.findLessonsByGroupId(groupId));
        }

        if (Objects.isNull(groupId) && Objects.isNull(userId)) {
            lessons.addAll(scheduleService.findUsersLessons());
        }

        return lessons;
    }
}
