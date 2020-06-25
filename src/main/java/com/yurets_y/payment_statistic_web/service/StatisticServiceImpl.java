package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.dto.ChartDto;
import com.yurets_y.payment_statistic_web.dto.DailyStatisticDto;
import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.repo.PaymentDetailsRepo;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {

    private PaymentListRepo paymentListRepo;

    private PaymentDetailsRepo paymentDetailsRepo;

    private Comparator<PaymentDetails> paymentDetailsComparator;

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public StatisticServiceImpl(PaymentListRepo paymentListRepo,
                                PaymentDetailsRepo paymentDetailsRepo,
                                Comparator<PaymentDetails> paymentDetailsComparator) {
        this.paymentListRepo = paymentListRepo;
        this.paymentDetailsRepo = paymentDetailsRepo;
        this.paymentDetailsComparator = paymentDetailsComparator;
    }

    @Override
    public DailyStatisticDto getDailyStatistic(Date dateFrom, Date dateUntil) {
        List<PaymentList> payments = paymentListRepo.findAllByDateBetween(dateFrom,dateUntil);
        DailyStatisticDto dto = new DailyStatisticDto();
        dto.setPayments(payments);

        List<PaymentDetails> paymentDetails = paymentDetailsRepo.findAllByDateBetween(dateFrom,dateUntil);
        Collections.sort(paymentDetails, paymentDetailsComparator);

        Map<String, Map<Date, Long>> details = paymentDetails
                .stream()
                .collect(Collectors.groupingBy(PaymentDetails::getType,
                        LinkedHashMap::new,
                        Collectors.groupingBy(
                                PaymentDetails::getDate,
                                TreeMap::new,
                                Collectors.summingLong(PaymentDetails::getTotalPayment))
                ));

        Map<String, Map<String, Long>> reformattedDetails = details
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        innerEntry -> DATE_FORMAT.format(innerEntry.getKey()),
                                        Map.Entry::getValue
                                )),
                        (first, second) -> first,
                        LinkedHashMap::new
                ));

        dto.setDetails(reformattedDetails);

        return dto;
    }

    @Override
    public ChartDto getChartStatistic(Date dateFrom, Date dateUntil) {
        List<Date> dates = getDatesArray(dateFrom, dateUntil);
        Map<Date,Long> expenses = paymentListRepo.findAllByDateBetween(dateFrom,dateUntil)
                .stream()
                .collect(Collectors.groupingBy(PaymentList::getDate,
                        LinkedHashMap::new,
                        Collectors.summingLong(PaymentList::getPaymentVsTaxes)
                ));

        List<Long> expensesList = fillBlankDatesAndGetList(dates,expenses);
        return null;
    }

    private List<Long> fillBlankDatesAndGetList(List<Date> dates, Map<Date,Long> expenses) {
        List<Long> dataList = new ArrayList<>(dates.size());
        dates.forEach(date ->{
            dataList.add(expenses.get(date));

        });
        return dataList;
    }

    private List<Date> getDatesArray(Date dateFrom, Date dateUntil) {
        List<Date> dates = new ArrayList<>();

        dateFrom = getClearDate(dateFrom);
        dateUntil = getClearDate(dateUntil);

        GregorianCalendar instance = new GregorianCalendar();
        instance.setTime(dateFrom);

        while(instance.getTime().before(dateUntil)){
            dates.add(instance.getTime());
            instance.add(Calendar.DATE,1);
        }
        dates.add(dateUntil);
        return dates;
    }

    private Date getClearDate(Date dateFrom) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateFrom);
        return new GregorianCalendar(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
                ).getTime();
    }
}
