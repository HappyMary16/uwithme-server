package com.educationapp.server.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;

import com.educationapp.server.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    protected Long id;

    protected String firstName;

    protected String lastName;

    protected String surname;

    @NonNull
    protected String username;

    @NonNull
    protected String password;

    protected String phone;

    protected String email;

    @NonNull
    protected Role role;

    @NonNull
    protected Boolean isAdmin;

    @NonNull
    protected Long universityId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

}
