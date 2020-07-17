package com.yurets_y.payment_statistic_web.config;


import com.yurets_y.payment_statistic_web.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareBean")
public class AuditConfig {

    @Bean
    public AuditorAware<User> auditorAwareBean(){
        return new AuditorAware<User>() {
            @Override
            public Optional<User> getCurrentAuditor() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null || !authentication.isAuthenticated()) {
                    Optional.of(null);
                }
                return Optional.of((User)authentication.getPrincipal());
            }
        };
    }
}
