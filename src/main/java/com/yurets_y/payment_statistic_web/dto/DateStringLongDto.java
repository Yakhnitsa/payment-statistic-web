package com.yurets_y.payment_statistic_web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.Views;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateStringLongDto {

    @JsonView(Views.ShortView.class)
    private Date date;

    @JsonView(Views.ShortView.class)
    private String type;

    @JsonView(Views.ShortView.class)
    private Long value;

    public DateStringLongDto(Date date, String type, Long value) {
        this.date = date;
        this.type = type;
        this.value = value;
    }

    public DateStringLongDto(int year, int month, int day, String type, Long value) {
        this.date  = new GregorianCalendar(year,month-1,day).getTime();
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
