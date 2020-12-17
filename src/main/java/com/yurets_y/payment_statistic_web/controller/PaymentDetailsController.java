package com.yurets_y.payment_statistic_web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.dto.JsonPage;
import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.payment_statistic_services.PaymentDetailsService;
import com.yurets_y.payment_statistic_web.service.payment_statistic_services.PaymentDetailsSpecification;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    private PaymentDetailsSpecification paymentDetailsSpec;

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
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "50") Integer itemsPerPage,
            @RequestParam(required = false, defaultValue = "date") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection,
            @RequestParam Integer payerCode,
            @RequestParam(required = false, defaultValue = "") String paymentType,

            @RequestParam(required = false) Integer stationCode,
            @RequestParam(required = false, defaultValue = "") String docNumber,
            @RequestParam(required = false) Float paymentSumFrom,
            @RequestParam(required = false) Float paymentSumTo
    ){
        Pageable pageable = getPageable(pageNumber, itemsPerPage, sortBy, sortDirection);
        Specification<PaymentDetails> specification = paymentDetailsSpec.dateSpec(dateFrom,dateUntil);
        specification = specification.and(paymentDetailsSpec.payerCodeSpec(payerCode));
        specification = specification.and(paymentDetailsSpec.paymentTypeSpec(paymentType));
        specification = specification.and(paymentDetailsSpec.stationSpec(stationCode));
        specification = specification
                .and(paymentDetailsSpec.totalPaymentSpec(floatToLong(paymentSumFrom),floatToLong(paymentSumTo)));
        specification = specification.and(paymentDetailsSpec.docNumberLikeSpec(docNumber));

        Page<PaymentDetails> paymentDetailsPage = paymentDetailsService.getAllBySpecification(specification,pageable);

        if(paymentDetailsPage.getTotalPages() < pageNumber + 1 && pageNumber > 0){
            pageable = getPageable(0,itemsPerPage,sortBy,sortDirection);
            paymentDetailsPage = paymentDetailsService.getAllBySpecification(specification,pageable);
        }


//        Page<PaymentDetails> paymentDetailsPage = paymentDetailsService.getAllWithParameters(
//                payerCode, paymentType, dateFrom,dateUntil, pageRequest,stationCode,docNumber,paymentSum);

        JsonPage<PaymentDetails> jsonPage = new JsonPage<>(paymentDetailsPage,pageable);

        return new ResponseEntity<>(jsonPage,HttpStatus.OK);

    }

//    private int getItemsInPage(){
//        return 30;
//    }

    private Pageable getPageable(int page, int size, String sort, String direction){
        return "ASC".equalsIgnoreCase(direction) ? PageRequest.of(page,size, Sort.by(sort).ascending())
                : PageRequest.of(page,size, Sort.by(sort).ascending().descending());

    }

    private Long floatToLong(Float number){
        if(number == null ) return null;
        number = number * 100;
        return number.longValue();
    }

    @Autowired
    public void setPaymentDetailsSpec(PaymentDetailsSpecification paymentDetailsSpec) {
        this.paymentDetailsSpec = paymentDetailsSpec;
    }
}
