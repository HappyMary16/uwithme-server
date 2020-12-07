package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.UserDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDb, String> {

    Optional<UserDb> findById(final String username);

    List<UserDb> findAllByStudyGroupIsNullAndRoleAndUniversityId(Integer role, Long universityId);

    List<UserDb> findAllByStudyGroupId(Long groupId);

    @Query("SELECT DISTINCT user " +
            "FROM UserDb user " +
            "LEFT JOIN ScheduleGroupDb scheduleGroup " +
            "ON user.studyGroup.id = scheduleGroup.groupId " +
            "LEFT JOIN ScheduleDb schedule " +
            "ON scheduleGroup.scheduleId = schedule.id " +
            "LEFT JOIN SubjectDB subject " +
            "ON schedule.subject.id = subject.id " +
            "WHERE user.role = 1 " +
            "AND subject.teacher.id = :teacherId ")
    List<UserDb> findStudentsByTeacherId(@Param("teacherId") String teacherId);

    @Query("SELECT DISTINCT user " +
            "FROM UserDb user " +
            "LEFT JOIN SubjectDB subject " +
            "ON user.id = subject.teacher.id " +
            "LEFT JOIN ScheduleDb schedule " +
            "ON subject.id = schedule.subject.id " +
            "LEFT JOIN ScheduleGroupDb scheduleGroup " +
            "ON schedule.id = scheduleGroup.scheduleId " +
            "WHERE user.role = 2 " +
            "AND scheduleGroup.groupId = :groupId ")
    List<UserDb> findTeachersByGroupId(@Param("groupId") Long groupId);

    List<UserDb> findAllByRoleAndUniversityId(Integer role, Long universityId);

    default UserDb getProxyByIdIfExist(final String id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
