package com.educationapp.server.schedule.servises;

import static com.educationapp.server.common.enums.Role.STUDENT;
import static com.educationapp.server.common.enums.Role.TEACHER;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.educationapp.server.files.models.persistence.SubjectDB;
import com.educationapp.server.files.repositories.SubjectRepository;
import com.educationapp.server.schedule.models.ScheduleDb;
import com.educationapp.server.schedule.models.api.CreateLessonApi;
import com.educationapp.server.schedule.models.api.LessonApi;
import com.educationapp.server.schedule.repositories.ScheduleRepository;
import com.educationapp.server.university.repositories.StudyGroupRepository;
import com.educationapp.server.users.model.persistence.UserDB;
import com.educationapp.server.users.repositories.StudentRepository;
import com.educationapp.server.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private StudentRepository studentRepository;

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

    public List<LessonApi> findLessonsByGroupId(final Long groupId) {
        return scheduleRepository.findAllByStudyGroupId(groupId)
                                 .stream()
                                 .map(this::mapScheduleDbToLesson)
                                 .collect(Collectors.toList());
    }

    public List<LessonApi> findLessonsByUserId(final Long userId) {
        final Optional<UserDB> user = userRepository.findById(userId);
        final List<LessonApi> lessons = new ArrayList<>();

        if (user.isPresent() && user.get().getRole().equals(STUDENT.getId())) {
            studentRepository.findById(userId)
                             .ifPresent(student -> lessons.addAll(findLessonsByGroupId(student.getStudyGroupId())));
        } else if (user.isPresent() && user.get().getRole().equals(TEACHER.getId())) {
            return scheduleRepository.findAllByTeacherId(userId)
                                     .stream()
                                     .map(this::mapScheduleDbToLesson)
                                     .collect(Collectors.toList());
        } else {
            throw new NotImplementedException();
        }

        return lessons;
    }

    private LessonApi mapScheduleDbToLesson(final ScheduleDb scheduleDb) {
        final LessonApi.LessonApiBuilder lesson = LessonApi.builder()
                                                           .lessonTime(scheduleDb.getLessonNumber())
                                                           .lectureHall(scheduleDb.getAuditory())
                                                           .weekDay(scheduleDb.getDayOfWeek())
                                                           .weekNumber(scheduleDb.getWeekNumber());

        if (Objects.nonNull(scheduleDb.getSubjectId())) {
            subjectRepository
                    .findById(scheduleDb.getSubjectId())
                    .ifPresent(subject -> {
                        lesson.subjectName(subject.getName());
                        userRepository.findById(subject.getTeacherId())
                                      .ifPresent(teacher -> lesson
                                              .teacherName(teacher.getSurname() + " " + teacher.getFirstName() +
                                                                   " " + teacher.getLastName()));
                    });
        } else {
            lesson.subjectName(scheduleDb.getSubjectName())
                  .teacherName(scheduleDb.getTeacherName());
        }

        studyGroupRepository.findById(scheduleDb.getStudyGroupId())
                            .ifPresent(group -> lesson.groupName(group.getName()));

        return lesson.build();
    }
}
