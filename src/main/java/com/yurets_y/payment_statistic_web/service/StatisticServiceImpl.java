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
    public ChartDto getChartStatistic(Date dateFrom, Date dateUntil,Integer averageIndex) {
        List<Date> dates = getDatesArray(dateFrom, dateUntil);

        Map<Date,Long> expenses = paymentListRepo.findAllByDateBetween(dateFrom,dateUntil)
                .stream()
                .collect(Collectors.groupingBy(PaymentList::getDate,
                        LinkedHashMap::new,
                        Collectors.summingLong(PaymentList::getPaymentVsTaxes)
                ));

        Map<Date,Long> payments = paymentDetailsRepo.findAllByDateBetween(dateFrom,dateUntil)
                .stream()
                .filter(details -> details.getType().equals("Платіжні доручення"))
                .collect(Collectors.groupingBy(PaymentDetails::getDate,Collectors.summingLong(PaymentDetails::getTotalPayment)));


        List<Long> expensesList = fillBlankDatesAndGetList(dates,expenses);
        List<Long> paymentsList = fillBlankDatesAndGetList(dates,payments);
        List<Long> averageStatistic = getAverageStatistic(expensesList,averageIndex);
        List<String> datesList = dates
                .stream()
                .map(date -> DATE_FORMAT.format(date))
                .collect(Collectors.toList());
        return new ChartDto(
                datesList,
                paymentsList,
                expensesList,
                averageStatistic,
                averageIndex
        );
    }

    private List<Long> getAverageStatistic(List<Long> list, Integer averageIndex) {
        List<Long> averageList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            // Интексы микромассива
            int startIndex = i - averageIndex/2;
            int endIndex = startIndex + averageIndex;
            //Корректировка индексов в пределах массива
            startIndex = startIndex < 0 ? 0: startIndex;
            endIndex = endIndex > list.size() ? list.size(): endIndex;

            Double average = list.subList(startIndex,endIndex)
                    .stream()
                    .mapToLong(val->val)
                    .average()
                    .orElse(0.0);
            averageList.add(average.longValue());

        }
        return averageList;
    }

    private List<Long> fillBlankDatesAndGetList(List<Date> dates, Map<Date,Long> map) {
        List<Long> dataList = new ArrayList<>(dates.size());
        dates.forEach(date ->{
            Long data = map.get(date);
            dataList.add(data == null? 0L : data);

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

    private Date getClearDate(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return new GregorianCalendar(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
                ).getTime();
    }
}
