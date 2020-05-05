package com.yurets_y.payment_statistic_web.controller;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.List;

@TestConfiguration
@Profile("test")
public class ControllerTestConfig {

    @Bean("paymentListServiceRepoBased")
    @Primary
    public PaymentListService testPaymentListService(){
        return new PaymentListService() {
            @Override
            public void add(PaymentList paymentList) {
                System.out.println("add payment list to DB");
            }

            @Override
            public void update(PaymentList paymentList) {
                System.out.println("update payment list from DB");
            }

            @Override
            public boolean remove(PaymentList paymentList) {
                return false;
            }

            @Override
            public PaymentList getById(PaymentListId id) {
                return null;
            }

            @Override
            public List<PaymentList> getAll() {
                return null;
            }

            @Override
            public List<PaymentList> getByPeriod(Date from, Date until) {
                return null;
            }

            @Override
            public boolean contains(PaymentList paymentList) {
                return false;
            }
        };
    }
}
