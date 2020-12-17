package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails,Long> , JpaSpecificationExecutor<PaymentDetails> {

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
            "(:payment_type = '' or pd.type = :payment_type) and " +
            "pd.date BETWEEN COALESCE(:date_from, pd.date) AND COALESCE(:date_until, pd.date) and " +
//            "pd.date between :date_from and :date_until and " +
            "(:station_code is null or pd.stationCode = :station_code) and " +
            "(:document_number = '' or pd.documentNumber = :document_number) and " +
            "(:payment_sum is null or pd.totalPayment = :payment_sum)"

    )
    Page<PaymentDetails> findAllByQuery(
            @Param("payer_code") Integer payerCode,
            @Param("payment_type") String paymentType,
            @Param("date_from") Date dateFrom,
            @Param("date_until") Date dateUntil,
            Pageable pageable,
            @Param("station_code") Integer stationCode,
            @Param("document_number") String docNumber,
            @Param("payment_sum") Long paymentSum
    );

    @Query("select distinct pd.type from PaymentDetails pd")
    List<String> findAllPaymentTypes();

}
