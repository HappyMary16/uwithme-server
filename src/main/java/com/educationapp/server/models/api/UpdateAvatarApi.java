package com.educationapp.server.models.api;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UpdateAvatarApi {

    @NotNull
    private Long userId;

    @NotNull
    private MultipartFile file;

}
