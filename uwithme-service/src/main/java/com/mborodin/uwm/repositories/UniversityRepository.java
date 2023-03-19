package com.mborodin.uwm.repositories;

import com.mborodin.uwm.model.persistence.TenantDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<TenantDb, Long> {

}
