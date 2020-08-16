package com.educationapp.server.models.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UploadFileResponseApi {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
