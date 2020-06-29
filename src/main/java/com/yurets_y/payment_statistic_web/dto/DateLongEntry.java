package com.yurets_y.payment_statistic_web.dto;

import java.util.Date;

public class DateLongEntry {
    private Date date;

    private Long value;

    public DateLongEntry(Date date, Long value) {
        this.date = date;
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public Long getValue() {
        return value;
    }
}
