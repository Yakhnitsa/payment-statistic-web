package com.yurets_y.payment_statistic_web.controller.raildoc_controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.dto.JsonPage;
import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class RailroadDocumentsController {
    RailroadDocumentsService documentsService;

    @GetMapping("/api/documents/railroad-documents")
    @Secured({"ROLE_EDITOR"})
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> uploadSingleList(Pageable pageable){
        pageable = pageable == null? getDefaultPageable() : pageable;

        Page<RailroadDocument> railDocsPage = documentsService.getAll(pageable);
        JsonPage<RailroadDocument> jsonPage = new JsonPage<>(railDocsPage, pageable);

        return new ResponseEntity<>(jsonPage,HttpStatus.OK);
    }

    @Autowired
    public void setDocumentsService(RailroadDocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    private Pageable getDefaultPageable(){
        return PageRequest.of(0,100);
    }
}
