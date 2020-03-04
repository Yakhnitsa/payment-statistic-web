package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.DocParser;
import com.yurets_y.payment_statistic_web.service.PaymentListDAO;
import com.yurets_y.payment_statistic_web.service.TempListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
public class UploadController {

    private DocParser htmlDocParser;

    private PaymentListDAO paymentListDAO;

    private TempListService tempListService;

    @PostMapping("/api/upload-single")
    public ResponseEntity<?> uploadSingleList(
            @RequestParam("file") MultipartFile file
    ){
        if(file.isEmpty()){
            return new ResponseEntity<String>("No file!",HttpStatus.OK);
        }
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);

        return null;
    }


    @PostMapping("/api/upload-multiple")
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> uploadMultiple(
            @RequestParam("files") MultipartFile[] files
    ){
        if(files.length == 0){
            String fileName = "no file";
            return new ResponseEntity<String>("No file!!!",HttpStatus.NO_CONTENT);
        }
        for(MultipartFile file: files){
            String type = file.getContentType();
            if(!"text/html".equals(type)){
                continue;
            }
            PaymentList paymentList = tempListService.putToTempDB(file);

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
    public ResponseEntity<?> saveSelectedToMainDB(
            @RequestBody PaymentList[] paymentLists
    ){
        for(PaymentList list : paymentLists){
            PaymentList listFromTemp = tempListService.deleteFromTempDB(list);
            if(listFromTemp == null) continue;
            if(paymentListDAO.contains(list))
                paymentListDAO.update(list);
            else{
                paymentListDAO.add(listFromTemp);
            }

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

    @PostMapping("/api/test")
    public ResponseEntity<?> test(
            @RequestBody POJO[] pojo
    ){


        return  new ResponseEntity<>("test called",HttpStatus.OK);
    }


    @Autowired
    public void setHtmlDocParser(DocParser htmlDocParser) {
        this.htmlDocParser = htmlDocParser;
    }

    @Autowired
    public void setPaymentListDAO(PaymentListDAO paymentListDAO) {
        this.paymentListDAO = paymentListDAO;
    }

    @Autowired
    public void setTempListService(TempListService tempListService) {
        this.tempListService = tempListService;
    }

    static class POJO{
        String firstName;
        String lastName;
        Date date;

        public POJO() {
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }


}
