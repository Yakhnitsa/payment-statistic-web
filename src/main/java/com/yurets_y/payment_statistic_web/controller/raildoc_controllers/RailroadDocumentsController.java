package com.yurets_y.payment_statistic_web.controller.raildoc_controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.dto.JsonPage;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class RailroadDocumentsController {
    RailroadDocumentsService documentsService;

    @GetMapping("/api/documents/railroad-documents")
    @Secured({"ROLE_EDITOR"})
    @JsonView(Views.NormalView.class)
    public ResponseEntity<?> uploadSingleList(
            @RequestParam(required = false, defaultValue = "0") Integer currentPage,
            @RequestParam(required = false, defaultValue = "100") Integer elementsCount,
            @RequestParam(required = false, defaultValue = "dateStamp") String sortBy,
            @RequestParam(required = false) Integer stationFromCode,
            @RequestParam(required = false) Integer stationToCode,
            @RequestParam(required = false) Integer cargoSenderCode,
            @RequestParam(required = false) Integer cargoReceiverCode,
            @RequestParam(required = false) Integer tarifPayerCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam(required = false) Integer docNumber,
            @RequestParam(required = false) Integer vagonNumber,
            @RequestParam(required = false) String cargoCode
            ){
        Pageable pageable = getPageable(currentPage, elementsCount, sortBy);

        Page<RailroadDocument> railDocsPage = documentsService.getAll(pageable);
        JsonPage<RailroadDocument> jsonPage = new JsonPage<>(railDocsPage, pageable);
//        TODO Возможны проблемы с кодом груза

        return new ResponseEntity<>(jsonPage,HttpStatus.OK);
    }

    @Autowired
    public void setDocumentsService(RailroadDocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    private Pageable getDefaultPageable(){
        return PageRequest.of(0,100);
    }

    private Pageable getPageable(int page, int size, String sort){
        return PageRequest.of(page,size, Sort.by(sort));
    }
}
