package com.mborodin.uwm.repositories;

import java.util.List;
import java.util.Set;

import com.mborodin.uwm.model.persistence.UserDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDb, String> {

    List<UserDb> findAllByGroupIdIsNullAndUniversityId(Long universityId);

    List<UserDb> findAllByDepartmentId(String departmentId);

    List<UserDb> findAllByGroupId(Long groupId);

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

    List<UserDb> findAllByUniversityIdAndIdIn(Long universityId, Set<String> ids);

    default UserDb getProxyByIdIfExist(final String id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
