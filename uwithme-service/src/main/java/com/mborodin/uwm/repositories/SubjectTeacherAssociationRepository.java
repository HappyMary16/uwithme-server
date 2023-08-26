package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.model.persistence.SubjectTeacherAssociationDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectTeacherAssociationRepository extends
        JpaRepository<SubjectTeacherAssociationDb, SubjectTeacherAssociationDb.SubjectTeacherAssociationPK> {

    List<SubjectTeacherAssociationDb> findAllByTeacherId(String teacherId);

    boolean existsBySubjectIdAndTeacherId(Long subjectId, String teacherId);
}
