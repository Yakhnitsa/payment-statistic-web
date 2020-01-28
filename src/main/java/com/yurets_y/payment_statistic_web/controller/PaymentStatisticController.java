package com.yurets_y.payment_statistic_web.controller;


import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.service.PaymentListDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PaymentStatisticController {

    @Autowired
    private PaymentListDAO paymentListDAO;

    @GetMapping
    public String paymentStatistic(){
        return "index";
    }

    @GetMapping("last-payments")
    @ResponseBody
    public List<PaymentList> getLastPayments(){
        return paymentListDAO.getAll();
    }

}
