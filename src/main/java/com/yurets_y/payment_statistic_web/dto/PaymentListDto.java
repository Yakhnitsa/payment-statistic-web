package com.yurets_y.payment_statistic_web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.Views;

import java.util.List;

@JsonView(Views.NormalView.class)
public class PaymentListDto {
    private List<PaymentList> list;
    private int currentPage;
    private int totalPages;

    public PaymentListDto(List<PaymentList> list, int currentPage, int totalPages) {
        this.list = list;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<PaymentList> getList() {
        return list;
    }

    public void setList(List<PaymentList> list) {
        this.list = list;
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
