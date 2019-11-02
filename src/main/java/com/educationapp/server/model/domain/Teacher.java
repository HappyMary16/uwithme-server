package com.educationapp.server.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends User {

    private String departmentId;

    @Builder(toBuilder = true)
    public Teacher(Long id, String firstName, String lastName, String nickname, String password, String phone,
                   String email, String departmentId) {
        super(id, firstName, lastName, nickname, password, phone, email);
        this.departmentId = departmentId;
    }
}
