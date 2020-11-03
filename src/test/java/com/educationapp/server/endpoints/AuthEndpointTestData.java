package com.educationapp.server.endpoints;

import com.educationapp.server.enums.Role;
import com.educationapp.server.models.api.RegisterApi;
import com.educationapp.server.models.api.UserApi;

public class AuthEndpointTestData {

    public static final String AUTH_TOKEN = "authToken";
    public static final String REFRESH_TOKEN = "refreshToken";

    public static final RegisterApi USER_REQUIRED_FIELDS = RegisterApi.builder()
                                                                      .universityId(1L)
                                                                      .instituteId(1L)
                                                                      .departmentId(1L)
                                                                      .build();
    public static final RegisterApi USER = USER_REQUIRED_FIELDS.toBuilder()
                                                               .build();
    public static final RegisterApi STUDENT_REQUIRED_FIELDS = USER_REQUIRED_FIELDS.toBuilder()
                                                                                  .role(Role.STUDENT.getId())
                                                                                  .studyGroupId(1L)
                                                                                  .build();
    public static final RegisterApi STUDENT = USER.toBuilder()
                                                  .studentId("AA00000")
                                                  .role(Role.STUDENT.getId())
                                                  .studyGroupId(1L)
                                                  .build();
    public static final RegisterApi TEACHER_REQUIRED_FIELDS = USER_REQUIRED_FIELDS.toBuilder()
                                                                                  .role(Role.TEACHER.getId())
                                                                                  .scienceDegreeId(1L)
                                                                                  .build();
    public static final RegisterApi TEACHER = USER.toBuilder()
                                                  .role(Role.TEACHER.getId())
                                                  .scienceDegreeId(1L)
                                                  .build();

    public static final UserApi CREATED_USER_REQUIRED_FIELDS = UserApi.builder()
                                                                      .universityId(USER.getUniversityId())
                                                                      .departmentName("departmentName")
                                                                      .instituteName("instituteName")
                                                                      .build();
    public static final UserApi CREATED_USER = CREATED_USER_REQUIRED_FIELDS.toBuilder()
                                                                           .build();

    public static final UserApi CREATED_STUDENT_REQUIRED_FIELDS =
            CREATED_USER_REQUIRED_FIELDS.toBuilder()
                                        .role(STUDENT.getRole())
                                        .studyGroupName("studyGroupName")
                                        .studyGroupId(STUDENT.getStudyGroupId())
                                        .build();
    public static final UserApi CREATED_STUDENT = CREATED_USER.toBuilder()
                                                              .studentId(STUDENT.getStudentId())
                                                              .role(STUDENT.getRole())
                                                              .studyGroupName("studyGroupName")
                                                              .studyGroupId(STUDENT.getStudyGroupId())
                                                              .build();

    public static final UserApi CREATED_TEACHER_REQUIRED_FIELDS =
            CREATED_USER_REQUIRED_FIELDS.toBuilder()
                                        .role(TEACHER.getRole())
                                        .scienceDegreeName("scienceDegreeName")
                                        .build();
    public static final UserApi CREATED_TEACHER = CREATED_USER.toBuilder()
                                                              .role(TEACHER.getRole())
                                                              .scienceDegreeName("scienceDegreeName")
                                                              .build();

    public static final UserApi EXPECTED_STUDENT_REQUIRED_FIELDS =
            CREATED_STUDENT_REQUIRED_FIELDS.toBuilder()
                                           .build();
    public static final UserApi EXPECTED_STUDENT = CREATED_STUDENT.toBuilder()
                                                                  .build();

    public static final UserApi EXPECTED_TEACHER_REQUIRED_FIELDS =
            CREATED_TEACHER_REQUIRED_FIELDS.toBuilder()
                                           .build();
    public static final UserApi EXPECTED_TEACHER = CREATED_TEACHER.toBuilder()
                                                                  .build();
}
