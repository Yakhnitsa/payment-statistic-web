package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentListRepo extends JpaRepository<PaymentList,PaymentListId> {

//    TODO Проверить и протестит, нихрена не работает!!!
    @Override
    @EntityGraph(attributePaths = {"paymentDetailsList"})
    PaymentList getOne(PaymentListId paymentListId);

    Page<PaymentList> findAllByDateBetweenAndPayerCode(Pageable pageable, Date from,Date until,Integer paymentCode);

    Page<PaymentList> findAllByPayerCode(Pageable pageable,Integer paymentCode);

    List<PaymentList> findAllByDateBetweenAndPayerCode(Date from, Date until,Integer paymentCode);

    @Query("select distinct pl.payerCode from PaymentList pl")
    List<String> findAllPaymentCodes();
}
