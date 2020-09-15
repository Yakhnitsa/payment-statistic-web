package com.yurets_y.payment_statistic_web.entity;

import java.util.Date;

public class RailroadDocumentId {
    private int number;
    private Date date;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RailroadDocumentId that = (RailroadDocumentId) o;

        if (number != that.number) return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
