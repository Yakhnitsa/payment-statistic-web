package com.yurets_y.payment_statistic_web.entity;

import java.io.Serializable;
import java.util.Date;

public class RailroadDocumentId implements Serializable {
    private int docNumber;
    private Date dateStamp;

    public int getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(int docNumber) {
        this.docNumber = docNumber;
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

        if (docNumber != that.docNumber) return false;
        return dateStamp != null ? dateStamp.equals(that.dateStamp) : that.dateStamp == null;
    }

    @Override
    public int hashCode() {
        int result = docNumber;
        result = 31 * result + (dateStamp != null ? dateStamp.hashCode() : 0);
        return result;
    }
}
