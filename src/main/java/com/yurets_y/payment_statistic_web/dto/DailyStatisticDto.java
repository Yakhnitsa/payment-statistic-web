package com.yurets_y.payment_statistic_web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.Views;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DailyStatisticDto {

    @JsonView(Views.ShortView.class)
    private List<Date> dates;

    @JsonView(Views.ShortView.class)
    private Map<String, Map<Date, Long>> payments;

    @JsonView(Views.ShortView.class)
    private Map<String, Map<Date, Long>> details;

    @JsonView(Views.ShortView.class)
    private Map<String, Map<Date, Long>> expensesByStation;

    public DailyStatisticDto(List<Date> dates, Map<String, Map<Date, Long>> payments, Map<String,
            Map<Date, Long>> details, Map<String, Map<Date, Long>> expensesByStation) {
        this.dates = dates;
        this.payments = payments;
        this.details = details;
        this.expensesByStation = expensesByStation;
    }

    public List<Date> getDates() {
        return dates;
    }

    public Map<String, Map<Date, Long>> getPayments() {
        return payments;
    }

    public Map<String, Map<Date, Long>> getDetails() {
        return details;
    }

}
