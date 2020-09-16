package com.yurets_y.payment_statistic_web.entity;

import java.io.Serializable;
import java.util.Date;

public class RailroadDocumentId implements Serializable {
    private int number;
    private Date dateStamp;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(Date dateStamp) {
        this.dateStamp = dateStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RailroadDocumentId that = (RailroadDocumentId) o;

        if (number != that.number) return false;
        return dateStamp != null ? dateStamp.equals(that.dateStamp) : that.dateStamp == null;
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + (dateStamp != null ? dateStamp.hashCode() : 0);
        return result;
    }
}
