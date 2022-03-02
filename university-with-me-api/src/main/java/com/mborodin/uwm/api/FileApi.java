package com.mborodin.uwm.api;

import java.util.Date;

import com.mborodin.uwm.api.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileApi {

    private long fileId;
    private String fileName;
    @Deprecated
    private Integer type;
    private FileType fileType;
    private long subjectId;
    private long teacherId;
    private Date startAccessTime;
}
