package com.yurets_y.payment_statistic_web.controller.raildoc_controllers;


import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.ExcelDocumentWriter;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsService;
import com.yurets_y.payment_statistic_web.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/api/raildocs-download")
public class RailroadDocumentsDownloadController {

    private final String WRONG_PARAMETERS_MESSAGE = "application.controller.void-request-param";

    private RailroadDocumentsService documentsService;

    private ExcelDocumentWriter excelDocWriter;

    private MessageProvider messageProvider;

    @Autowired
    public RailroadDocumentsDownloadController(RailroadDocumentsService documentsService, ExcelDocumentWriter excelDocWriter) {
        this.documentsService = documentsService;
        this.excelDocWriter = excelDocWriter;
    }

    @GetMapping("/file")
    @ResponseBody
    public ResponseEntity<Resource> getFileById(
            @RequestParam String fileType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date  dateStamp,
            @RequestParam Integer docNumber,
            HttpServletRequest request
    ) throws FileNotFoundException {
        RailroadDocument document = documentsService.getById(new RailroadDocumentId(docNumber,dateStamp));
        if (document == null) return ResponseEntity.noContent().build();

        String fileName = fileType.equalsIgnoreCase("xml") ? document.getXmlBackupFilePath() :
                fileType.equalsIgnoreCase("pdf") ? document.getPdfBackupFilePath() : null;
        if(fileName == null) return ResponseEntity.noContent().build();

        Resource resource = documentsService.getFileAsResource(fileName);

        String resourceName = getFileNameASCII(document,fileType);

        String contentType = "application/octet-stream";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }
        String contendDispString = "attachment; filename=" + resourceName;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contendDispString)
                .body(resource);
    }



    @PostMapping("/archive")
    @ResponseBody
    public ResponseEntity<?> downloadArchive(
            @RequestBody RailroadDocumentId[] documentsIds,
            HttpServletRequest request
    ) {

        Resource resource = null;
        try {
            resource = documentsService.getFilesArchiveAsResource(Arrays.asList(documentsIds));
            String contentType = "application/zip";
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                System.out.println("Could not determine file type.");
            }

            String arcName = "documents_archive.zip";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(resource.contentLength())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arcName + "\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }


    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    private String getFileNameASCII(RailroadDocument document, String type) {
        return String.format("%1$td_%1$tm_%1$tY__%2$s_.%3$s",
                document.getDateStamp(), document.getDocNumber(),type);
    }



}
