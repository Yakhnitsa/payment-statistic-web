package com.yurets_y.payment_statistic_web.dto;

import java.util.*;

public class ChartStatisticDto {

    private Date date;

    private long expenses;

    private long payments;

    private List<StringLongEntry> expensesByType = new ArrayList<>();

    private List<StringLongEntry> expensesByStation = new ArrayList<>();

    public ChartStatisticDto() {
    }

    public ChartStatisticDto(Date date, Long expenses) {
        this.date = date;
        this.expenses = expenses;
    }

    public ChartStatisticDto(int year, int month, int day, Long expenses) {
        this.date = new GregorianCalendar(year,month-1,day).getTime();
        this.expenses = expenses;
    }

    public Long getExpenses() {
        return expenses;
    }


    public Date getDate() {
        return date;
    }


    public Long getPayments() {
        return payments;
    }

    public List<StringLongEntry> getExpensesByType() {
        return expensesByType;
    }


    public void setExpensesByType(List<StringLongEntry> expensesByType) {
        if(expensesByType != null) this.expensesByType = expensesByType;
    }

    public void setPayments(Long payments) {
        this.payments = payments != null? payments : 0L;
    }

    public List<StringLongEntry> getExpensesByStation() {
        return expensesByStation;
    }

    public void setExpensesByStation(List<StringLongEntry> expensesByStation) {
        if(expensesByStation != null) this.expensesByStation = expensesByStation;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setExpenses(Long expenses) {
        this.expenses = expenses != null ? expenses : 0L;
    }
}
