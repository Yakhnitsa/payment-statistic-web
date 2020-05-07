package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.repo.PaymentDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaymentDetailsService {
    private PaymentDetailsRepo paymentDetailsRepo;

    @Autowired
    public PaymentDetailsService(PaymentDetailsRepo paymentDetailsRepo) {
        this.paymentDetailsRepo = paymentDetailsRepo;
    }


    public List<PaymentDetails> getPaymentDetailsByDate(Date dateFrom, Date dateUntil) {
        return paymentDetailsRepo.findAllByDateBetween(dateFrom,dateUntil);
    }
}
