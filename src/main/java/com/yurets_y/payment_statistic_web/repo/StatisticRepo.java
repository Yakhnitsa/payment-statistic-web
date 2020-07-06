package com.yurets_y.payment_statistic_web.repo;

import com.yurets_y.payment_statistic_web.dto.DateLongEntry;
import com.yurets_y.payment_statistic_web.dto.DateStringLongDto;
import com.yurets_y.payment_statistic_web.dto.StringLongEntry;
import com.yurets_y.payment_statistic_web.dto.YearStatisticDtoEntry;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StatisticRepo extends org.springframework.data.repository.Repository<PaymentList,Long> {

    @Query("select new com.yurets_y.payment_statistic_web.dto.DateLongEntry(pl.date, pl.paymentVsTaxes) " +
            "from PaymentList pl where pl.date between :date_from and :date_until")
    List<DateLongEntry> getChartExpensesStatistic(@Param("date_from") Date dateFrom,
                                                  @Param("date_until") Date dateUntil);

    @Query("select new com.yurets_y.payment_statistic_web.dto.DateLongEntry(pd.date, sum(pd.totalPayment)) " +
            "from PaymentDetails pd where pd.date between :date_from and :date_until and pd.type = :payment_type " +
            "group by pd.date")
    List<DateLongEntry> getChartStatisticByPaymentType(@Param("date_from") Date dateFrom,
                                                       @Param("date_until") Date dateUntil,
                                                       @Param("payment_type") String type);

    @Query("select new com.yurets_y.payment_statistic_web.dto.StringLongEntry(pd.type, sum(pd.totalPayment)) " +
            "from PaymentDetails pd where pd.date between :date_from and :date_until " +
            "group by pd.type")
    List<StringLongEntry> getChartStatisticGroupByType(@Param("date_from") Date dateFrom,
                                                       @Param("date_until") Date dateUntil);

    @Query("select new com.yurets_y.payment_statistic_web.dto.StringLongEntry(pd.stationName, sum(pd.totalPayment)) " +
            "from PaymentDetails pd where pd.date between :date_from and :date_until " +
            "group by pd.stationName")
    List<StringLongEntry> getChartStatisticGroupByStation(@Param("date_from") Date dateFrom,
                                                          @Param("date_until") Date dateUntil);


    @Query("select new com.yurets_y.payment_statistic_web.dto.DateStringLongDto(pd.date, pd.type, sum(pd.totalPayment)) " +
            "from PaymentDetails pd " +
            "where pd.date between :date_from and :date_until " +
            "group by pd.date, pd.type")
    List<DateStringLongDto> getDailyStatisticByType(@Param("date_from") Date dateFrom,
                                                    @Param("date_until") Date dateUntil);


    @Query("select new com.yurets_y.payment_statistic_web.dto.YearStatisticDtoEntry( " +
                "function('YEAR',pl.date), function('MONTH',pl.date),1, sum(pl.paymentVsTaxes)" +
            ") from PaymentList pl " +
            "where pl.date between :date_from and :date_until " +
            "group by function('YEAR',pl.date), function('MONTH',pl.date)")
    List<YearStatisticDtoEntry> getYearExpensesStatisticGroupByMonth(@Param("date_from") Date dateFrom,
                                                             @Param("date_until") Date dateUntil);


    @Query("select new com.yurets_y.payment_statistic_web.dto.DateStringLongDto(" +
            "function('YEAR',pd.date), function('MONTH',pd.date),1 ,pd.type, sum(pd.totalPayment))" +
            "FROM PaymentDetails pd " +
            "where pd.date between :date_from and :date_until " +
            "group by function('YEAR',pd.date), function('MONTH',pd.date), pd.type")
    List<DateStringLongDto> getYearStatisticGroupByMonthAndType(@Param("date_from") Date dateFrom,
                                                                @Param("date_until") Date dateUntil);



}
