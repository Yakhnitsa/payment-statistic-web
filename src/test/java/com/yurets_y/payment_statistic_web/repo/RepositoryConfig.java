package com.yurets_y.payment_statistic_web.repo;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@EntityScan(basePackages = "com.yurets_y.payment_statistic_web.entity")
@ComponentScan(basePackages = "com.yurets_y.payment_statistic_web.repo")
public class RepositoryConfig {
}
