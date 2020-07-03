package com.yurets_y.payment_statistic_web.dto;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateStringLongDto {

    private Date date;

    private String type;

    private Long value;

    public DateStringLongDto(Date date, String type, Long value) {
        this.date = date;
        this.type = type;
        this.value = value;
    }

    public DateStringLongDto(int year, int month, int day, String type, Long value) {
        this.date  = new GregorianCalendar(year,month,day).getTime();
        this.type = type;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Long getValue() {
        return value;
    }
}
