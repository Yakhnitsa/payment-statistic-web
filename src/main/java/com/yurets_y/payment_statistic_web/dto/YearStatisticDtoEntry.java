package com.yurets_y.payment_statistic_web.dto;

import java.util.*;

public class YearStatisticDtoEntry {

    private Date date;

    private Long expenses;

    private Long payments;

    private List<DateStringLongDto> expensesByType = new ArrayList<>();

    private List<DateStringLongDto> expensesByStation = new ArrayList<>();

    public YearStatisticDtoEntry(Date date, Long expenses) {
        this.date = date;
        this.expenses = expenses;
    }

    public YearStatisticDtoEntry(int year, int month, int day, Long expenses) {
        this.date = new GregorianCalendar(year,month,day).getTime();
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

    public List<DateStringLongDto> getExpensesByType() {
        return expensesByType;
    }


    public void setExpensesByType(List<DateStringLongDto> expensesByType) {
        this.expensesByType = expensesByType;
    }

    public void setPayments(Long payments) {
        this.payments = payments;
    }

    public List<DateStringLongDto> getExpensesByStation() {
        return expensesByStation;
    }

    public void setExpensesByStation(List<DateStringLongDto> expensesByStation) {
        this.expensesByStation = expensesByStation;
    }


}
