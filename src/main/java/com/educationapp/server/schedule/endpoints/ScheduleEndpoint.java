package com.educationapp.server.schedule.endpoints;

import java.util.List;

import com.educationapp.server.schedule.models.api.CreateLessonApi;
import com.educationapp.server.schedule.models.api.LessonApi;
import com.educationapp.server.schedule.servises.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ScheduleEndpoint {

    @Autowired
    private ScheduleService scheduleService;

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
