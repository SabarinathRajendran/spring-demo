package com.cerclex.epr.authorization.dtos;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@ToString
@RequiredArgsConstructor
public class ApplicationUsers implements UserDetails {

    private final User userObject;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userObject.getRole().getPrivileges().stream().map(privilege -> new SimpleGrantedAuthority(privilege.getPrivilegeName())).collect(Collectors.toSet());

    }

    @Override
    public String getPassword() {
        log.info(String.format("User: %s - Password: %s", userObject.getUsername(), userObject.getPassword()));
        return userObject.getPassword();
    }

    @Override
    public String getUsername() {
        return userObject.getUsername();
    }

    public User getUserDetails(){
        return userObject;
    }

    @Override
    public boolean isAccountNonExpired() {
        return userObject.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userObject.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userObject.isEnabled();
    }
}
