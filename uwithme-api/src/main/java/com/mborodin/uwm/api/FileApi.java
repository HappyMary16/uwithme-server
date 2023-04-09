package com.mborodin.uwm.api;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mborodin.uwm.api.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Instant startAccessTime;
}
