package com.educationapp.server.models.persistence;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileDB {

    public FileDB(final String path, final String name, final Long subjectId, final Integer fileTypeId) {
        this.path = path;
        this.name = name;
        this.subjectId = subjectId;
        this.fileTypeId = fileTypeId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "type_id")
    private Integer fileTypeId;

    @CreationTimestamp
    @Column(name = "create_date")
    private Date createDate;
}