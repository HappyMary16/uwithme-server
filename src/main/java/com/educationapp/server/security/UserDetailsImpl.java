package com.educationapp.server.security;

import java.util.Collection;
import java.util.HashSet;

import com.educationapp.server.enums.Role;
import com.educationapp.server.models.persistence.UserDB;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class UserDetailsImpl  implements UserDetails  {

    private static final long serialVersionUID = 4186644860219354968L;

    private final UserDB user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(Role.getById(user.getRole()).name()));
        if (user.getIsAdmin()) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
