package com.yurets_y.payment_statistic_web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;

@SpringBootApplication
public class PaymentStatisticWebApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentStatisticWebApplication.class);

    @Value("${spring.profiles.active}")
    private static String profile;

    public static void main(String[] args) {
        LOGGER.info("Application started with '{}' active profile at time {}", profile,  Calendar.getInstance().getTime());
        SpringApplication.run(PaymentStatisticWebApplication.class, args);
    }

}
