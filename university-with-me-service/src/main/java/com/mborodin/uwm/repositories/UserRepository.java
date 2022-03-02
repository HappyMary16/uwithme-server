package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.model.persistence.UserDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDb, String> {

    List<UserDb> findAllByGroupIdIsNullAndRoleAndUniversityId(Role role, Long universityId);

    List<UserDb> findAllByGroupId(Long groupId);

    @Query("SELECT DISTINCT user " +
            "FROM UserDb user " +
            "LEFT JOIN ScheduleGroupDb scheduleGroup " +
            "ON user.groupId = scheduleGroup.groupId " +
            "LEFT JOIN ScheduleDb schedule " +
            "ON scheduleGroup.scheduleId = schedule.id " +
            "LEFT JOIN SubjectDB subject " +
            "ON schedule.subject.id = subject.id " +
            "WHERE user.role = 'ROLE_STUDENT' " +
            "AND subject.teacher.id = :teacherId ")
    List<UserDb> findStudentsByTeacherId(@Param("teacherId") String teacherId);

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT * " +
                    "FROM users " +
                    "LEFT JOIN subjects " +
                    "ON users.id = subjects.teacher_id " +
                    "LEFT JOIN schedule " +
                    "ON subjects.id = schedule.subject_id " +
                    "LEFT JOIN schedule_group " +
                    "ON schedule.id = schedule_group.schedule_id " +
                    "WHERE users.roles @> 'ROLE_TEACHER' " +
                    "AND schedule_group.group_id = :groupId ")
    List<UserDb> findTeachersByGroupId(@Param("groupId") Long groupId);

    List<UserDb> findAllByDepartmentId(Long departmentId);

    List<UserDb> findAllByRoleAndUniversityId(Role role, Long universityId);

    List<UserDb> findAllByIsAdminAndUniversityId(boolean isAdmin, Long universityId);

    default UserDb getProxyByIdIfExist(final String id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
