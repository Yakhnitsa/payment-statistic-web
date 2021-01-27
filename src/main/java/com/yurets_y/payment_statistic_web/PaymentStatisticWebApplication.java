package com.yurets_y.payment_statistic_web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.TimeZone;

@SpringBootApplication
public class PaymentStatisticWebApplication {

    private final Logger logger = LoggerFactory.getLogger(PaymentStatisticWebApplication.class);

    @Value("${spring.profiles.active:'production'}")
    private String profile;

    @Value("${application.timezone:'UTC'}")
    private String timezone;

    public static void main(String[] args) {

        SpringApplication.run(PaymentStatisticWebApplication.class, args);
    }

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        logger.info("Application started with '{}' active profile at time {}", profile,  Calendar.getInstance().getTime());
        logger.info("Timezone is set to '{}'", timezone);

    }

}
