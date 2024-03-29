package com.mborodin.uwm.model.persistence;

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
public class UserDb implements Serializable {

    private static final long serialVersionUID = 3366295050169335755L;

    @Id
    private String id;
    private Long universityId;
    private String departmentId;
    private Long groupId;
}
