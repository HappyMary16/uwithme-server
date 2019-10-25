package com.educationapp.server.model.domain;

import lombok.*;


@EqualsAndHashCode
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String nickname;
    private String password;
    private String phone;
    private String email;
}
