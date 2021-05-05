package com.mborodin.uwm.repositories;

import java.util.List;
import java.util.Optional;

import com.mborodin.uwm.models.persistence.InstituteDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteRepository extends JpaRepository<InstituteDb, Long> {

    List<InstituteDb> findAllByUniversityId(final Long universityId);

    Optional<InstituteDb> findByUniversityIdAndName(final Long universityId, final String name);

    default InstituteDb getProxyByIdIfExist(final Long id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
