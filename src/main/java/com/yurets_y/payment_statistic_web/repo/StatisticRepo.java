package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.dto.DateLongEntry;
import com.yurets_y.payment_statistic_web.dto.StringLongEntry;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StatisticRepo extends org.springframework.data.repository.Repository<PaymentList,Long> {

    @Query("SELECT new com.yurets_y.payment_statistic_web.dto.DateLongEntry(pl.date, pl.paymentVsTaxes) " +
            "FROM PaymentList pl WHERE pl.date BETWEEN :date_from AND :date_until")
    List<DateLongEntry> getDailyExpensesStatistic(@Param("date_from") Date dateFrom,
                                   @Param("date_until") Date dateUntil);


    @Query("SELECT new com.yurets_y.payment_statistic_web.dto.DateLongEntry(pd.date, SUM(pd.totalPayment)) " +
            "FROM PaymentDetails pd WHERE pd.date BETWEEN :date_from AND :date_until AND pd.type = :payment_type " +
            "GROUP BY pd.date")
    List<DateLongEntry> getDailyStatisticByPaymentType(@Param("date_from") Date dateFrom,
                                                       @Param("date_until") Date dateUntil,
                                                       @Param("payment_type") String type);

    @Query("SELECT new com.yurets_y.payment_statistic_web.dto.StringLongEntry(pd.type, SUM(pd.totalPayment)) " +
            "FROM PaymentDetails pd WHERE pd.date BETWEEN :date_from AND :date_until " +
            "GROUP BY pd.type")
    List<StringLongEntry> getDailyStatisticByType(@Param("date_from") Date dateFrom,
                                                  @Param("date_until") Date dateUntil);

    @Query("SELECT new com.yurets_y.payment_statistic_web.dto.StringLongEntry(pd.stationName, SUM(pd.totalPayment)) " +
            "FROM PaymentDetails pd WHERE pd.date BETWEEN :date_from AND :date_until " +
            "GROUP BY pd.stationName")
    List<StringLongEntry> getDailyStatisticByStation(@Param("date_from") Date dateFrom,
                                                  @Param("date_until") Date dateUntil);
}
