package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.exceptions.EmptyFieldsException;
import com.mborodin.uwm.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthEndpoint {

    private final UserService userService;

    @GetMapping("/signIn")
    public UserApi authenticateUser() {
        return userService.getUserApi();
    }

    @PostMapping("/signUp")
    public UserApi register(@RequestBody final RegisterApi registerApi) {
        validateRegistrationApiValid(registerApi);

        userService.save(registerApi);
        return userService.getUserApi();
    }

    private void validateRegistrationApiValid(final RegisterApi registerApi) {
        final Integer role = registerApi.getRole();
        if (role == null) {
            throw new EmptyFieldsException(getLanguages());
        }

        if (role == 3 && StringUtils.isEmpty(registerApi.getUniversityName())) {
            throw new EmptyFieldsException(getLanguages());
        }

        if (role != 3) {
            boolean result = registerApi.getUniversityId() != null;
            result &= registerApi.getInstituteId() != null;
            result &= registerApi.getDepartmentId() != null;

            if (result) {
                return;
            }
            throw new EmptyFieldsException(getLanguages());
        }
    }
}
