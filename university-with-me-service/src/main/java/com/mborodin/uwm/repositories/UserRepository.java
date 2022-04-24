package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.model.persistence.UserDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDb, String> {

    List<UserDb> findAllByGroupIdIsNullAndUniversityId(Long universityId);

    List<UserDb> findAllByGroupId(Long groupId);

    @Query("SELECT DISTINCT user " +
            "FROM UserDb user " +
            "LEFT JOIN ScheduleGroupDb scheduleGroup " +
            "ON user.groupId = scheduleGroup.groupId " +
            "LEFT JOIN ScheduleDb schedule " +
            "ON scheduleGroup.scheduleId = schedule.id " +
            "LEFT JOIN SubjectDB subject " +
            "ON schedule.subject.id = subject.id " +
            "WHERE subject.teacher.id = :teacherId ")
    List<UserDb> findStudentsByTeacherId(@Param("teacherId") String teacherId);

    @Query("SELECT DISTINCT user " +
            "FROM UserDb user " +
            "LEFT JOIN SubjectDB subject " +
            "ON user.id = subject.teacher.id " +
            "LEFT JOIN ScheduleDb schedule " +
            "ON subject.id = schedule.subject.id " +
            "LEFT JOIN ScheduleGroupDb scheduleGroup " +
            "ON schedule.id = scheduleGroup.scheduleId " +
            "WHERE scheduleGroup.groupId = :groupId ")
    List<UserDb> findTeachersByGroupId(@Param("groupId") Long groupId);

    List<UserDb> findAllByDepartmentId(Long departmentId);

    List<UserDb> findAllByUniversityId(Long universityId);

    default UserDb getProxyByIdIfExist(final String id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
