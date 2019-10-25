package com.educationapp.server.model.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Student extends User {
    private String studyGroupId;
    private String studentId;
}
