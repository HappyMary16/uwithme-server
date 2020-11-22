package com.educationapp.server.models.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class SimpleUserDb implements Serializable {

    private static final long serialVersionUID = 3366295050169335755L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "role")
    private Integer role;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "university_id")
    private Long universityId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "group_id")
    private Long groupId;
}
