package com.yurets_y.payment_statistic_web.controller.raildoc_controllers;


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
    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
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
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> saveDocumentsToMainDb(
            @RequestBody RailroadDocument[] documents
    ){
        List<RailroadDocument> savedDocuments = new ArrayList<>();
        for (RailroadDocument docFromRequest : documents) {
            if(isDocCorrect(docFromRequest)){
                RailroadDocument docFromTempDb = tempDocService.deleteFromTempDB(docFromRequest);
                docService.add(docFromTempDb);
                savedDocuments.add(docFromTempDb);
            }
            //TODO проверить в работе
//            else{
//                return new ResponseEntity<>(docFromRequest, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
//            }

        }
        Map<String, Collection<RailroadDocument>> responseMap = new HashMap<>();
        responseMap.put("saved-docs",savedDocuments);
        responseMap.put("temp-docs",tempDocService.getAllFromTempDB());

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @DeleteMapping("/api/documents/delete-docs-from-temp-db")
    @Secured({"ROLE_EDITOR"})
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> deleteDocumentsFromTempDb(
            @RequestBody RailroadDocument[] payload
    )
    {
        for(RailroadDocument document: payload){
//            System.out.println(document);
            tempDocService.deleteFromTempDB(document);
        }
        return new ResponseEntity<>(tempDocService.getAllFromTempDB(),HttpStatus.OK);
    }

    private Map<String,String> getMockedFile(String fileName){
        Map<String,String> map = new HashMap<>();
        map.put("number",fileName);
        return map;
    }

    private boolean isDocCorrect(RailroadDocument railroadDocument){
        return (railroadDocument.getDocNumber() != -1) && (railroadDocument.getDateStamp() != null);
    }
}
