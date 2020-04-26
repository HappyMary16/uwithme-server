package com.educationapp.server.users.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
    @Column(name = "id")
    @NotNull
    private Long id;

    @Column(name = "study_group_id")
    @NotNull
    private Long studyGroupId;

    @Column(name = "student_id")
    @NotNull
    private String studentId;
}
