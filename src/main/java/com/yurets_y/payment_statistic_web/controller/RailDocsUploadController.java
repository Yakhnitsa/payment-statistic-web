package com.yurets_y.payment_statistic_web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsService;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.TempRailroadDocsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController()
public class RailDocsUploadController {

    private RailroadDocumentsService docService;

    private TempRailroadDocsService tempDocService;

    @Autowired
    public RailDocsUploadController(RailroadDocumentsService docService, TempRailroadDocsService tempDocService) {
        this.docService = docService;
        this.tempDocService = tempDocService;
    }

    @PostMapping("/api/documents/upload-single-raildoc")
    @Secured({"ROLE_EDITOR"})
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> uploadSingleList(
            @RequestParam("file") MultipartFile file
    ){
        if(file.isEmpty()){
            return new ResponseEntity<String>("No file!",HttpStatus.OK);
        }
        RailroadDocument doc = tempDocService.putToTempDB(file);

        return new ResponseEntity<>(doc,HttpStatus.OK);
    }

    @GetMapping("/api/documents/get-temp-uploaded")
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> getAllFromTemp(){
        Collection<RailroadDocument> tempDocs = tempDocService.getAllFromTempDB();
        return new ResponseEntity<>(tempDocs,HttpStatus.OK);
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
