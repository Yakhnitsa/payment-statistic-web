package com.yurets_y.payment_statistic_web.service.payment_statistic_services;

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

    Page<PaymentList> getAll(Pageable pageable, Integer paymentCode);

    List<String> getPaymentCodes();

    Page<PaymentList> getPageByPeriod(Pageable pageable, Date from, Date until, Integer paymentCode);

    List<PaymentList> getByPeriod(Date from, Date until, Integer paymentCode);

    boolean contains(PaymentList paymentList);

    Resource getFileAsResource(String filename) throws FileNotFoundException;

    Resource getFilesArchiveAsResource(Date dateFrom, Date dateUntil, Integer paymentCode);
}
