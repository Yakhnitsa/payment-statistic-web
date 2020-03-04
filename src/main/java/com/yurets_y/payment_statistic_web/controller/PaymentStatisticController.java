package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewSerializer;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.PaymentListDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/last-payments")
    @ResponseBody
    public List<PaymentList> getLastPayments(){

        List<PaymentList> paymentLists = paymentListDAO.getAll();


        return paymentLists;
    }

    @GetMapping("/api/payments")
    @ResponseBody
    @com.fasterxml.jackson.annotation.JsonView(Views.NormalView.class)
    public List<PaymentList> getTestJSon() throws JsonProcessingException {

        List<PaymentList> paymentLists = paymentListDAO.getAll();

        return paymentLists;
    }


    @PostMapping("/api/single-payment")
    @ResponseBody
    @com.fasterxml.jackson.annotation.JsonView(Views.FullView.class)
    public PaymentList getPayment(
            @RequestParam(value = "payerCode",required = false) Integer payerCode,
            @RequestParam(value = "listNumber",required = false) Integer listNumber
    ) {
        PaymentListId id = new PaymentListId(payerCode,listNumber);
        PaymentList paymentList = paymentListDAO.getById(id);
        return paymentList;
    }

    @PostMapping("/api/delete-payment")
    @ResponseBody
    @com.fasterxml.jackson.annotation.JsonView(Views.ShortView.class)
    public ResponseEntity<?> deleteList(@RequestBody PaymentList paymentList){
        if(paymentListDAO.contains(paymentList)){
            paymentListDAO.remove(paymentList);
        }else{
            return new ResponseEntity<>(paymentList,HttpStatus.NOT_FOUND);
        }
        if(paymentListDAO.contains(paymentList)){
            return new ResponseEntity<>(paymentList,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(paymentList,HttpStatus.OK);

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
