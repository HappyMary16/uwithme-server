package com.educationapp.server.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

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
