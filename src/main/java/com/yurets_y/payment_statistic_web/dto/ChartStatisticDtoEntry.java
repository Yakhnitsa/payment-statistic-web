package com.yurets_y.payment_statistic_web.dto;

import java.util.*;

public class ChartStatisticDtoEntry {

    private Date date;

    private Long expenses;

    private Long payments;

    private List<DateStringLongEntry> expensesByType = new ArrayList<>();

    private List<DateStringLongEntry> expensesByStation = new ArrayList<>();

    public ChartStatisticDtoEntry() {
    }

    public ChartStatisticDtoEntry(Date date, Long expenses) {
        this.date = date;
        this.expenses = expenses;
    }

    public ChartStatisticDtoEntry(int year, int month, int day, Long expenses) {
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

    public List<DateStringLongEntry> getExpensesByType() {
        return expensesByType;
    }


    public void setExpensesByType(List<DateStringLongEntry> expensesByType) {
        this.expensesByType = expensesByType;
    }

    public void setPayments(Long payments) {
        this.payments = payments;
    }

    public List<DateStringLongEntry> getExpensesByStation() {
        return expensesByStation;
    }

    public void setExpensesByStation(List<DateStringLongEntry> expensesByStation) {
        this.expensesByStation = expensesByStation;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setExpenses(Long expenses) {
        this.expenses = expenses;
    }
}
