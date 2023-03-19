package com.mborodin.uwm.model.persistence;

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
@Table(name = "study_groups")
public class StudyGroupDataDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private Integer course;

    private Long teacherId;

    @Column(name = "is_showing_in_registration")
    private boolean visible;

    private String departmentId;
    private long universityId;
}
