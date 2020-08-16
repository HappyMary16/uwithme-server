package com.educationapp.server.endpoints;

import java.util.List;

import com.educationapp.server.models.api.CreateLessonApi;
import com.educationapp.server.models.api.LessonApi;
import com.educationapp.server.services.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ScheduleEndpoint {

    private final ScheduleService scheduleService;

    @PostMapping("/lesson/add")
    public void addLesson(@RequestBody CreateLessonApi createLessonApi) {
        scheduleService.createLesson(createLessonApi);
    }

    @GetMapping("/lessons/group/{groupId}")
    public List<LessonApi> getLessonsByGroup(@PathVariable("groupId") Long groupId) {
        return scheduleService.findLessonsByGroupId(groupId);
    }

    @GetMapping("/lessons/user/{username}")
    public List<LessonApi> getLessonsByUserId(@PathVariable("username") String username) {
        return scheduleService.findLessonsByUsername(username);
    }
}
