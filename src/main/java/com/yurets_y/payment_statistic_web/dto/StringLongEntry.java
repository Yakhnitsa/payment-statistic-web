package com.yurets_y.payment_statistic_web.dto;

public class StringLongEntry {
    private String type;

    private Long value;

    public StringLongEntry(String type, Long value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Long getValue() {
        return value;
    }
}
