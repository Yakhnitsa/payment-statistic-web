package com.yurets_y.payment_statistic_web.service.payment_statistic_services;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.repo.PaymentDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    private PaymentDetailsRepo paymentDetailsRepo;

    @Autowired
    public PaymentDetailsServiceImpl(PaymentDetailsRepo paymentDetailsRepo) {
        this.paymentDetailsRepo = paymentDetailsRepo;
    }

    @Override
    public Page<PaymentDetails> getAllBySpecification(Specification<PaymentDetails> spec, Pageable pageable) {
        return paymentDetailsRepo.findAll( spec , pageable);
    }

    @Override
    public List<String> getAllPaymentTypes() {
        return paymentDetailsRepo.findAllPaymentTypes();
    }

    @Override
    public Page<PaymentDetails> getAllWithParameters(Integer payerCode, String paymentType, Date dateFrom, Date dateUntil,
                                                     Pageable page, Integer stationCode, String docNumber, Float paymentSum) {
        Long paymentSumInt = null;
        if(paymentSum != null){
            paymentSumInt = (long)(paymentSum * 100);
        }
        return paymentDetailsRepo.findAllByQuery(payerCode,paymentType,dateFrom,dateUntil,page,stationCode,docNumber,paymentSumInt);
    }
}
