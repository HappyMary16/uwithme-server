package com.mborodin.uwm.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class SaveFileApi {

    private String subjectName;
    private Integer fileTypeId;
    private MultipartFile file;
}
