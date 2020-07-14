package com.yurets_y.payment_statistic_web.controller;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
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
            public List<String> getPaymentCodes() {
                return null;
            }

            @Override
            public void add(PaymentList paymentList) {

            }

            @Override
            public void update(PaymentList paymentList) {

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
            public Page<PaymentList> getAll(Pageable pageable, Integer payerCode) {
                return null;
            }

            @Override
            public Page<PaymentList> getPageByPeriod(Pageable pageable, Date from, Date until, Integer paymentCode) {
                return null;
            }

            @Override
            public List<PaymentList> getByPeriod(Date from, Date until, Integer payerCode) {
                return null;
            }

            @Override
            public boolean contains(PaymentList paymentList) {
                return false;
            }

            @Override
            public Resource getFileAsResource(String filename) throws FileNotFoundException {
                return null;
            }

            @Override
            public Resource getFilesArchiveAsResource(Date dateFrom, Date dateUntil, Integer payerCode) {
                return null;
            }
        };
    }
}
