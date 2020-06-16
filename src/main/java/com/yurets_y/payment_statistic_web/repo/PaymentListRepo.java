package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<PaymentList> findAllByDateBetween(Pageable pageable, Date from,Date until);

    Page<PaymentList> findAll(Pageable pageable);

    List<PaymentList> findAllByDateBetween(Date from, Date until);

}
