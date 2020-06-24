package com.yurets_y.payment_statistic_web.service;

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
public class PaymentStatisticServiceImpl implements PaymentStatisticService {
    private PaymentListRepo paymentListRepo;
    private PaymentDetailsRepo paymentDetailsRepo;
    private Comparator<PaymentDetails> paymentDetailsComparator;
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public PaymentStatisticServiceImpl(PaymentListRepo paymentListRepo,
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
}
