package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.dto.PaymentListDto;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import com.yurets_y.payment_statistic_web.entity.User;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class PaymentListController {

    private MessageProvider messageProvider;

    private final int RECORDS_PER_PAGE = 30;

    private PaymentListService paymentListService;

    @GetMapping("/api/payments")
    @ResponseBody
    @JsonView(Views.NormalView.class)
    public PaymentListDto getPayments(
            @AuthenticationPrincipal User user,
            @PageableDefault(size=RECORDS_PER_PAGE,
                    sort={"date"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam Integer payerCode,
            @RequestParam(value = "page") Integer pageNumb

    ) {
        Page<PaymentList> page = null;
        if(dateFrom == null || dateUntil == null){
            page = paymentListService.getAll(pageable,payerCode);
        }else{
            page = paymentListService.getPageByPeriod(pageable,dateFrom,dateUntil,payerCode);
        }

        return new PaymentListDto(page.getContent(),page.getNumber(),page.getTotalPages());
    }

    @GetMapping("/api/single-payment")
    @ResponseBody
    @JsonView(Views.FullView.class)
    public PaymentList getPayment(
            @RequestParam(value = "payerCode") Integer payerCode,
            @RequestParam(value = "listNumber") Integer listNumber
    ) {
        PaymentListId id = new PaymentListId(payerCode, listNumber);
        PaymentList paymentList = paymentListService.getById(id);
        return paymentList;
    }

    @DeleteMapping("/api/remove-payment")
    @Secured({"ROLE_ADMIN", "ROLE_EDITOR"})
    @ResponseBody
    @JsonView(Views.NormalView.class)
    public ResponseEntity<?> deletePayment(@RequestBody(required = false) PaymentList id) {
        if (paymentListService.remove(id)) {
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
    }

    @Autowired
    public void setPaymentListService(PaymentListService paymentListService) {
        this.paymentListService = paymentListService;
    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

}
