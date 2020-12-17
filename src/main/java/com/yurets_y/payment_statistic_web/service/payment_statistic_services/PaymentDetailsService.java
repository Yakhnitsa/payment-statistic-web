package com.yurets_y.payment_statistic_web.service.payment_statistic_services;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;

public interface PaymentDetailsService {
    Page<PaymentDetails> getAllBySpecification(Specification<PaymentDetails> spec, Pageable pageable);

    List<String> getAllPaymentTypes();

    Page<PaymentDetails> getAllWithParameters(Integer payerCode, String paymentType, Date dateFrom, Date dateUntil,
                                              Pageable page, Integer stationCode, String docNumber, Float paymentSum);
}
