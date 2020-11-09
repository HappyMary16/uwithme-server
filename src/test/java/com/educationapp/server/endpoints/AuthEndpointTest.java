package com.educationapp.server.endpoints;

import static com.educationapp.server.endpoints.AuthEndpointTestData.*;

import java.util.stream.Stream;

import com.educationapp.server.services.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthEndpointTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthEndpoint unit;

//    @ParameterizedTest
//    @MethodSource
//    void register_requiredFields(final RegisterApi userToRegister,
//                                 final UserApi createdUser,
//                                 final UserApi expectedUser) {
//        //GIVEN
//        given(userService.save(userToRegister)).willReturn(1L);
//
//        //WHEN
//        final ResponseEntity<?> createdStudent = unit.register(userToRegister);
//
//        //THEN
//        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
//        assertThat(createdStudent.getBody()).isNotNull().isEqualTo(expectedUser);
//
//        verify(userService, times(1)).save(userToRegister);
//    }

//    @ParameterizedTest
//    @MethodSource
//    void register_allFieldsSet(final RegisterApi userToRegister,
//                               final UserApi createdUser,
//                               final UserApi expectedUser) {
//        //GIVEN
//        given(userService.save(userToRegister)).willReturn(1L);
//
//        //WHEN
//        final ResponseEntity<?> createdStudent = unit.register(userToRegister);
//
//        //THEN
//        assertThat(createdStudent.getStatusCodeValue()).isEqualTo(200);
//        assertThat(createdStudent.getBody()).isNotNull().isEqualTo(expectedUser);
//
//        verify(userService, times(1)).save(userToRegister);
//    }

    private static Stream<Arguments> register_requiredFields() {
        return Stream.of(Arguments.of(STUDENT_REQUIRED_FIELDS,
                                      CREATED_STUDENT_REQUIRED_FIELDS,
                                      EXPECTED_STUDENT_REQUIRED_FIELDS),
                         Arguments.of(TEACHER_REQUIRED_FIELDS,
                                      CREATED_TEACHER_REQUIRED_FIELDS,
                                      EXPECTED_TEACHER_REQUIRED_FIELDS));
    }

    private static Stream<Arguments> register_allFieldsSet() {
        return Stream.of(Arguments.of(STUDENT, CREATED_STUDENT, EXPECTED_STUDENT),
                         Arguments.of(TEACHER, CREATED_TEACHER, EXPECTED_TEACHER));
    }
}