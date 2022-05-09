package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.model.persistence.StudyGroupDataDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupDataRepository extends JpaRepository<StudyGroupDataDb, Long> {

    List<StudyGroupDataDb> findAllByUniversityId(Long universityId);

    List<StudyGroupDataDb> findAllByVisibleAndDepartmentId(boolean isVisible, long departmentId);

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
