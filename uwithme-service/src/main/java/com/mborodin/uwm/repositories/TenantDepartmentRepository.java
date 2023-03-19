package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.model.persistence.DepartmentDb;
import com.mborodin.uwm.model.persistence.TenantDb;
import com.mborodin.uwm.model.persistence.TenantDepartmentDb;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
public interface TenantDepartmentRepository extends CrudRepository<TenantDepartmentDb, String> {

    List<TenantDepartmentDb> findAllByTenantIdAndMainDepartmentIdIsNull(Long tenantId);

    List<TenantDepartmentDb> findAllByTenantIdAndMainDepartmentIdIsNotNull(Long tenantId);

    List<TenantDepartmentDb> findAllByMainDepartmentId(String mainDepartmentId);

    void deleteAllByMainDepartmentId(String mainDepartmentId);
}
