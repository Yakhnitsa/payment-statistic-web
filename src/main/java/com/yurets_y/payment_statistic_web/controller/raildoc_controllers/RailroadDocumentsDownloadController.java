package com.yurets_y.payment_statistic_web.controller.raildoc_controllers;


import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import com.yurets_y.payment_statistic_web.service.PaymentListService;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/api/raildocs-download")
public class RailroadDocumentsDownloadController {

    private final String WRONG_PARAMETERS_MESSAGE = "application.controller.void-request-param";

    private RailroadDocumentsService documentsService;

    private MessageProvider messageProvider;

    @Autowired
    public RailroadDocumentsDownloadController(RailroadDocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    @GetMapping("/{type}")
    @ResponseBody
    public ResponseEntity<Resource> getFileById(
            @PathVariable String type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date  dateStamp,
            @RequestParam Integer docNumber,
            HttpServletRequest request
    ) throws FileNotFoundException {
        RailroadDocument document = documentsService.getById(new RailroadDocumentId(docNumber,dateStamp));
        if (document == null) return ResponseEntity.noContent().build();

        String fileName = type.equalsIgnoreCase("xml") ? document.getXmlBackupFilePath() :
                type.equalsIgnoreCase("pdf") ? document.getPdfBackupFilePath() : null;
        if(fileName == null) return ResponseEntity.noContent().build();

        Resource resource = documentsService.getFileAsResource(fileName);

        String resourceName = getFileName(document,type);

        String contentType = "application/octet-stream";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resourceName + "\"")
                .body(resource);
    }

    private String getFileName(RailroadDocument document, String type) {
        Date date = document.getDocDate();
        String station = document.getSendStation().getRusName();
        int vagCount = document.getVagonCount();
        int railroadCode = document.getDocNumber();
        int receiveCode = document.getCargoReceiver().getRailroadCode();
        return String.format("%1$td_%1$tm_%1$tY %2$s %3$d ваг ЖД %4$s (%5$s).%6s",
                date, station, vagCount, railroadCode, receiveCode, type);
    }

/*    @GetMapping("/archive")
    @ResponseBody
    public ResponseEntity<?> serveArchive(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam Integer payerCode,
            HttpServletRequest request
    ) throws ParseException, IOException {
        if (dateFrom == null || dateUntil == null) {
            String message = messageProvider.get("application.controller.void-request-param");
            return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
        }
        Resource resource = paymentListService.getFilesArchiveAsResource(dateFrom,dateUntil,payerCode);

        String contentType = "application/zip";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        String arcName = String
                .format("archive_%1s_%2$tY_%2$tm_%2$td__%3$tY_%3$tm_%3$td.zip",payerCode, dateFrom, dateUntil);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arcName + "\"")
                .body(resource);
    }*/

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

}
