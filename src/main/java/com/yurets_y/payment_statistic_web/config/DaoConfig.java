package com.yurets_y.payment_statistic_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class DaoConfig {

    @Bean(name={"defaultEMF", "entityManagerFactory"})
    public EntityManagerFactory defaultEMF(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("basic-persistence");
        return emf;
    }

}
