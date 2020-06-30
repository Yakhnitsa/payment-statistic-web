package com.yurets_y.payment_statistic_web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.Views;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DailyStatisticDto {

    private List<Date> dates;

    @JsonView(Views.ShortView.class)
    private List<PaymentList> payments;

    @JsonView(Views.ShortView.class)
    private Map<String, Map<Date, Long>> details;

    public DailyStatisticDto(List<Date> dates, List<PaymentList> payments, Map<String, Map<Date, Long>> details) {
        this.dates = dates;
        this.payments = payments;
        this.details = details;
    }

    public List<Date> getDates() {
        return dates;
    }

    public List<PaymentList> getPayments() {
        return payments;
    }

    public Map<String, Map<Date, Long>> getDetails() {
        return details;
    }
}
