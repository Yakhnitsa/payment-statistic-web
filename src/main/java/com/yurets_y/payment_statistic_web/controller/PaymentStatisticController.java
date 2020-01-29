package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewSerializer;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.service.PaymentListDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import static com.monitorjbl.json.Match.match;


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

        List<PaymentList> paymentLists = paymentListDAO.getAll();


        return paymentLists;
    }

    @GetMapping("test")
    @ResponseBody
    public String getTestJSon() throws JsonProcessingException {

        List<PaymentList> paymentLists = paymentListDAO.getAll();

        return marshallJSON(paymentLists);
    }

    private String marshallJSON(List<PaymentList> paymentLists) throws JsonProcessingException {


        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(JsonView.class, new JsonViewSerializer());
        mapper.registerModule(module);

        String result = mapper.writeValueAsString(JsonView.with(paymentLists)
                .onClass(PaymentList.class, match()
                        .exclude("paymentDetailsList")));

        return result;
    }

}
