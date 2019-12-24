package com.educationapp.server.users.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teachers")
public class TeacherDB implements Serializable {

    private static final long serialVersionUID = 8421354192650411535L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "science_degree_id")
    private Long scienceDegreeId;
}
