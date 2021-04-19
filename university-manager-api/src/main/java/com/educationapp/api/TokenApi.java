package com.educationapp.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenApi {

    private String authToken;

    private String refreshToken;
}
