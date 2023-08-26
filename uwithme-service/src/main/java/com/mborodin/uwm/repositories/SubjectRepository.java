package com.mborodin.uwm.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.mborodin.uwm.model.persistence.SubjectDB;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<SubjectDB, Long> {

    List<SubjectDB> findAllByIdIn(final Collection<Long> id);

    Optional<SubjectDB> findByNameAndTeacherId(final String name, final String teacherId);

    @Query("SELECT subject " +
            "FROM SubjectDB subject " +
            "JOIN UserDb user " +
            "ON subject.teacherId = user.id " +
            "WHERE user.universityId = :universityId")
    List<SubjectDB> findAllByUniversityId(@Param("universityId") Long universityId);
}
