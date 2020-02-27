package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



public interface TempListService {

    PaymentList putToTempDB(MultipartFile file);

    PaymentList saveToMainDB(PaymentList paymentList);

    List<PaymentList> getAllFromTempDB();

}
