package com.educationapp.server.services;

import com.educationapp.api.CreateLessonApi;
import com.educationapp.api.DeleteLessonApi;
import com.educationapp.api.KeycloakUserApi;
import com.educationapp.api.LessonApi;
import com.educationapp.server.clients.KeycloakServiceClient;
import com.educationapp.server.enums.Role;
import com.educationapp.server.exception.UserNotFoundException;
import com.educationapp.server.models.persistence.*;
import com.educationapp.server.repositories.*;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final LectureHallRepository lectureHallRepository;
    private final KeycloakServiceClient keycloakServiceClient;

    public void createLesson(final CreateLessonApi createLessonApi) {
        final List<ScheduleDb> lessonsToCreate = buildLessons(createLessonApi);
        filterDuplicatedEntity(lessonsToCreate, createLessonApi.getGroups());
        scheduleRepository.saveAll(lessonsToCreate);
    }

    public List<LessonApi> findLessonsByGroupId(final Long groupId) {
        return scheduleRepository.findAllByStudyGroupId(groupId)
                .stream()
                .map(schedule -> mapScheduleDbToLesson(schedule,
                        keycloakServiceClient.getUsersByIds(List.of())))
                .collect(Collectors.toList());
    }

    public List<LessonApi> findUsersLessons() {
        final Role userRole = UserContextHolder.getRole();

        if (STUDENT.equals(userRole)) {
            final Long groupId = UserContextHolder.getGroupId();
            return new ArrayList<>(findLessonsByGroupId(groupId));
        } else if (TEACHER.equals(userRole)) {
            return findLessonsByTeacherId(UserContextHolder.getId());
        } else {
            throw new RuntimeException("Schedule exist only for students and teacher");
        }
    }

    public List<LessonApi> findLessonsById(final String userId) {
        final UserDb user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (user.getRole().equals(STUDENT.getId())) {
            return new ArrayList<>(findLessonsByGroupId(user.getStudyGroup().getId()));
        } else if (user.getRole().equals(TEACHER.getId())) {
            return findLessonsByTeacherId(user.getId());
        } else {
            throw new RuntimeException("Schedule exist only for students and teacher");
        }
    }

    public LessonApi deleteLesson(final DeleteLessonApi deleteLessonApi) {
        final List<String> groupNames = deleteLessonApi.getGroups() != null
                ? deleteLessonApi.getGroups()
                : List.of();

        final Long lessonId = deleteLessonApi.getLessonId();
        final ScheduleDb schedule = scheduleRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson is not found"));

        if (groupNames.isEmpty()) {
            schedule.setGroups(null);
            scheduleRepository.save(schedule);
            scheduleRepository.deleteById(lessonId);
            return null;
        } else {
            final List<StudyGroupDb> groups = schedule.getGroups()
                    .stream()
                    .filter(group -> !groupNames.contains(group.getName()))
                    .collect(Collectors.toList());
            schedule.setGroups(groups);
            return mapScheduleDbToLesson(scheduleRepository.save(schedule),
                    keycloakServiceClient.getUsersByIds(List.of()));
        }
    }

    private LessonApi mapScheduleDbToLesson(final ScheduleDb scheduleDb, final Map<String, KeycloakUserApi> teachers) {
        final LessonApi.LessonApiBuilder lesson = LessonApi.builder()
                .id(scheduleDb.getId())
                .lessonTime(scheduleDb.getLessonNumber())
                .building(scheduleDb.getAuditory().getBuilding().getName())
                .lectureHall(scheduleDb.getAuditory().getName())
                .weekDay(scheduleDb.getDayOfWeek())
                .weekNumber(scheduleDb.getWeekNumber());

        final SubjectDB subject = scheduleDb.getSubject();
        if (Objects.nonNull(subject)) {
            final String teacherId = subject.getTeacher().getId();

            final KeycloakUserApi teacher = Optional.ofNullable(teachers.get(teacherId))
                    .orElseGet(() -> keycloakServiceClient.getUserById(teacherId));

            lesson.subjectName(subject.getName())
                    .teacherName(teacher.getLastName());
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
        log.info("Create lesson by api: {}", createLessonApi.toString());
        //TODO fix get
        final LectureHallDb lectureHallDb = lectureHallRepository.findById(createLessonApi.getLectureHall()).get();
        final ScheduleDb.ScheduleDbBuilder scheduleDb = ScheduleDb.builder()
                .auditory(lectureHallDb);

        if (createLessonApi.getTeacherId() == null && createLessonApi.getSubjectId() == null) {
            scheduleDb.subjectName(createLessonApi.getSubjectName());
            scheduleDb.teacherName(createLessonApi.getTeacherName());

        } else if (createLessonApi.getTeacherId() == null) {
            scheduleDb.teacherName(createLessonApi.getTeacherName());
            subjectRepository.findById(createLessonApi.getSubjectId())
                    .ifPresent(subject -> scheduleDb.subjectName(subject.getName()));

        } else if (createLessonApi.getSubjectId() == null) {
            final SubjectDB newSubject = new SubjectDB(createLessonApi.getSubjectName(),
                    userRepository
                            .getProxyByIdIfExist(createLessonApi.getTeacherId()));
            scheduleDb.subject(newSubject);

        } else {
            final Optional<SubjectDB> subjectDb =
                    subjectRepository.findByNameAndTeacherId(createLessonApi.getSubjectName(),
                            createLessonApi.getTeacherId());
            subjectDb.ifPresentOrElse(scheduleDb::subject,
                    () -> {
                        final SubjectDB newSubject =
                                new SubjectDB(createLessonApi.getSubjectName(),
                                        userRepository.getProxyByIdIfExist(
                                                createLessonApi.getTeacherId()));
                        scheduleDb.subject(newSubject);
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

    private List<LessonApi> findLessonsByTeacherId(final String teacherId) {
        return scheduleRepository.findAllByTeacherId(teacherId)
                .stream()
                .map(schedule -> mapScheduleDbToLesson(schedule, keycloakServiceClient.getUsersByIds(List.of())))
                .collect(Collectors.toList());
    }
}
