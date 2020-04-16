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
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        final List<ScheduleDb> lessonsToCreate = buildLessons(createLessonApi);
        filterDuplicatedEntity(lessonsToCreate, createLessonApi.getGroups());
        scheduleRepository.saveAll(lessonsToCreate);
    }

    public List<LessonApi> findLessonsByGroupId(final Long groupId) {
        return scheduleRepository.findAllByStudyGroupId(groupId)
                                 .stream()
                                 .map(this::mapScheduleDbToLesson)
                                 .collect(Collectors.toList());
    }

    public List<LessonApi> findLessonsByUsername(final String username) {
        final Optional<UserDB> user = userRepository.findByUsername(username);
        final List<LessonApi> lessons = new ArrayList<>();

        if (user.isPresent() && user.get().getRole().equals(STUDENT.getId())) {
            studentRepository.findById(user.get().getId())
                             .ifPresent(student -> lessons.addAll(findLessonsByGroupId(student.getStudyGroupId())));
        } else if (user.isPresent() && user.get().getRole().equals(TEACHER.getId())) {
            return scheduleRepository.findAllByTeacherId(user.get().getId())
                                     .stream()
                                     .map(this::mapScheduleDbToLesson)
                                     .collect(Collectors.toList());
        } else {
            throw new NotImplementedException("Schedule exist only for students and teacher");
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

    private List<ScheduleDb> buildLessons(final CreateLessonApi createLessonApi) {
        final ScheduleDb scheduleDb = new ScheduleDb()
                .toBuilder()
                .auditory(createLessonApi.getLectureHall())
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

        final List<ScheduleDb.ScheduleDbBuilder> lessons = List.of(scheduleDb.toBuilder());

        return lessons.stream()
                      .flatMap(lesson -> createLessonApi.getGroups()
                                                        .stream()
                                                        .map(lesson::studyGroupId))
                      .flatMap(lesson -> createLessonApi.getWeekDays()
                                                        .stream()
                                                        .map(lesson::dayOfWeek))
                      .flatMap(lesson -> createLessonApi.getWeekNumbers()
                                                        .stream()
                                                        .map(lesson::weekNumber))
                      .flatMap(lesson -> createLessonApi.getLessonTimes()
                                                        .stream()
                                                        .map(lesson::lessonNumber))
                      .map(ScheduleDb.ScheduleDbBuilder::build)
                      .collect(Collectors.toList());
    }

    private void filterDuplicatedEntity(final List<ScheduleDb> lessons, final List<Long> groups) {
        final List<ScheduleDb> createdLessons = scheduleRepository.findAllByStudyGroupId(groups);
        lessons.removeIf(lesson -> createdLessons.stream()
                                                 .anyMatch(createdLesson -> compareLessonTime(lesson,
                                                                                              createdLesson)));
    }

    private boolean compareLessonTime(final ScheduleDb scheduleDb1, final ScheduleDb scheduleDb2) {
        return scheduleDb1.getDayOfWeek().equals(scheduleDb2.getDayOfWeek())
                && scheduleDb1.getLessonNumber().equals(scheduleDb2.getLessonNumber())
                && scheduleDb1.getWeekNumber().equals(scheduleDb2.getWeekNumber())
                && scheduleDb1.getStudyGroupId().equals(scheduleDb2.getStudyGroupId());
    }
}
