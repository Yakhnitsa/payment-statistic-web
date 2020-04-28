package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails,Long> {

    List<PaymentDetails> findAllByDateBetween(Date from, Date until);

}
