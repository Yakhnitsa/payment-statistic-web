package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public interface PaymentListService {
    void add(PaymentList paymentList);

    void update(PaymentList paymentList);

    boolean remove(PaymentList paymentList);

    PaymentList getById(PaymentListId id);

    List<PaymentList> getAll();

    List<PaymentList> getByPeriod(Date from, Date until);

    boolean contains(PaymentList paymentList);

    Resource getFileAsResourse(String filename) throws FileNotFoundException;
}
