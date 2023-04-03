package org.sopt.makers.operation.security.jwt;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AdminAuthentication extends UsernamePasswordAuthenticationToken {
    public AdminAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AdminAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}