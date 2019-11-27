package com.educationapp.server.model.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    protected Long id;
    protected String firstName;
    protected String lastName;
    @NonNull
    @NotEmpty
    protected String username;
    @NonNull
    @NotEmpty
    protected String password;
    @NonNull
    @NotEmpty
    protected String passwordConfirm;
    protected String phone;
    @NonNull
    @NotEmpty
    protected String email;
    @NonNull
    @NotEmpty
    protected String role;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  new ArrayList<>();
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
