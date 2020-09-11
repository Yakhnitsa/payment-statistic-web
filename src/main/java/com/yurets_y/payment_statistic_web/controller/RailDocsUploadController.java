package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.Views;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RailDocsUploadController {

    @PostMapping("/api/upload-single-raildoc")
    @Secured({"ROLE_EDITOR"})
    public Object uploadSingleList(
            @RequestParam("file") MultipartFile file
    ){
        if(file.isEmpty()){
            return new ResponseEntity<String>("No file!",HttpStatus.OK);
        }
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);

        return getMockedFile(fileName);
    }

    @PostMapping("/api/upload-multiple-raildocs")
    @Secured({"ROLE_EDITOR"})
    public ResponseEntity<?> uploadMultiple(
            @RequestParam("files") MultipartFile[] files
    ){
        if(files.length == 0){
            return new ResponseEntity<String>("No file!!!",HttpStatus.NO_CONTENT);
        }
        List<String> extensions = Arrays.asList("text/html","text/xml");

        for(MultipartFile file: files){
            String type = file.getContentType();
            System.out.println(type);
        }
        return null;
    }

    private Map<String,String> getMockedFile(String fileName){
        Map<String,String> map = new HashMap<>();
        map.put("number",fileName);
        return map;
    }
}
