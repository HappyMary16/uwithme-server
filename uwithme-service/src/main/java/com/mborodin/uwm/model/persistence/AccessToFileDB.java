package com.mborodin.uwm.model.persistence;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study_group_file")
public class AccessToFileDB {

    public AccessToFileDB(final Long studyGroupId, final Long fileId, final Date dateAddAccess) {
        this.studyGroupId = studyGroupId;
        this.fileId = fileId;
        this.dateAddAccess = dateAddAccess;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id")
    private Long studyGroupId;
    private Long fileId;
    private Date dateAddAccess;
}
