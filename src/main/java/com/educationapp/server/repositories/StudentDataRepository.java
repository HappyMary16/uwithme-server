package com.educationapp.server.repositories;

import java.util.List;

import com.educationapp.server.models.persistence.StudentDataDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StudentDataRepository extends CrudRepository<StudentDataDb, String> {

    @Query(value = "SELECT DISTINCT s.id, s.group_id, student_id, " +
            "u.first_name, last_name, surname, username, password, phone, email, role, is_admin, university_id " +
            "FROM students s JOIN users u ON u.id = s.id " +
            "JOIN schedule_group sg on s.group_id = sg.group_id " +
            "LEFT JOIN schedule s2 on sg.schedule_id = s2.id " +
            "LEFT JOIN subjects s3 ON s2.subject_id = s3.id " +
            "WHERE s3.teacher_id = :teacherId",
            nativeQuery = true)
    List<StudentDataDb> findAllByTeacherId(@Param("teacherId") String teacherId);

    List<StudentDataDb> findAllByUniversityId(Long groupId);

    List<StudentDataDb> findAllByStudyGroupId(Long groupId);

    List<StudentDataDb> findAllByStudyGroupIdAndUniversityId(Long groupId, Long universityId);
}