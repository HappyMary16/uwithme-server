package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.StudyGroupDataDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupDataRepository extends JpaRepository<StudyGroupDataDb, Long> {

    Optional<StudyGroupDataDb> findById(Long id);

    @Query(value = "SELECT * FROM study_groups sg " +
            "JOIN departments d ON sg.department_id = d.id " +
            "JOIN institutes i on d.institute_id = i.id " +
            "WHERE i.university_id = :universityId",
            nativeQuery = true)
    List<StudyGroupDataDb> findAllByUniversityId(Long universityId);

    List<StudyGroupDataDb> findAllByIsShowingInRegistration(Boolean isShowingInRegistration);

    @Query(value = "SELECT * FROM study_groups sg " +
            "JOIN schedule_group s on sg.id = s.group_id " +
            "JOIN schedule s2 on s.schedule_id = s2.id " +
            "JOIN subjects s3 on s2.subject_id = s3.id " +
            "WHERE s3.teacher_id = :teacherId",
            nativeQuery = true)
    List<StudyGroupDataDb> findAllByTeacher(String teacherId);

    default StudyGroupDataDb getProxyByIdIfExist(final Long id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
