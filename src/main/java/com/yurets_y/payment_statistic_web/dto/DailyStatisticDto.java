package com.yurets_y.payment_statistic_web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.Views;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DailyStatisticDto {

    @JsonView(Views.ShortView.class)
    private List<PaymentList> payments;

    @JsonView(Views.ShortView.class)
    private Map<String, Map<String, Long>> details;

    public List<PaymentList> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentList> payments) {
        this.payments = payments;
    }

    public Map<String, Map<String, Long>> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Map<String, Long>> details) {
        this.details = details;
    }
}
