package com.yurets_y.payment_statistic_web.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_EDITOR,
    ROLE_VIEWER;

    @Override
    public String getAuthority() {
        return name();
    }
}
