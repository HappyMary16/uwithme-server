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

    @Query(value = "SELECT DISTINCT * FROM users u " +
            "JOIN schedule_group sg on u.group_id = sg.group_id " +
            "LEFT JOIN schedule s2 on sg.schedule_id = s2.id " +
            "LEFT JOIN subjects s3 ON s2.subject_id = s3.id " +
            "WHERE u.role = 1 AND s3.teacher_id = :teacherId",
            nativeQuery = true)
    List<UserDb> findStudentsByTeacherId(@Param("teacherId") String teacherId);

    @Query(value = "SELECT DISTINCT * FROM users u " +
            "LEFT JOIN subjects s ON u.id = s.teacher_id " +
            "LEFT JOIN schedule s2 on s.id = s2.subject_id " +
            "JOIN schedule_group sg on s2.id = sg.schedule_id " +
            "WHERE u.role = 2 AND sg.group_id = :groupId",
            nativeQuery = true)
    List<UserDb> findTeachersByGroupId(@Param("groupId") Long groupId);

    List<UserDb> findAllByRoleAndUniversityId(Integer role, Long universityId);

    default UserDb getProxyByIdIfExist(final String id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
