package com.mborodin.uwm.model.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tenant_departments")
public class TenantDepartmentDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String departmentId;
    private String name;
    private String mainDepartmentId;
    private long tenantId;
}
