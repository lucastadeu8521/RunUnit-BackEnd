package com.rununit.rununit.infrastructure.security;

import com.rununit.rununit.domain.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AuthUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(User user) {
        this.id = user.getId();
        this.username = user.getLogin().getEmail();
        this.password = user.getLogin().getPasswordHash();

        // As roles devem ser prefixadas com "ROLE_" para serem reconhecidas pelo hasRole/hasAnyRole.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name());
        this.authorities = Collections.singletonList(authority);
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true; // Use o status 'active' real do seu modelo User se aplicável
    }
}
