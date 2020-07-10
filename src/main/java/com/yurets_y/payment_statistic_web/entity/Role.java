package com.yurets_y.payment_statistic_web.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    FINANCE,
    COUNTER,
    OTHER;

    @Override
    public String getAuthority() {
        return name();
    }
}
