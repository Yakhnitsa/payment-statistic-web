package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.dto.DailyStatisticDto;
import com.yurets_y.payment_statistic_web.dto.PaymentListDto;
import com.yurets_y.payment_statistic_web.entity.*;
import com.yurets_y.payment_statistic_web.service.PaymentDetailsService;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import com.yurets_y.payment_statistic_web.service.StatisticService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

    @Value("${spring.profiles.active}")
    private String profile;

    private PaymentListService paymentListService;

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    private MessageProvider messageProvider;

    @GetMapping
//    @PreAuthorize("hasRole(T(com.yurets_y.payment_statistic_web.entity.Role).ROLE_ADMIN)")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String paymentStatistic(
            @AuthenticationPrincipal UserDetails user,
            Model model) {
        List<String> codes = paymentListService.getPaymentCodes();
        model.addAttribute("paymentCodes",codes);
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }


    @Autowired
    public void setPaymentListService(PaymentListService paymentListService) {
        this.paymentListService = paymentListService;
    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
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
