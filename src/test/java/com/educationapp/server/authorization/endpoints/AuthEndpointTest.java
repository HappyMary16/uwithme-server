package com.educationapp.server.authorization.endpoints;

import static com.educationapp.server.authorization.endpoints.AuthEndpointTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.educationapp.server.authorization.security.JwtTokenProvider;
import com.educationapp.server.common.api.RegisterApi;
import com.educationapp.server.common.api.UserApi;
import com.educationapp.server.users.servises.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthEndpointTest {

    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthEndpoint unit;

    @Test
    void register_studentRequiredFields() {
        //GIVEN
        given(tokenProvider.createAuthToken(STUDENT_REQUIRED_FIELDS.getEmail())).willReturn(AUTH_TOKEN);
        given(tokenProvider.createRefreshToken(
                STUDENT_REQUIRED_FIELDS.getEmail())).willReturn(REFRESH_TOKEN);
        given(userService.findByUserName(STUDENT_REQUIRED_FIELDS.getEmail())).willReturn(
                CREATED_STUDENT_REQUIRED_FIELDS);
        given(userService.save(STUDENT_REQUIRED_FIELDS)).willReturn(1L);

        //WHEN
        final ResponseEntity<UserApi> createdStudent = unit.register(STUDENT_REQUIRED_FIELDS);

        //THEN
        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
        assertThat(createdStudent.getBody()).isNotNull()
                                            .isEqualTo(EXPECTED_STUDENT_REQUIRED_FIELDS);

        verify(tokenProvider, times(1)).createAuthToken(STUDENT_REQUIRED_FIELDS.getEmail());
        verify(tokenProvider, times(1)).createRefreshToken(STUDENT_REQUIRED_FIELDS.getEmail());
        verify(userService, times(1)).save(STUDENT_REQUIRED_FIELDS);
        verify(userService, times(1)).findByUserName(STUDENT_REQUIRED_FIELDS.getEmail());
        verifyNoMoreInteractions(tokenProvider, userService);
    }

    @Test
    void register_studentUsernameIsEmptyString() {
        //GIVEN
        final RegisterApi student = STUDENT_REQUIRED_FIELDS.toBuilder()
                                                           .username("")
                                                           .build();
        given(tokenProvider.createAuthToken(STUDENT_REQUIRED_FIELDS.getEmail())).willReturn(AUTH_TOKEN);
        given(tokenProvider.createRefreshToken(
                STUDENT_REQUIRED_FIELDS.getEmail())).willReturn(REFRESH_TOKEN);
        given(userService.findByUserName(STUDENT_REQUIRED_FIELDS.getEmail())).willReturn(
                CREATED_STUDENT_REQUIRED_FIELDS);
        given(userService.save(student)).willReturn(1L);

        //WHEN
        final ResponseEntity<UserApi> createdStudent = unit.register(student);

        //THEN
        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
        assertThat(createdStudent.getBody()).isNotNull()
                                            .isEqualTo(EXPECTED_STUDENT_REQUIRED_FIELDS);

        verify(tokenProvider, times(1)).createAuthToken(STUDENT_REQUIRED_FIELDS.getEmail());
        verify(tokenProvider, times(1)).createRefreshToken(STUDENT_REQUIRED_FIELDS.getEmail());
        verify(userService, times(1)).save(student);
        verify(userService, times(1)).findByUserName(STUDENT_REQUIRED_FIELDS.getEmail());
        verifyNoMoreInteractions(tokenProvider, userService);
    }

    @Test
    void register_studentAllFieldsSet() {
        //GIVEN
        given(tokenProvider.createAuthToken(STUDENT.getUsername())).willReturn(AUTH_TOKEN);
        given(tokenProvider.createRefreshToken(STUDENT.getUsername()))
                .willReturn(REFRESH_TOKEN);
        given(userService.findByUserName(STUDENT.getUsername()))
                .willReturn(CREATED_STUDENT);
        given(userService.save(STUDENT)).willReturn(1L);

        //WHEN
        final ResponseEntity<UserApi> createdStudent = unit.register(STUDENT);

        //THEN
        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
        assertThat(createdStudent.getBody()).isNotNull()
                                            .isEqualTo(EXPECTED_STUDENT);

        verify(tokenProvider, times(1)).createAuthToken(STUDENT.getUsername());
        verify(tokenProvider, times(1)).createRefreshToken(STUDENT.getUsername());
        verify(userService, times(1)).save(STUDENT);
        verify(userService, times(1)).findByUserName(STUDENT.getUsername());
        verifyNoMoreInteractions(tokenProvider, userService);
    }

    @Test
    void register_teacherRequiredFields() {
        //GIVEN
        given(tokenProvider.createAuthToken(TEACHER_REQUIRED_FIELDS.getEmail())).willReturn(AUTH_TOKEN);
        given(tokenProvider.createRefreshToken(
                TEACHER_REQUIRED_FIELDS.getEmail())).willReturn(REFRESH_TOKEN);
        given(userService.findByUserName(TEACHER_REQUIRED_FIELDS.getEmail())).willReturn(
                CREATED_TEACHER_REQUIRED_FIELDS);
        given(userService.save(TEACHER_REQUIRED_FIELDS)).willReturn(1L);

        //WHEN
        final ResponseEntity<UserApi> createdStudent = unit.register(TEACHER_REQUIRED_FIELDS);

        //THEN
        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
        assertThat(createdStudent.getBody()).isNotNull()
                                            .isEqualTo(EXPECTED_TEACHER_REQUIRED_FIELDS);

        verify(tokenProvider, times(1)).createAuthToken(TEACHER_REQUIRED_FIELDS.getEmail());
        verify(tokenProvider, times(1)).createRefreshToken(TEACHER_REQUIRED_FIELDS.getEmail());
        verify(userService, times(1)).save(TEACHER_REQUIRED_FIELDS);
        verify(userService, times(1)).findByUserName(TEACHER_REQUIRED_FIELDS.getEmail());
        verifyNoMoreInteractions(tokenProvider, userService);
    }

    @Test
    void register_teacherUsernameIsEmptyString() {
        //GIVEN
        final RegisterApi teacher = TEACHER_REQUIRED_FIELDS.toBuilder()
                                                           .username("")
                                                           .build();
        given(tokenProvider.createAuthToken(TEACHER_REQUIRED_FIELDS.getEmail())).willReturn(AUTH_TOKEN);
        given(tokenProvider.createRefreshToken(
                TEACHER_REQUIRED_FIELDS.getEmail())).willReturn(REFRESH_TOKEN);
        given(userService.findByUserName(TEACHER_REQUIRED_FIELDS.getEmail())).willReturn(
                CREATED_TEACHER_REQUIRED_FIELDS);
        given(userService.save(teacher)).willReturn(1L);

        //WHEN
        final ResponseEntity<UserApi> createdStudent = unit.register(teacher);

        //THEN
        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
        assertThat(createdStudent.getBody()).isNotNull()
                                            .isEqualTo(EXPECTED_TEACHER_REQUIRED_FIELDS);

        verify(tokenProvider, times(1)).createAuthToken(TEACHER_REQUIRED_FIELDS.getEmail());
        verify(tokenProvider, times(1)).createRefreshToken(TEACHER_REQUIRED_FIELDS.getEmail());
        verify(userService, times(1)).save(teacher);
        verify(userService, times(1)).findByUserName(TEACHER_REQUIRED_FIELDS.getEmail());
        verifyNoMoreInteractions(tokenProvider, userService);
    }

    @Test
    void register_teacherAllFieldsSet() {
        //GIVEN
        given(tokenProvider.createAuthToken(TEACHER.getUsername())).willReturn(AUTH_TOKEN);
        given(tokenProvider.createRefreshToken(TEACHER.getUsername()))
                .willReturn(REFRESH_TOKEN);
        given(userService.findByUserName(TEACHER.getUsername()))
                .willReturn(CREATED_TEACHER);
        given(userService.save(TEACHER)).willReturn(1L);

        //WHEN
        final ResponseEntity<UserApi> createdStudent = unit.register(TEACHER);

        //THEN
        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
        assertThat(createdStudent.getBody()).isNotNull()
                                            .isEqualTo(EXPECTED_TEACHER);

        verify(tokenProvider, times(1)).createAuthToken(TEACHER.getUsername());
        verify(tokenProvider, times(1)).createRefreshToken(TEACHER.getUsername());
        verify(userService, times(1)).save(TEACHER);
        verify(userService, times(1)).findByUserName(TEACHER.getUsername());
        verifyNoMoreInteractions(tokenProvider, userService);
    }
}