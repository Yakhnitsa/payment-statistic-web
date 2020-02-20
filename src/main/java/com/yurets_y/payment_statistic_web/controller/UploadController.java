package com.yurets_y.payment_statistic_web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @PostMapping("/api/upload-single")
    public ResponseEntity<UploadResult> uploadSingle(
            @RequestParam("file") MultipartFile file

            ){
        if(file.isEmpty()){
            String fileName = "no file";
            return new ResponseEntity<UploadResult>(new UploadResult(fileName,null),HttpStatus.OK);
        }

        String fileName = file.getOriginalFilename();

        return new ResponseEntity<UploadResult>(new UploadResult(fileName,null),HttpStatus.OK);
    }
    @PostMapping("/api/upload-multiple")
    public ResponseEntity<UploadResult> uploadMultiple(
            @RequestParam("files") MultipartFile[] files

    ){

        System.out.println(files.length);
//        if(files.isEmpty()){
//            String fileName = "no file";
//            return new ResponseEntity<UploadResult>(new UploadResult(fileName,null),HttpStatus.OK);
//        }

//        String fileName = files.getOriginalFilename();

        return new ResponseEntity<UploadResult>(new UploadResult("Files uploaded!!",null),HttpStatus.OK);
    }


}
