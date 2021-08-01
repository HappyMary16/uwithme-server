package com.mborodin.uwm.api.studcab;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthEntity {

    private String email;
    private String password;
}
