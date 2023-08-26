package com.mborodin.uwm.repositories;

import java.util.Collection;
import java.util.List;

import com.mborodin.uwm.model.persistence.StudyGroupDataDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupDataRepository extends JpaRepository<StudyGroupDataDb, Long> {

    List<StudyGroupDataDb> findAllByUniversityId(Long universityId);

    List<StudyGroupDataDb> findAllByVisibleAndDepartmentId(boolean isVisible, String departmentId);

    Collection<StudyGroupDataDb> findAllByDepartmentId(String departmentId);
    void deleteAllByDepartmentId(String departmentId);
}
