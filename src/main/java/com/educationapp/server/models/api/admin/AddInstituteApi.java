package com.educationapp.server.models.api.admin;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddInstituteApi {

    @NotNull
    private String instituteName;
}
