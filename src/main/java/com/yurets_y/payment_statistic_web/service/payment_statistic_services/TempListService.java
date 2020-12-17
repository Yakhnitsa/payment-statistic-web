package com.yurets_y.payment_statistic_web.service.payment_statistic_services;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;


public interface TempListService {

    PaymentList putToTempDB(MultipartFile file);

    PaymentList deleteFromTempDB(PaymentList paymentList);

    Collection<PaymentList> getAllFromTempDB();

}
