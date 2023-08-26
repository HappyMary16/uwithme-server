package com.mborodin.uwm.services;

import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.getLanguages;
import static com.mborodin.uwm.security.UserContextHolder.hasRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import com.mborodin.uwm.api.CreateLessonApi;
import com.mborodin.uwm.api.DeleteLessonApi;
import com.mborodin.uwm.api.LessonApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.exceptions.UserNotFoundException;
import com.mborodin.uwm.model.persistence.BuildingDb;
import com.mborodin.uwm.model.persistence.LectureHallDb;
import com.mborodin.uwm.model.persistence.ScheduleDb;
import com.mborodin.uwm.model.persistence.StudyGroupDb;
import com.mborodin.uwm.model.persistence.SubjectDB;
import com.mborodin.uwm.model.persistence.UserDb;
import com.mborodin.uwm.repositories.BuildingsRepository;
import com.mborodin.uwm.repositories.LectureHallRepository;
import com.mborodin.uwm.repositories.ScheduleRepository;
import com.mborodin.uwm.repositories.StudyGroupRepository;
import com.mborodin.uwm.repositories.SubjectRepository;
import com.mborodin.uwm.repositories.UserRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final BuildingsRepository buildingsRepository;
    private final LectureHallRepository lectureHallRepository;
    private final KeycloakUserService keycloakUserService;

    public void createLesson(final CreateLessonApi createLessonApi) {
        final List<ScheduleDb> lessonsToCreate = buildLessons(createLessonApi);
        filterDuplicatedEntity(lessonsToCreate, createLessonApi.getGroups());
        scheduleRepository.saveAll(lessonsToCreate);
    }

    public List<LessonApi> findLessonsByGroupId(final Long groupId) {
        return mapScheduleToLessons(scheduleRepository.findAllByStudyGroupId(groupId));
    }

    public List<LessonApi> findUsersLessons() {
        if (hasRole(ROLE_STUDENT)) {
            final Long groupId = UserContextHolder.getGroupId();
            return new ArrayList<>(findLessonsByGroupId(groupId));
        } else if (hasRole(ROLE_TEACHER)) {
            return findLessonsByTeacherId(UserContextHolder.getId());
        } else {
            throw new RuntimeException("Schedule exist only for students and teacher");
        }
    }

    public List<LessonApi> findLessonsById(final String userId) {
        final UserDb user = userRepository.findById(userId)
                                          .orElseThrow(() -> new UserNotFoundException(getLanguages()));
        final Set<Role> roles = keycloakUserService.getUserRoles(userId);

        if (roles.contains(ROLE_STUDENT)) {
            return findLessonsByGroupId(user.getGroupId());
        } else if (roles.contains(ROLE_TEACHER)) {
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

            final UserRepresentation teacher = Optional.ofNullable(getTeacherFromSchedule(schedule))
                                                       .map(keycloakUserService::getUser)
                                                       .orElse(null);

            return mapScheduleDbToLesson(scheduleRepository.save(schedule),
                                         Objects.isNull(teacher)
                                                 ? Map.of()
                                                 : Map.of(teacher.getId(), teacher));
        }
    }

    public void deleteLessonsByGroupId(final Long groupId) {
        final var lessonIds = scheduleRepository.findAllByStudyGroupId(groupId)
                                                .stream()
                                                .map(ScheduleDb::getId)
                                                .collect(Collectors.toList());

        lessonIds.forEach(lessonId -> scheduleRepository.deleteAllByGroupIdAndLessonId(groupId, lessonId));
    }


    private LessonApi mapScheduleDbToLesson(final ScheduleDb scheduleDb,
                                            final Map<String, UserRepresentation> teachers) {
        final String buildingName = buildingsRepository.findById(scheduleDb.getAuditory().getBuildingId())
                                                       .map(BuildingDb::getName)
                                                       .orElse(null);

        final LessonApi.LessonApiBuilder lesson = LessonApi.builder()
                                                           .id(scheduleDb.getId())
                                                           .lessonTime(scheduleDb.getLessonNumber())
                                                           .building(buildingName)
                                                           .lectureHall(scheduleDb.getAuditory().getName())
                                                           .weekDay(scheduleDb.getDayOfWeek())
                                                           .weekNumber(scheduleDb.getWeekNumber());

        final SubjectDB subject = scheduleDb.getSubject();
        if (Objects.nonNull(subject)) {
            final String teacherId = subject.getTeacherId();

            final UserRepresentation teacher = Optional.ofNullable(teachers.get(teacherId))
                                                       .orElseGet(() -> keycloakUserService.getUser(teacherId));

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
                                                       createLessonApi.getTeacherId());
            scheduleDb.subject(newSubject);

        } else {
            final Optional<SubjectDB> subjectDb =
                    subjectRepository.findByNameAndTeacherId(createLessonApi.getSubjectName(),
                                                             createLessonApi.getTeacherId());
            subjectDb.ifPresentOrElse(scheduleDb::subject,
                                      () -> {
                                          final SubjectDB newSubject = new SubjectDB(createLessonApi.getSubjectName(),
                                                                                     createLessonApi.getTeacherId());
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
        return mapScheduleToLessons(scheduleRepository.findAllByTeacherId(teacherId));
    }

    private List<LessonApi> mapScheduleToLessons(final List<ScheduleDb> schedule) {
        final Map<String, UserRepresentation> teachers = new HashMap<>();

        return schedule.stream()
                       .peek(s -> Optional.ofNullable(getTeacherFromSchedule(s))
                                          .filter(Predicate.not(teachers::containsKey))
                                          .map(keycloakUserService::getUser)
                                          .ifPresent(teacher -> teachers.put(teacher.getId(), teacher)))
                       .map(s -> mapScheduleDbToLesson(s, teachers))
                       .collect(Collectors.toList());
    }

    private String getTeacherFromSchedule(final ScheduleDb schedule) {
        return Optional.ofNullable(schedule)
                       .map(ScheduleDb::getSubject)
                       .map(SubjectDB::getTeacherId)
                       .orElse(null);
    }
}
