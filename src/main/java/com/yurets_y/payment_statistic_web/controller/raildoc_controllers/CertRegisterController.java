package com.yurets_y.payment_statistic_web.controller.raildoc_controllers;


import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.dto.JsonPage;
import com.yurets_y.payment_statistic_web.entity.AditionalVagonInfo;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.Views;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsService;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsSpecification;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.VagonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class CertRegisterController {

    private VagonService vagonService;

    private RailroadDocumentsService documentsService;

    private RailroadDocumentsSpecification docSpec;

    public CertRegisterController(RailroadDocumentsService documentsService, RailroadDocumentsSpecification docSpec) {
        this.documentsService = documentsService;
        this.docSpec = docSpec;
    }

    @GetMapping("/api/documents/certificates")
    @Secured({"ROLE_VIEWER","ROLE_EDITOR","ROLE_ADMIN"})
    @JsonView(Views.NormalView.class)
    public ResponseEntity<?> uploadSingleList(
            @RequestParam(required = false, defaultValue = "0") Integer currentPage,
            @RequestParam(required = false, defaultValue = "50") Integer itemsPerPage,
            @RequestParam(required = false, defaultValue = "dateStamp") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam(required = false) Integer stationFromCode,
            @RequestParam(required = false) Integer stationToCode,
            @RequestParam(required = false) Integer cargoSenderCode,
            @RequestParam(required = false) Integer cargoReceiverCode,
            @RequestParam(required = false) Integer docNumber,
            @RequestParam(required = false) Integer vagonNumber,
            @RequestParam(required = false) String cargoCode
    ){
        Pageable pageable = getPageable(currentPage, itemsPerPage, sortBy,sortDirection);

        Specification<RailroadDocument> specification = docSpec.docDateSpecification(dateFrom,dateUntil);
        specification = specification.and(docSpec.sendStationSpec(stationFromCode));
        specification = specification.and(docSpec.receiveStationSpec(stationToCode));
        specification = specification.and(docSpec.senderCodeSpec(cargoSenderCode));
        specification = specification.and(docSpec.receiverCodeSpec(cargoReceiverCode));
        specification = specification.and(docSpec.docNumberSpecification(docNumber));
        specification = specification.and(docSpec.vagonNumberSpec(vagonNumber));
        specification = specification.and(docSpec.cargoCodeSpec(cargoCode));

        Page<RailroadDocument> railDocsPage = documentsService.getAllBySpecification(specification,pageable);
        JsonPage<RailroadDocument> jsonPage = new JsonPage<>(railDocsPage, pageable);

        return new ResponseEntity<>(jsonPage,HttpStatus.OK);
    }

    @PostMapping("/api/documents/certificates")
    @Secured({"ROLE_EDITOR","ROLE_ADMIN"})
    @JsonView(Views.NormalView.class)
    public ResponseEntity<?> postChanges(@RequestBody VagonChangesDto[] changes){

        List<AditionalVagonInfo> appliedChanges = new ArrayList<>();
        for(VagonChangesDto dto: changes){
            appliedChanges.add(vagonService.addVagonInfoToVagon(dto.getVagonId(),dto.getChanges()));
        }

        return new ResponseEntity<>(appliedChanges,HttpStatus.OK);
    }

    @Autowired
    public void setVagonService(VagonService vagonService) {
        this.vagonService = vagonService;
    }

    private Pageable getPageable(int page, int size, String sort, String direction){
        return "ASC".equalsIgnoreCase(direction) ? PageRequest.of(page,size, Sort.by(sort).ascending())
                : PageRequest.of(page,size, Sort.by(sort).ascending().descending());
    }

    private static class VagonChangesDto{
        private Long vagonId;
        private AditionalVagonInfo changes;

        public Long getVagonId() {
            return vagonId;
        }

        public void setVagonId(Long vagonId) {
            this.vagonId = vagonId;
        }

        public AditionalVagonInfo getChanges() {
            return changes;
        }

        public void setChanges(AditionalVagonInfo changes) {
            this.changes = changes;
        }
    }
}
