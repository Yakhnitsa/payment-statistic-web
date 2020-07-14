package com.yurets_y.payment_statistic_web.controller;


import com.yurets_y.payment_statistic_web.service.PaymentListService;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/download")
public class DownloadFilesController {

    private final String WRONG_PARAMETERS_MESSAGE = "application.controller.void-request-param";

    private PaymentListService paymentListService;

    private MessageProvider messageProvider;

    @Autowired
    public DownloadFilesController(PaymentListService paymentListService) {
        this.paymentListService = paymentListService;
    }

    @GetMapping("/file/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
            @PathVariable String fileName,
            @RequestParam String file,
            HttpServletRequest request
    ) throws FileNotFoundException {

        Resource resource = paymentListService.getFileAsResource(fileName);

        String contentType = "application/octet-stream";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/archive")
    @ResponseBody
    public ResponseEntity<?> serveArchive(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            HttpServletRequest request
    ) throws ParseException, IOException {
        if (dateFrom == null || dateUntil == null) {
            String message = messageProvider.get("application.controller.void-request-param");
            return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
        }
        Resource resource = paymentListService.getFilesArchiveAsResource(dateFrom,dateUntil);

        String contentType = "application/zip";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        String arcName = String.format("archive_%1$tY_%1$tm_%1$td__%2$tY_%2$tm_%2$td.zip", dateFrom,dateUntil);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arcName + "\"")
                .body(resource);
    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

}
