package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.dto.*;
import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.repo.PaymentDetailsRepo;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import com.yurets_y.payment_statistic_web.repo.StatisticRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {

    private PaymentListRepo paymentListRepo;

    private StatisticRepo statisticRepo;

    private Comparator<String> paymentTypeComparator;

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM");

    @Autowired
    public StatisticServiceImpl(PaymentListRepo paymentListRepo,
                                Comparator<String> paymentTypeComparator,
                                StatisticRepo statisticRepo

    ) {
        this.paymentListRepo = paymentListRepo;
        this.paymentTypeComparator = paymentTypeComparator;
        this.statisticRepo = statisticRepo;
    }

    @Override
    public DailyStatisticDto getDailyStatistic(Date dateFrom, Date dateUntil) {
        List<Date> dates = getDatesArray(dateFrom, dateUntil);
        List<PaymentList> payments = paymentListRepo.findAllByDateBetween(dateFrom,dateUntil);
        List<DateStringLongDto> details = statisticRepo.getDailyStatisticByType(dateFrom,dateUntil);
//        TODO - Вставить сортировку
        Map<String, Map<Date,Long>> detailsMap = details
                .stream()
                .collect(Collectors.groupingBy(DateStringLongDto::getType,
                        Collectors.toMap(DateStringLongDto::getDate,DateStringLongDto::getValue)));

        TreeMap<String, Map<Date,Long>> sortedDetails = new TreeMap<>(paymentTypeComparator);
        sortedDetails.putAll(detailsMap);

        return new DailyStatisticDto(dates,payments,sortedDetails);

    }

    @Override
    public ChartDto getChartStatistic(Date dateFrom, Date dateUntil,Integer averageIndex) {
        List<Date> dates = getDatesArray(dateFrom, dateUntil);

        List<DateLongEntry> expenses = statisticRepo.getChartExpensesStatistic(dateFrom,dateUntil);

        String paymentType = "Платіжні доручення";

        List<DateLongEntry> payments = statisticRepo.getChartStatisticByPaymentType(dateFrom,dateUntil,paymentType);

        List<StringLongEntry> typeChartData = statisticRepo.getChartStatisticByType(dateFrom,dateUntil)
                .stream()
                .filter(entity -> !entity.getType().equals(paymentType))
                .collect(Collectors.toList());

        List<StringLongEntry> stationChartData = statisticRepo.getChartStatisticByStation(dateFrom,dateUntil)
                .stream()
                .filter(entity -> entity.getType() != null)
                .collect(Collectors.toList());

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
                typeChartData,
                stationChartData,
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

    private List<Long> fillBlankDatesAndGetList(List<Date> dates, List<DateLongEntry> values) {
        Map<Date,Long> valuesMap = values
                .stream()
                .collect(Collectors.toMap(DateLongEntry::getDate,DateLongEntry::getValue));

        List<Long> dataList = new ArrayList<>(dates.size());

        dates.forEach(date ->{
            Long data = valuesMap.get(date);
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
