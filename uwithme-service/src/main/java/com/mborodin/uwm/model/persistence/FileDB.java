package com.mborodin.uwm.model.persistence;

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
    private Long id;
    private String name;
    private String path;
    private Long subjectId;
    @Column(name = "type_id")
    private Integer fileTypeId;
    private Date createDate;
}