package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentListRepo extends JpaRepository<PaymentList,PaymentListId> {

    @Override
    @EntityGraph(attributePaths = {"paymentDetailsList"})
    PaymentList getOne(PaymentListId paymentListId);

    List<PaymentList> getAllByDateBetween(Date from,Date until);
}
