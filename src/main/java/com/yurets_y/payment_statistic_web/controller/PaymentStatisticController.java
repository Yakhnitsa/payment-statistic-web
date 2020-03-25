package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewSerializer;
import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PaymentStatisticController {

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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


    @GetMapping("/api/single-payment")
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

    @DeleteMapping("/api/delete-payment/{code}_{numb}")
    @ResponseBody
    public ResponseEntity<?> deleteTest(@PathVariable int code, @PathVariable int numb){
        PaymentListId id = new PaymentListId(code,numb);

        if(paymentListDAO.removeById(id)){
            return new ResponseEntity<>(id,HttpStatus.OK);
        }
        return new ResponseEntity<>(id,HttpStatus.NOT_FOUND);

    }

    @GetMapping("/api/daily-statistic")
    @ResponseBody
    @com.fasterxml.jackson.annotation.JsonView(Views.ShortView.class)
    public ResponseEntity<?> getDailyStatistic(
            @RequestParam(value = "dateFrom",required = false) String from,
            @RequestParam(value = "dateUntil",required = false) String until
    ) throws ParseException {
        Date dateFrom = null;
        Date dateUntil = null;
        if(!"".equals(dateFrom)){
            dateFrom = DATE_FORMAT.parse(from);
        }
        if(!"".equals(until)){
            dateUntil = DATE_FORMAT.parse(until);
        }
        Map<String,Object> statistic = new LinkedHashMap<>();
        List<PaymentList> payments = paymentListDAO.getByPeriod(dateFrom,dateUntil);
        List<PaymentDetails> paymentDetailsList = paymentListDAO.getPaymentDetailsByDate(dateFrom,dateUntil);
        Map<String,Map<Date,Long>> details = paymentDetailsList
                .stream()
                .collect(Collectors.groupingBy(PaymentDetails::getType,
                        LinkedHashMap::new,
                        Collectors.groupingBy(
                                PaymentDetails::getDate,
                                TreeMap::new,
                                Collectors.summingLong(PaymentDetails::getTotalPayment))
                        ));

        statistic.put("payments",payments);
        statistic.put("details",details);
        return new ResponseEntity<>(statistic,HttpStatus.OK);
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
