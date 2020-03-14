package com.educationapp.server.schedule.servisec;

import java.util.Optional;

import com.educationapp.server.files.models.persistence.SubjectDB;
import com.educationapp.server.files.repositories.SubjectRepository;
import com.educationapp.server.schedule.models.ScheduleDb;
import com.educationapp.server.schedule.models.api.CreateLessonApi;
import com.educationapp.server.schedule.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public void createLesson(final CreateLessonApi createLessonApi) {
        final ScheduleDb scheduleDb = new ScheduleDb()
                .toBuilder()
                .auditory(createLessonApi.getLectureHall())
                .dayOfWeek(createLessonApi.getWeekDay())
                .weekNumber(createLessonApi.getWeekNumber())
                .lessonNumber(createLessonApi.getLessonTime())
                .build();

        if (createLessonApi.getTeacherId() == null && createLessonApi.getSubjectId() == null) {
            scheduleDb.setSubjectName(createLessonApi.getSubjectName());
            scheduleDb.setTeacherName(createLessonApi.getTeacherName());

        } else if (createLessonApi.getTeacherId() == null) {
            scheduleDb.setTeacherName(createLessonApi.getTeacherName());
            subjectRepository.findById(createLessonApi.getSubjectId())
                             .ifPresent(subject -> scheduleDb.setSubjectName(subject.getName()));

        } else if (createLessonApi.getSubjectId() == null) {
            final SubjectDB newSubject = new SubjectDB(createLessonApi.getSubjectName(),
                                                       createLessonApi.getTeacherId());
            final Long subjectId = subjectRepository.save(newSubject).getId();
            scheduleDb.setSubjectId(subjectId);

        } else {
            final Optional<SubjectDB> subjectDb = subjectRepository.findById(createLessonApi.getSubjectId());
            subjectDb.ifPresent(subject -> {
                if (subject.getTeacherId().equals(createLessonApi.getTeacherId())) {
                    scheduleDb.setSubjectId(createLessonApi.getSubjectId());
                } else {
                    final SubjectDB newSubject = new SubjectDB(subject.getName(),
                                                               createLessonApi.getTeacherId());
                    final Long subjectId = subjectRepository.save(newSubject).getId();
                    scheduleDb.setSubjectId(subjectId);
                }
            });
        }

        createLessonApi.getGroups().forEach(groupId -> {
            scheduleRepository.save(scheduleDb.toBuilder()
                                              .id(null)
                                              .studyGroupId(groupId)
                                              .build());
        });
    }
}
