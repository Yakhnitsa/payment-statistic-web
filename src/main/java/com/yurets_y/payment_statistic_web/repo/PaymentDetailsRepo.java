package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails,Long> {

    List<PaymentDetails> findAllByDateBetween(Date from, Date until);


    @Query("select pd from PaymentDetails pd where " +
            "pd.paymentList.payerCode = :payer_code and " +
            "(:payment_type is null or pd.type = :payment_type) and " +
            "pd.date between :date_from and :date_until")
    List<PaymentDetails> findAllByQuery(
            @Param("payer_code") Integer payerCode,
            @Param("payment_type") String paymentType,
            @Param("date_from") Date dateFrom,
            @Param("date_until") Date dateUntil
//            @Param("station_code") Integer stationCode,
//            @Param("document_number") String docNumber,
//            @Param("payment_sum") Integer paymentSum
    );

    @Query("select pd from PaymentDetails pd where " +
            "pd.paymentList.payerCode = :payer_code and " +
            "(:payment_type is null or pd.type = :payment_type) and " +
            "pd.date between :date_from and :date_until")
    Page<PaymentDetails> findAllByQuery(
            @Param("payer_code") Integer payerCode,
            @Param("payment_type") String paymentType,
            @Param("date_from") Date dateFrom,
            @Param("date_until") Date dateUntil,
            Pageable pageable
//            @Param("station_code") Integer stationCode,
//            @Param("document_number") String docNumber,
//            @Param("payment_sum") Integer paymentSum
    );

    @Query("select distinct pd.type from PaymentDetails pd")
    List<String> findAllPaymentTypes();

}
