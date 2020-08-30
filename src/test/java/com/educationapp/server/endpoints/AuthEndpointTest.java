package com.educationapp.server.endpoints;

import static com.educationapp.server.endpoints.AuthEndpointTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.stream.Stream;

import com.educationapp.server.models.api.RegisterApi;
import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.security.JwtTokenProvider;
import com.educationapp.server.services.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    @ParameterizedTest
    @MethodSource
    void register_requiredFields(final RegisterApi userToRegister,
                                 final UserApi createdUser,
                                 final UserApi expectedUser) {
        //GIVEN
        given(tokenProvider.createAuthToken(userToRegister.getEmail())).willReturn(AUTH_TOKEN);
        given(tokenProvider.createRefreshToken(userToRegister.getEmail())).willReturn(REFRESH_TOKEN);
        given(userService.findByUserName(userToRegister.getEmail())).willReturn(createdUser);
        given(userService.save(userToRegister)).willReturn(1L);

        //WHEN
        final ResponseEntity<UserApi> createdStudent = unit.register(userToRegister);

        //THEN
        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
        assertThat(createdStudent.getBody()).isNotNull().isEqualTo(expectedUser);

        verify(tokenProvider, times(1)).createAuthToken(userToRegister.getEmail());
        verify(tokenProvider, times(1)).createRefreshToken(userToRegister.getEmail());
        verify(userService, times(1)).save(userToRegister);
        verify(userService, times(1)).findByUserName(userToRegister.getEmail());
        verifyNoMoreInteractions(tokenProvider, userService);
    }

    @ParameterizedTest
    @MethodSource
    void register_allFieldsSet(final RegisterApi userToRegister,
                               final UserApi createdUser,
                               final UserApi expectedUser) {
        //GIVEN
        given(tokenProvider.createAuthToken(userToRegister.getUsername())).willReturn(AUTH_TOKEN);
        given(tokenProvider.createRefreshToken(userToRegister.getUsername())).willReturn(REFRESH_TOKEN);
        given(userService.findByUserName(userToRegister.getUsername())).willReturn(createdUser);
        given(userService.save(userToRegister)).willReturn(1L);

        //WHEN
        final ResponseEntity<UserApi> createdStudent = unit.register(userToRegister);

        //THEN
        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
        assertThat(createdStudent.getBody()).isNotNull().isEqualTo(expectedUser);

        verify(tokenProvider, times(1)).createAuthToken(userToRegister.getUsername());
        verify(tokenProvider, times(1)).createRefreshToken(userToRegister.getUsername());
        verify(userService, times(1)).save(userToRegister);
        verify(userService, times(1)).findByUserName(userToRegister.getUsername());
        verifyNoMoreInteractions(tokenProvider, userService);
    }

    private static Stream<Arguments> register_requiredFields() {
        return Stream.of(Arguments.of(STUDENT_REQUIRED_FIELDS,
                                      CREATED_STUDENT_REQUIRED_FIELDS,
                                      EXPECTED_STUDENT_REQUIRED_FIELDS),
                         Arguments.of(STUDENT_REQUIRED_FIELDS.toBuilder().username("").build(),
                                      CREATED_STUDENT_REQUIRED_FIELDS,
                                      EXPECTED_STUDENT_REQUIRED_FIELDS),
                         Arguments.of(TEACHER_REQUIRED_FIELDS,
                                      CREATED_TEACHER_REQUIRED_FIELDS,
                                      EXPECTED_TEACHER_REQUIRED_FIELDS),
                         Arguments.of(TEACHER_REQUIRED_FIELDS.toBuilder()
                                                             .username("")
                                                             .build(),
                                      CREATED_TEACHER_REQUIRED_FIELDS,
                                      EXPECTED_TEACHER_REQUIRED_FIELDS));
    }

    private static Stream<Arguments> register_allFieldsSet() {
        return Stream.of(Arguments.of(STUDENT, CREATED_STUDENT, EXPECTED_STUDENT),
                         Arguments.of(TEACHER, CREATED_TEACHER, EXPECTED_TEACHER));
    }
}