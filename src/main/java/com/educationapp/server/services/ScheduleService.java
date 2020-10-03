package com.educationapp.server.services;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import com.educationapp.server.models.api.CreateLessonApi;
import com.educationapp.server.models.api.LessonApi;
import com.educationapp.server.models.api.admin.DeleteLessonApi;
import com.educationapp.server.models.persistence.ScheduleDb;
import com.educationapp.server.models.persistence.StudyGroupDb;
import com.educationapp.server.models.persistence.SubjectDB;
import com.educationapp.server.models.persistence.UserDB;
import com.educationapp.server.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudyGroupRepository studyGroupRepository;

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
            throw new RuntimeException("Schedule exist only for students and teacher");
        }

        return lessons;
    }

    public void deleteLesson(final DeleteLessonApi deleteLessonApi) {
        final List<String> groupNames = deleteLessonApi.getGroups();
        final Long lessonId = deleteLessonApi.getLessonId();
        final ScheduleDb schedule = scheduleRepository.findById(lessonId)
                                                      .orElseThrow(() -> new NotFoundException("Lesson is not found"));

        if (groupNames == null || groupNames.isEmpty()) {
            schedule.setGroups(null);
            scheduleRepository.save(schedule);
            scheduleRepository.deleteById(lessonId);
        } else {
            schedule.setGroups(studyGroupRepository.findAllByNames(groupNames));
            scheduleRepository.save(schedule);
        }
    }

    private LessonApi mapScheduleDbToLesson(final ScheduleDb scheduleDb) {
        final LessonApi.LessonApiBuilder lesson = LessonApi.builder()
                                                           .id(scheduleDb.getId())
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
                                      .ifPresent(teacher -> lesson.teacherName(getTeacherName(teacher)));
                    });
        } else {
            lesson.subjectName(scheduleDb.getSubjectName())
                  .teacherName(scheduleDb.getTeacherName());
        }

        final List<String> groups = scheduleDb.getGroups()
                                              .stream()
                                              .map(StudyGroupDb::getName)
                                              .collect(Collectors.toList());

        return lesson.groups(groups).build();
    }

    private List<ScheduleDb> buildLessons(final CreateLessonApi createLessonApi) {
        final ScheduleDb.ScheduleDbBuilder scheduleDb = ScheduleDb.builder()
                                                                  .auditory(createLessonApi.getLectureHall());

        if (createLessonApi.getTeacherId() == null && createLessonApi.getSubjectId() == null) {
            scheduleDb.subjectName(createLessonApi.getSubjectName());
            scheduleDb.teacherName(createLessonApi.getTeacherName());

        } else if (createLessonApi.getTeacherId() == null) {
            scheduleDb.teacherName(createLessonApi.getTeacherName());
            subjectRepository.findById(createLessonApi.getSubjectId())
                             .ifPresent(subject -> scheduleDb.subjectName(subject.getName()));

        } else if (createLessonApi.getSubjectId() == null) {
            final SubjectDB newSubject = new SubjectDB(createLessonApi.getSubjectName(),
                                                       createLessonApi.getTeacherId());
            final Long subjectId = subjectRepository.save(newSubject).getId();
            scheduleDb.subjectId(subjectId);

        } else {
            final Optional<SubjectDB> subjectDb =
                    subjectRepository.findByNameAndTeacherId(createLessonApi.getSubjectName(),
                                                             createLessonApi.getTeacherId());
            subjectDb.ifPresentOrElse(subject -> scheduleDb.subjectId(subject.getId()),
                                      () -> {
                                          final SubjectDB newSubject = new SubjectDB(createLessonApi.getSubjectName(),
                                                                                     createLessonApi.getTeacherId());
                                          subjectRepository.save(newSubject);
                                          scheduleDb.subjectId(newSubject.getId());
                                      });
        }

        final List<ScheduleDb.ScheduleDbBuilder> lessons = List.of(scheduleDb);
        final List<StudyGroupDb> groups = studyGroupRepository.findAllByIds(createLessonApi.getGroups());

        return lessons.stream()
                      .flatMap(lesson -> createLessonApi.getWeekDays()
                                                        .stream()
                                                        .map(lesson::dayOfWeek))
                      .flatMap(lesson -> createLessonApi.getWeekNumbers()
                                                        .stream()
                                                        .map(lesson::weekNumber))
                      .flatMap(lesson -> createLessonApi.getLessonTimes()
                                                        .stream()
                                                        .map(lesson::lessonNumber))
                      .map(lesson -> lesson.groups(groups).build())
                      .collect(Collectors.toList());
    }

    private void filterDuplicatedEntity(final List<ScheduleDb> lessons, final List<Long> groups) {
        final List<ScheduleDb> createdLessons = groups.stream()
                                                      .flatMap(group -> scheduleRepository.findAllByStudyGroupId(group)
                                                                                          .stream())
                                                      .collect(Collectors.toList());
        lessons.removeIf(lesson -> createdLessons.stream()
                                                 .anyMatch(createdLesson -> compareLessonTime(lesson, createdLesson)));
    }

    private boolean compareLessonTime(final ScheduleDb scheduleDb1, final ScheduleDb scheduleDb2) {
        return scheduleDb1.getDayOfWeek().equals(scheduleDb2.getDayOfWeek())
                && scheduleDb1.getLessonNumber().equals(scheduleDb2.getLessonNumber())
                && scheduleDb1.getWeekNumber().equals(scheduleDb2.getWeekNumber());
    }

    private String getTeacherName(final UserDB teacher) {
        final String lastName = teacher.getLastName().length() > 1
                ? teacher.getLastName().charAt(0) + "."
                : "";
        return teacher.getSurname() + " " + teacher.getFirstName().charAt(0) + ". " + lastName;
    }
}
