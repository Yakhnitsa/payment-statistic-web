package com.yurets_y.payment_statistic_web.controller;


import com.yurets_y.payment_statistic_web.dto.DailyStatisticDto;
import com.yurets_y.payment_statistic_web.dto.PaymentListDto;
import com.yurets_y.payment_statistic_web.entity.*;
import com.yurets_y.payment_statistic_web.service.PaymentDetailsService;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import com.yurets_y.payment_statistic_web.service.StatisticService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final int RECORDS_PER_PAGE = 30;

    private PaymentListService paymentListService;


    @Autowired
    private MessageProvider messageProvider;



    @GetMapping
    public String paymentStatistic() {
        return "index";
    }

    @GetMapping("/api/payments")
    @ResponseBody
    @com.fasterxml.jackson.annotation.JsonView(Views.NormalView.class)
    public PaymentListDto getPayments(
            @PageableDefault(size=RECORDS_PER_PAGE,
                    sort={"date"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @RequestParam(value = "dateFrom") String from,
            @RequestParam(value = "page") Integer pageNumb,
            @RequestParam(value = "dateUntil") String until

    ) throws ParseException {
        Date dateFrom = null;
        Date dateUntil = null;

        if ((!"".equals(from)) && (!"".equals(until))) {
            dateFrom = DATE_FORMAT.parse(from);
            dateUntil = DATE_FORMAT.parse(until);
        }
        Page<PaymentList> page = null;
        if(dateFrom == null || dateUntil == null){
            page = paymentListService.getAll(pageable);
        }else{
            page = paymentListService.getPageByPeriod(pageable,dateFrom,dateUntil);
        }

        return new PaymentListDto(page.getContent(),page.getNumber(),page.getTotalPages());
    }

    @GetMapping("/api/single-payment")
    @ResponseBody
    @com.fasterxml.jackson.annotation.JsonView(Views.FullView.class)
    public PaymentList getPayment(
            @RequestParam(value = "payerCode", required = false) Integer payerCode,
            @RequestParam(value = "listNumber", required = false) Integer listNumber
    ) {
        PaymentListId id = new PaymentListId(payerCode, listNumber);
        PaymentList paymentList = paymentListService.getById(id);
        return paymentList;
    }

    @DeleteMapping("/api/remove-payment")
    @ResponseBody
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

//    private String marshallJSON(List<PaymentList> paymentLists) throws JsonProcessingException {
//
//        ObjectMapper mapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(JsonView.class, new JsonViewSerializer());
//        mapper.registerModule(module);
//
//        String result = mapper.writeValueAsString(JsonView.with(paymentLists)
//                .onClass(PaymentList.class, match()
//                        .exclude("paymentDetailsList")));
//
//        return result;
//    }

}
