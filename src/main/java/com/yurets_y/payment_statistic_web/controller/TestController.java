package com.yurets_y.payment_statistic_web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.AditionalVagonInfo;
import com.yurets_y.payment_statistic_web.entity.Vagon;
import com.yurets_y.payment_statistic_web.entity.Views;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/api/test")
//    @JsonView(Views.NormalView.class)
    public ResponseEntity<?> testPostRequest(@RequestBody VagonChangesDto[] vagonInfo){


        return new ResponseEntity<>("Done",HttpStatus.OK);
    }

    @GetMapping("/api/test")
//    @JsonView(Views.NormalView.class)
    public ResponseEntity<?> testGetRequest(){


        return new ResponseEntity<>(new AditionalVagonInfo(),HttpStatus.OK);
    }

    private static class VagonChangesDto{
        public int vagonId;
        public AditionalVagonInfo changes;
    }
}
