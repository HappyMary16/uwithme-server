package com.educationapp.server.models.persistence;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "departments")
public class DepartmentDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "institute_id")
    private Long instituteId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "institute_id", referencedColumnName = "id", insertable = false, updatable = false)
    private InstituteDb institute;
}
