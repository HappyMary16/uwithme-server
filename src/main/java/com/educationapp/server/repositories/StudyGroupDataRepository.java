package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.StudyGroupDataDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupDataRepository extends JpaRepository<StudyGroupDataDb, Long> {

    Optional<StudyGroupDataDb> findById(Long id);

    @Query("SELECT studyGroup " +
            "FROM StudyGroupDataDb studyGroup " +
            "WHERE studyGroup.department.institute.universityId = :universityId")
    List<StudyGroupDataDb> findAllByUniversityId(Long universityId);

    List<StudyGroupDataDb> findAllByIsVisibleAndDepartment(Boolean isVisible, DepartmentDb department);

    @Query("SELECT DISTINCT studyGroup " +
            "FROM StudyGroupDataDb studyGroup " +
            "JOIN ScheduleGroupDb scheduleGroup " +
            "ON studyGroup.id = scheduleGroup.groupId " +
            "JOIN ScheduleDb schedule " +
            "ON scheduleGroup.scheduleId = schedule.id " +
            "JOIN SubjectDB subject " +
            "ON schedule.subject.teacher.id = :teacherId")
    List<StudyGroupDataDb> findAllByTeacher(String teacherId);

    default StudyGroupDataDb getProxyByIdIfExist(final Long id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
