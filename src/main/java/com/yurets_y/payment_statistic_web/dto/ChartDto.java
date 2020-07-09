package com.yurets_y.payment_statistic_web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.Views;

import java.util.List;

public class ChartDto {

    @JsonView(Views.ShortView.class)
    private List<String> labels;

    @JsonView(Views.ShortView.class)
    private List<Long> paymentStatistic;

    @JsonView(Views.ShortView.class)
    private List<Long> expensesStatistic;

    @JsonView(Views.ShortView.class)
    private List<Long> averageStatistic;

    @JsonView(Views.ShortView.class)
    private List<StringLongEntry> typeChartData;

    @JsonView(Views.ShortView.class)
    private List<StringLongEntry> stationChartData;

    @JsonView(Views.ShortView.class)
    private int averageIndex;


    public ChartDto(List<String> labels, List<Long> paymentStatistic, List<Long> expensesStatistic, List<Long> averageStatistic, List<StringLongEntry> typeChartData, List<StringLongEntry> stationChartData, int averageIndex) {
        this.labels = labels;
        this.paymentStatistic = paymentStatistic;
        this.expensesStatistic = expensesStatistic;
        this.averageStatistic = averageStatistic;
        this.typeChartData = typeChartData;
        this.stationChartData = stationChartData;
        this.averageIndex = averageIndex;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Long> getPaymentStatistic() {
        return paymentStatistic;
    }

    public void setPaymentStatistic(List<Long> paymentStatistic) {
        this.paymentStatistic = paymentStatistic;
    }

    public List<Long> getExpensesStatistic() {
        return expensesStatistic;
    }

    public void setExpensesStatistic(List<Long> expensesStatistic) {
        this.expensesStatistic = expensesStatistic;
    }

    public List<Long> getAverageStatistic() {
        return averageStatistic;
    }

    public void setAverageStatistic(List<Long> averageStatistic) {
        this.averageStatistic = averageStatistic;
    }

    public int getAverageIndex() {
        return averageIndex;
    }

    public void setAverageIndex(int averageIndex) {
        this.averageIndex = averageIndex;
    }

    public List<StringLongEntry> getTypeChartData() {
        return typeChartData;
    }

    public List<StringLongEntry> getStationChartData() {
        return stationChartData;
    }
}
