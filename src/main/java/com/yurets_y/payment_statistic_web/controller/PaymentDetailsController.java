package com.yurets_y.payment_statistic_web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.dto.JsonPage;
import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.payment_statistic_services.PaymentDetailsService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/payment-details")
public class PaymentDetailsController {


    private final String WRONG_PARAMETERS_MESSAGE = "application.controller.void-request-param";


    private MessageProvider messageProvider;

    private PaymentDetailsService paymentDetailsService;

    @Autowired
    public PaymentDetailsController(MessageProvider messageProvider, PaymentDetailsService paymentDetailsService) {
        this.messageProvider = messageProvider;
        this.paymentDetailsService = paymentDetailsService;
    }

    @GetMapping("/payment-types")
    public ResponseEntity<?> dailyChartStatistic(){
        List<String> paymentTypes = paymentDetailsService.getAllPaymentTypes();
        return new ResponseEntity<>(paymentTypes,HttpStatus.OK);

    }

    @GetMapping()
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> getPaymentDetails(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam Integer payerCode,
            @RequestParam(required = false, defaultValue = "") String paymentType,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false) Integer stationCode,
            @RequestParam(required = false, defaultValue = "") String docNumber,
            @RequestParam(required = false) Float paymentSum

            ){
//        TODO выбрать сортировку в front-end и записать в параметры
        Sort dateSort = Sort.by("date").descending();
        Pageable pageRequest = PageRequest.of(pageNumber,getItemsInPage(),dateSort);

        Page<PaymentDetails> paymentDetailsPage = paymentDetailsService.getAllWithParameters(
                payerCode, paymentType, dateFrom,dateUntil, pageRequest,stationCode,docNumber,paymentSum);

        JsonPage<PaymentDetails> jsonPage = new JsonPage<>(paymentDetailsPage,pageRequest);

        return new ResponseEntity<>(jsonPage,HttpStatus.OK);

    }

    private int getItemsInPage(){
        return 30;
    }

}
