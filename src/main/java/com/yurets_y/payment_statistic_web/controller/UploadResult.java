package com.yurets_y.payment_statistic_web.controller;

import com.yurets_y.payment_statistic_web.entity.PaymentList;

class UploadResult {
    private String fileName;
    private PaymentList paymentList;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public PaymentList getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(PaymentList paymentList) {
        this.paymentList = paymentList;
    }

    public UploadResult(String fileName, PaymentList paymentList) {
        this.fileName = fileName;
        this.paymentList = paymentList;
    }
}
