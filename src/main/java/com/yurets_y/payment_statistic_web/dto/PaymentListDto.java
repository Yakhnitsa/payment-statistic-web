package com.yurets_y.payment_statistic_web.dto;

import com.yurets_y.payment_statistic_web.entity.PaymentList;

import java.util.List;

public class PaymentListDto {
    private List<PaymentList> paymentLists;
    private int currentPage;
    private int totalPages;

    public PaymentListDto(List<PaymentList> paymentLists, int currentPage, int totalPages) {
        this.paymentLists = paymentLists;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<PaymentList> getPaymentLists() {
        return paymentLists;
    }

    public void setPaymentLists(List<PaymentList> paymentLists) {
        this.paymentLists = paymentLists;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
