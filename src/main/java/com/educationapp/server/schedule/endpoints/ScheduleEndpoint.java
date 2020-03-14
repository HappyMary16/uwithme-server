package com.educationapp.server.schedule.endpoints;

import com.educationapp.server.schedule.models.api.CreateLessonApi;
import com.educationapp.server.schedule.servisec.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @PostMapping("/addLesson")
    public void addLesson(@RequestBody CreateLessonApi createLessonApi) {
        scheduleService.createLesson(createLessonApi);
    }

}
