package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.yurets_y.payment_statistic_web.dto.PaymentListDto;
import com.yurets_y.payment_statistic_web.entity.*;
import com.yurets_y.payment_statistic_web.service.PaymentDetailsService;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PaymentStatisticController {

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final int RECORDS_PER_PAGE = 30;

//    @javax.annotation.Resource(name = "payment-details-by-type-comparator")
    private Comparator<PaymentDetails> paymentDetailsComparator;

    private PaymentListService paymentListService;

    private PaymentDetailsService paymentDetailsService;



    @GetMapping
    public String paymentStatistic() {
        return "index";
    }

//    @GetMapping("/api/last-payments")
//    @ResponseBody
//    public List<PaymentList> getLastPayments() {
//
//        List<PaymentList> paymentLists = paymentListService.getAll();
//
//
//        return paymentLists;
//    }


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

    @GetMapping("/api/download-file/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
            @PathVariable String fileName,
            @RequestParam String file,
            HttpServletRequest request
    ) throws FileNotFoundException {

        Resource resource = paymentListService.getFileAsResource(fileName);

        String contentType = "application/octet-stream";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/api/daily-statistic")
    @ResponseBody
    @com.fasterxml.jackson.annotation.JsonView(Views.ShortView.class)
    public ResponseEntity<?> getDailyStatistic(
            @RequestParam(value = "dateFrom", required = false) String from,
            @RequestParam(value = "dateUntil", required = false) String until
    ) throws ParseException {
        Date dateFrom = null;
        Date dateUntil = null;

        if (!"".equals(dateFrom)) {
            dateFrom = DATE_FORMAT.parse(from);
        }

        if (!"".equals(until)) {
            dateUntil = DATE_FORMAT.parse(until);
        }

        Map<String, Object> statistic = new LinkedHashMap<>();

        List<PaymentList> payments = paymentListService.getByPeriod(dateFrom, dateUntil);
        List<PaymentDetails> paymentDetailsList = paymentDetailsService.getPaymentDetailsByDate(dateFrom, dateUntil);

        Collections.sort(paymentDetailsList, paymentDetailsComparator);

        Map<String, Map<Date, Long>> details = paymentDetailsList
                .stream()
                .collect(Collectors.groupingBy(PaymentDetails::getType,
                        LinkedHashMap::new,
                        Collectors.groupingBy(
                                PaymentDetails::getDate,
                                TreeMap::new,
                                Collectors.summingLong(PaymentDetails::getTotalPayment))
                ));

        Map<String, Map<String, Long>> reformattedDetails = details
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        innerEntry -> DATE_FORMAT.format(innerEntry.getKey()),
                                        Map.Entry::getValue
                                )),
                        (first, second) -> first,
                        LinkedHashMap::new
                ));

        statistic.put("payments", payments);
        statistic.put("details", reformattedDetails);
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

    @Autowired
    public void setPaymentListService(PaymentListService paymentListService) {
        this.paymentListService = paymentListService;
    }

    @Autowired
    public void setPaymentDetailsService(PaymentDetailsService paymentDetailsService) {
        this.paymentDetailsService = paymentDetailsService;
    }

    @Autowired
    public void setPaymentDetailsComparator(Comparator<PaymentDetails> paymentDetailsComparator) {
        this.paymentDetailsComparator = paymentDetailsComparator;
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
