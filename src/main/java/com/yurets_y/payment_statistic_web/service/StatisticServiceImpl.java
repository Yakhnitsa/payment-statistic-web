package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.dto.*;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import com.yurets_y.payment_statistic_web.repo.StatisticRepo;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
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

    private final String PAYMENT_TYPE =  "Платіжні доручення";

    private MessageProvider messageProvider;

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
    public DailyStatisticDto getDailyStatistic(Date dateFrom, Date dateUntil, Integer payerCode) {
        List<Date> dates = getDatesArray(dateFrom, dateUntil);
        List<PaymentList> payments = paymentListRepo.findAllByDateBetweenAndPayerCode(dateFrom,dateUntil,payerCode);

        Map<String, Map<Date,Long>> paymentsMap = getMapFromPaymentList(payments);

        List<DateStringLongEntry> details = statisticRepo.getDailyStatisticByPaymentCodeGroupByType(dateFrom,dateUntil,payerCode);
        List<DateStringLongEntry> expByStations = statisticRepo.getDailyStatisticByPaymentCodeGroupByStation(dateFrom,dateUntil,payerCode);

        Map<String, Map<Date,Long>> detailsMap = details
                .stream()
                .collect(Collectors.groupingBy(DateStringLongEntry::getType,
                        Collectors.toMap(DateStringLongEntry::getDate,DateStringLongEntry::getValue)));

        Map<String, Map<Date,Long>> stationsMap = expByStations
                .stream()
                .filter(dto -> dto.getType() != null)
                .collect(Collectors.groupingBy(DateStringLongEntry::getType,
                        TreeMap::new,
                        Collectors.toMap(DateStringLongEntry::getDate,DateStringLongEntry::getValue)));

        TreeMap<String, Map<Date,Long>> sortedDetails = new TreeMap<>(paymentTypeComparator);
        sortedDetails.putAll(detailsMap);

        return new DailyStatisticDto(dates,paymentsMap,sortedDetails,stationsMap);

    }

    @Override
    public ChartDto getDailyChartStatistic(Date dateFrom, Date dateUntil, Integer averageIndex, Integer payerCode) {
        List<Date> dates = getDatesArray(dateFrom, dateUntil);

        List<DateLongEntry> expenses = statisticRepo.getChartExpensesStatisticByPayerCode(dateFrom, dateUntil, payerCode);

        List<DateStringLongEntry> payments = statisticRepo.getChartStatisticByPayerCodeGroupByPaymentType(dateFrom,dateUntil,payerCode);

        List<DateLongEntry> incomePayments = payments.stream()
                .filter(entity -> entity.getType().equals(PAYMENT_TYPE))
                .map(entity -> new DateLongEntry(entity.getDate(),entity.getValue()))
                .collect(Collectors.toList());

        List<StringLongEntry> typeChartData = statisticRepo.getChartStatisticGroupByType(dateFrom,dateUntil)
                .stream()
                .filter(entity -> !entity.getType().equals(PAYMENT_TYPE))
                .collect(Collectors.toList());

        List<StringLongEntry> stationChartData = statisticRepo.getChartStatisticGroupByStation(dateFrom,dateUntil)
                .stream()
                .filter(entity -> entity.getType() != null)
                .collect(Collectors.toList());

        List<Long> expensesList = fillBlankDatesAndGetList(dates,expenses);
        List<Long> paymentsList = fillBlankDatesAndGetList(dates,incomePayments);
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

    @Override
    public List<ChartStatisticDto> getYearChartStatistic(Date dateFrom, Date dateUntil, Integer payerCode) {
        List<ChartStatisticDto> dtoEntryList = statisticRepo.getYearExpensesStatisticGroupByMonth(dateFrom,dateUntil);

        List<DateStringLongEntry> allPaymentsByType = statisticRepo.getYearStatisticGroupByMonthAndType(dateFrom,dateUntil);
        Map<Date, List<DateStringLongEntry>> paymentsByTypeMap = allPaymentsByType.stream()
                .collect(Collectors.groupingBy(DateStringLongEntry::getDate));

        dtoEntryList.forEach(dtoEntry ->{
            // Заполнение графы доходово по каждому периоду
            Long payment = paymentsByTypeMap.get(dtoEntry.getDate())
                    .stream()
                    .filter(typeEntry -> typeEntry.getType().equals(PAYMENT_TYPE))
                    .mapToLong(DateStringLongEntry::getValue).sum();
            //Заполнение расходов по типам по каждому периоду
            dtoEntry.setPayments(payment);
                List<StringLongEntry> expensesByType = paymentsByTypeMap.get(dtoEntry.getDate())
                        .stream()
                        .filter(entry -> !entry.getType().equals(PAYMENT_TYPE))
                        .map(entry -> new StringLongEntry(entry.getType(),entry.getValue()))
                        .collect(Collectors.toList());

                dtoEntry.setExpensesByType(expensesByType);
        });



        return dtoEntryList;
    }


    public List<ChartStatisticDto> getDailyChartStatisticNew(
            Date dateFrom, Date dateUntil, Integer payerCode) {

        /*
        * Получение данных из репозитория
        */
        List<DateStringLongEntry> allPaymentsByType = statisticRepo
                .getChartStatisticByPayerCodeGroupByPaymentType(dateFrom,dateUntil,payerCode);

        List<DateStringLongEntry> allPaymentsByStation = statisticRepo
                .getChartStatisticByPayerCodeGroupByStation(dateFrom,dateUntil,payerCode);

        List<DateLongEntry> expenses = statisticRepo
                .getChartExpensesStatisticByPayerCode(dateFrom,dateUntil,payerCode);

        /*
        * Конвертация данных в удобный формат
        * */
        Map<Date,List<StringLongEntry>> paymentsByStationMap = getMapForChartStatistic(allPaymentsByStation);
        Map<Date,List<StringLongEntry>> paymentsByTypeMap = getMapForChartStatistic(allPaymentsByType);

        Map<Date,Long> expMap = expenses.stream()
                .collect(Collectors.toMap(DateLongEntry::getDate,DateLongEntry::getValue));

        /*
        * Заполнение DTO данными
        */
        List<ChartStatisticDto> dtoEntryList = new ArrayList<>();
        List<Date> dates = getDatesArray(dateFrom,dateUntil);
        dates.forEach(date -> {
            ChartStatisticDto entry = new ChartStatisticDto();
            entry.setDate(date);
            List<StringLongEntry> paymentsList = paymentsByTypeMap.get(date);

            if(paymentsList != null){
                Long payments = paymentsList.stream()
                        .filter(element -> element.getType().equals(PAYMENT_TYPE))
                        .mapToLong(StringLongEntry::getValue).sum();
                entry.setPayments(payments);
            }

            entry.setExpenses(expMap.get(date));

            entry.setExpensesByType(paymentsByTypeMap.get(date));
            entry.setExpensesByStation(paymentsByStationMap.get(date));


            dtoEntryList.add(entry);
        });

        return dtoEntryList;
    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
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

    private Map<String,Map<Date,Long>> getMapFromPaymentList(List<PaymentList> payments) {
        Map<String,Map<Date,Long>> result = new LinkedHashMap<>();

        Map<Date,Long> openingBalance = payments.stream()
                .collect(Collectors.toMap(
                        PaymentList::getDate,PaymentList::getOpeningBalance
                ));

        Map<Date,Long> closingBalance = payments.stream()
                .collect(Collectors.toMap(
                        PaymentList::getDate,PaymentList::getClosingBalance
                ));
        Map<Date,Long> expenses = payments.stream()
                .collect(Collectors.toMap(
                        PaymentList::getDate,PaymentList::getPaymentVsTaxes
                ));
        String openingBalanceTitle = messageProvider.get("application.service.statistic-service.opening-balance");
        String closingBalanceTitle = messageProvider.get("application.service.statistic-service.closing-balance");
        String expensesTitle = messageProvider.get("application.service.statistic-service.expenses");

        result.put(openingBalanceTitle, openingBalance);
        result.put(closingBalanceTitle, closingBalance);
        result.put(expensesTitle,expenses);

        return result;
    }

    private Map<Date, List<StringLongEntry>> getMapForChartStatistic(List<DateStringLongEntry> list){
        Map<Date,List<StringLongEntry>> resultMap = list.stream()
                .filter(entry -> entry.getType() != null)
                .collect(Collectors.groupingBy(DateStringLongEntry::getDate))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue()
                                .stream()
                                .map(listEntry -> new StringLongEntry(listEntry.getType(),listEntry.getValue()))
                                .collect(Collectors.toList())

                ));

        return resultMap;
    }
}
