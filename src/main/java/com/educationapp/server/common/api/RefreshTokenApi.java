package com.educationapp.server.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenApi {

    private String authToken;

    private String refreshToken;
}
