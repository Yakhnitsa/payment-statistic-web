package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.User;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import com.yurets_y.payment_statistic_web.service.TempListService;
import com.yurets_y.payment_statistic_web.service.mail_service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
public class UploadController {

    private PaymentListService paymentListService;

    private TempListService tempListService;

    private MailService mailService;

    @Autowired
    public UploadController(
            PaymentListService paymentListService,
            TempListService tempListService,
            MailService mailService) {
        this.paymentListService = paymentListService;
        this.tempListService = tempListService;
        this.mailService = mailService;
    }

    @PostMapping("/api/upload-single")
    @Secured({"ROLE_ADMIN", "ROLE_EDITOR"})
    public ResponseEntity<?> uploadSingleList(
            @RequestParam("file") MultipartFile file
    ){
        if(file.isEmpty()){
            return new ResponseEntity<String>("No file!",HttpStatus.OK);
        }
        String fileName = file.getOriginalFilename();
        return null;
    }


    @PostMapping("/api/upload-multiple")
    @Secured({"ROLE_ADMIN", "ROLE_EDITOR"})
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> uploadMultiple(
            @RequestParam("files") MultipartFile[] files
    ){
        if(files.length == 0){
            return new ResponseEntity<String>("No file!!!",HttpStatus.NO_CONTENT);
        }
        List<String> extensions = Arrays.asList("text/html","text/xml");

        for(MultipartFile file: files){
            String type = file.getContentType();
            if(extensions.contains(type))
                tempListService.putToTempDB(file);
        }
        return new ResponseEntity<>(tempListService.getAllFromTempDB(),HttpStatus.OK);
    }

    @GetMapping("/api/download-temp")
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> getListsFromTempDB(){
        return new ResponseEntity<>(tempListService.getAllFromTempDB(),HttpStatus.OK);
    }

    @PostMapping("/api/save-temp-selected")
    @JsonView(Views.ShortView.class)
    @Secured({"ROLE_ADMIN", "ROLE_EDITOR"})
    public ResponseEntity<?> saveSelectedToMainDB(
            @RequestBody PaymentList[] paymentLists
    ){
        for(PaymentList list : paymentLists){
            PaymentList listFromTemp = tempListService.deleteFromTempDB(list);
            if(listFromTemp == null) continue;
//            if(paymentListService.contains(paymentList))
//                paymentListService.update(listFromTemp);
//            else{
//                paymentListService.add(listFromTemp);
//            }
            paymentListService.add(listFromTemp);

        }
        return  new ResponseEntity<>(tempListService.getAllFromTempDB(),HttpStatus.OK);
    }

    @PostMapping("/api/delete-temp-selected")
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> deleteSelectedFromTemp(
            @RequestBody PaymentList[] paymentLists
    ){
        for(PaymentList list : paymentLists){
            tempListService.deleteFromTempDB(list);
        }
        return  new ResponseEntity<>(tempListService.getAllFromTempDB(),HttpStatus.OK);
    }


    @PostMapping("/api/scan-from-mail")
    @Secured({"ROLE_ADMIN", "ROLE_EDITOR"})
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> scanListsFromMail(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date lastUpdate
    ){

        mailService.scanFromMailToTempDb(lastUpdate);
        return new ResponseEntity<>(tempListService.getAllFromTempDB(),HttpStatus.OK);
    }


//    @Autowired
//    public void setHtmlDocParser(DocParser htmlDocParser) {
//        this.htmlDocParser = htmlDocParser;
//    }
//
//    @Autowired
//    public void setPaymentListDAO( PaymentListService paymentListService) {
//        this.paymentListService = paymentListService;
//    }
//
//    @Autowired
//    public void setTempListService(TempListService tempListService) {
//        this.tempListService = tempListService;
//    }

}
