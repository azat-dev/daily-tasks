package com.azatdev.dailytasks.presentation.security.services;

import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.util.Assert;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private String token;
    private Object principal;

    private JWTAuthenticationToken(String token) {
        super(Collections.emptyList());
        Assert.hasText(
            token,
            "token cannot be empty"
        );
        this.token = token;
        this.setAuthenticated(false);
    }

    private JWTAuthenticationToken(
        Object principal,
        String token
    ) {
        super(Collections.emptyList());
        Assert.notNull(
            principal,
            "principal cannot be null"
        );
        Assert.hasText(
            token,
            "token cannot be empty"
        );
        this.principal = principal;
        this.token = token;
        super.setAuthenticated(true);
    }

    public static JWTAuthenticationToken authenticated(
        Object principal,
        String token
    ) {
        return new JWTAuthenticationToken(
            principal,
            token
        );
    }

    public static JWTAuthenticationToken unauthenticated(String token) {
        return new JWTAuthenticationToken(token);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(
            !isAuthenticated,
            "Cannot set this token to trusted - use a static authenticated method instead"
        );
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }
}
