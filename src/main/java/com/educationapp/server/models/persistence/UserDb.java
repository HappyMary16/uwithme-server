package com.educationapp.server.models.persistence;

import java.io.Serializable;

import javax.persistence.*;

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
    @Column(name = "id")
    private String id;

    @Column(name = "role")
    private Integer role;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "university_id")
    private Long universityId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private DepartmentDb department;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private StudyGroupDataDb studyGroup;
}
