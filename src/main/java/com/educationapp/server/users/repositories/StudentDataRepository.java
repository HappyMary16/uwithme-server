package com.educationapp.server.users.repositories;

import java.util.List;

import com.educationapp.server.users.model.persistence.StudentDataDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StudentDataRepository extends CrudRepository<StudentDataDb, Long> {

    @Query(value = "SELECT DISTINCT s.id, s.study_group_id, student_id, " +
            "u.first_name, last_name, surname, username, password, phone, email, role, is_admin, university_id " +
            "FROM students s JOIN users u ON u.id = s.id " +
            "LEFT JOIN schedule s2 on s.study_group_id = s2.study_group_id " +
            "LEFT JOIN subjects s3 ON s2.subject_id = s3.id " +
            "WHERE s3.teacher_id = :teacherId",
            nativeQuery = true)
    List<StudentDataDb> findAllByTeacherId(@Param("teacherId") Long teacherId);

    List<StudentDataDb> findAllByUniversityId(final Long groupId);
}
