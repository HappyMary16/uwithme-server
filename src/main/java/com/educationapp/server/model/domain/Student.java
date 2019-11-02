package com.educationapp.server.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User {

    private String studyGroupId;
    private String studentId;

    @Builder(toBuilder = true)
    public Student(Long id, String firstName, String lastName, String nickname, String password, String phone,
                   String email, String studyGroupId, String studentId) {
        super(id, firstName, lastName, nickname, password, phone, email);
        this.studyGroupId = studyGroupId;
        this.studentId = studentId;
    }
}
