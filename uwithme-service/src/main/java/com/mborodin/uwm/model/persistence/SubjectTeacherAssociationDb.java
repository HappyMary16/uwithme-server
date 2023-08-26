package com.mborodin.uwm.model.persistence;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@IdClass(SubjectTeacherAssociationDb.SubjectTeacherAssociationPK.class)
@Table(name = "subject_teacher")
public class SubjectTeacherAssociationDb {

    @Id
    private Long subjectId;
    @Id
    private String teacherId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubjectTeacherAssociationPK implements Serializable {

        protected Long subjectId;
        protected String teacherId;
    }
}
