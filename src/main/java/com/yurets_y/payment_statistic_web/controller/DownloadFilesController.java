package com.yurets_y.payment_statistic_web.controller;


import com.yurets_y.payment_statistic_web.service.PaymentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private PaymentListService paymentListService;

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
    public ResponseEntity<Resource> serveArchive(
            @RequestParam(value = "dateFrom") String from,
            @RequestParam(value = "dateUntil") String until,
            HttpServletRequest request
    ) throws ParseException, IOException {
        Date dateFrom = null;
        Date dateUntil = null;

        if ((!"".equals(from)) && (!"".equals(until))) {
            dateFrom = DATE_FORMAT.parse(from);
            dateUntil = DATE_FORMAT.parse(until);
        }
        Resource resource = paymentListService.getFilesArchiveAsResource(dateFrom,dateUntil);

        String contentType = "application/zip";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")

                .body(resource);
    }
}
