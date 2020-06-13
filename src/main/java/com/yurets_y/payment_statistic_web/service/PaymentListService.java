package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.dto.PaymentListDto;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public interface PaymentListService {
    void add(PaymentList paymentList);

    void update(PaymentList paymentList);

    boolean remove(PaymentList paymentList);

    PaymentList getById(PaymentListId id);

    Page<PaymentList> getAll(Pageable pageable);

    Page<PaymentList> getPageByPeriod(Pageable pageable, Date from, Date until);

    List<PaymentList> getByPeriod(Date from, Date until);

    boolean contains(PaymentList paymentList);

    Resource getFileAsResource(String filename) throws FileNotFoundException;
}
