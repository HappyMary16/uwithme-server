package com.educationapp.server.model.persistence;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "teachers")
public class TeacherDB implements Serializable {

    private static final long serialVersionUID = 8421354192650411535L;

    @Id
    @UniqueElements
    @Column(name = "id")
    private Long id;

    @Column(name = "department_id")
    private String departmentId;
}
