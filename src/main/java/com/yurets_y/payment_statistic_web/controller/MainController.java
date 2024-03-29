package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurets_y.payment_statistic_web.entity.*;
import com.yurets_y.payment_statistic_web.service.payment_statistic_services.PaymentListService;
import com.yurets_y.payment_statistic_web.service.StationService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Controller
public class MainController {

    @Value("${spring.profiles.active}")
    private String profile;

    private PaymentListService paymentListService;

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    private MessageProvider messageProvider;

    private StationService stationService;

    @GetMapping("")
    public String railroadDocuments(
            Model model,
            @AuthenticationPrincipal User user
    ) {
        model.addAttribute("isDevMode", "dev".equals(profile));

        if(user != null){
            model.addAttribute("userRoles", user.getAuthorities());
        }

        try {
            model.addAttribute("stations",marshallJSON(stationService.getAllStations()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "rail-doc-page";
    }


    @GetMapping("payment-statistic/**")
    public String paymentStatistic(
            Model model,
            @AuthenticationPrincipal User user
    ) {
        List<String> codes = paymentListService.getPaymentCodes();
        model.addAttribute("paymentCodes",codes);
        model.addAttribute("isDevMode", "dev".equals(profile));

        try {
            model.addAttribute("stations",marshallJSON(stationService.getAllStations()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if(user != null){
            model.addAttribute("userRoles", user.getAuthorities());
        }


        return "payment-statistic";
    }

    @GetMapping("test")
    public String getTestPage() {
        return "test";
    }



    @GetMapping("/api/stations")
    @ResponseBody
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> getStations(){
        List<Station> stations = stationService.getAllStations();
        return new ResponseEntity<>(stations,HttpStatus.OK);
    }



    @Autowired
    public void setPaymentListService(PaymentListService paymentListService) {
        this.paymentListService = paymentListService;
    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Autowired
    public void setStationService(StationService stationService) {
        this.stationService = stationService;
    }

    private String marshallJSON(List<Station> stations) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.setConfig(mapper.getSerializationConfig()
                .withView(Views.ShortView.class));

        return mapper.writeValueAsString(stations);
    }

}
