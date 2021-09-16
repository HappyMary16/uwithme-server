package com.mborodin.uwm.repositories;

import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.models.persistence.UserDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDb, String> {

    Optional<UserDb> findById(final String username);

    List<UserDb> findAllByStudyGroupIsNullAndRoleAndUniversityId(Role role, Long universityId);

    List<UserDb> findAllByStudyGroupId(Long groupId);

    @Query("SELECT DISTINCT user " +
            "FROM UserDb user " +
            "LEFT JOIN ScheduleGroupDb scheduleGroup " +
            "ON user.studyGroup.id = scheduleGroup.groupId " +
            "LEFT JOIN ScheduleDb schedule " +
            "ON scheduleGroup.scheduleId = schedule.id " +
            "LEFT JOIN SubjectDB subject " +
            "ON schedule.subject.id = subject.id " +
            "WHERE (user.oldRole = 1 OR user.role = 'ROLE_STUDENT') " +
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
            "WHERE (user.oldRole = 2 OR user.role = 'ROLE_TEACHER') " +
            "AND scheduleGroup.groupId = :groupId ")
    List<UserDb> findTeachersByGroupId(@Param("groupId") Long groupId);

    List<UserDb> findAllByRoleAndUniversityId(Role role, Long universityId);

    List<UserDb> findAllByIsAdminAndUniversityId(boolean isAdmin, Long universityId);

    default UserDb getProxyByIdIfExist(final String id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
