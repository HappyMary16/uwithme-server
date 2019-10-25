package com.educationapp.server.model.persistence;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "students")
public class StudentDB implements Serializable {

    private static final long serialVersionUID = 1147071389117659854L;

    @Id
    @UniqueElements
    @Column(name = "id")
    private Long id;

    @Column(name = "study_group_id")
    private String studyGroupId;

    @UniqueElements
    @Column(name = "student_id")
    private String studentId;
}
