package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.Views;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
public class RailDocsUploadController {

    @PostMapping("/api/documents/upload-single-raildoc")
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

    @PostMapping("/api/documents/save-docs-to-main-db")
    @Secured({"ROLE_EDITOR"})
    public ResponseEntity<?> saveDocumentsToMainDb(
            @RequestBody Object[] payload
    ){
        System.out.println(payload);
        return null;
    }

    @DeleteMapping("/api/documents/delete-docs-from-temp-db")
    @Secured({"ROLE_EDITOR"})
    public ResponseEntity<?> deleteDocumentsFromTempDb(
            @RequestBody Object[] payload
    )
    {
        System.out.println(payload);
        return null;
    }

    private Map<String,String> getMockedFile(String fileName){
        Map<String,String> map = new HashMap<>();
        map.put("number",fileName);
        return map;
    }
}
