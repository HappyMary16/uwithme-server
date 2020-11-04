package com.educationapp.server.models.persistence;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "group_id")
    private Long studyGroupId;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "date_add_access")
    private Date dateAddAccess;
}
