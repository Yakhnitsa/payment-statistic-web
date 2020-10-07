package com.yurets_y.payment_statistic_web.controller.raildoc_controllers;

import com.fasterxml.jackson.annotation.JsonView;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
public class RailroadDocumentsController {
    RailroadDocumentsService documentsService;

    @PostMapping("/api/documents/railroad-documents")
    @Secured({"ROLE_EDITOR"})
    @JsonView(Views.ShortView.class)
    public ResponseEntity<?> uploadSingleList(Pageable pageable){
        pageable = pageable == null? getDefaultPageable() : pageable;

        Page<RailroadDocument> railDocsPage = documentsService.getAll(pageable);
        return new ResponseEntity<>(railDocsPage,HttpStatus.OK);
    }

    @Autowired
    public void setDocumentsService(RailroadDocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    private Pageable getDefaultPageable(){
        return PageRequest.of(0,100);
    }
}
