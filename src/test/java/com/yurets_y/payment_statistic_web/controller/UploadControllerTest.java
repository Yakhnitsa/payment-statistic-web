package com.yurets_y.payment_statistic_web.controller;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Import(ControllerTestConfig.class)
@ActiveProfiles("test")
public class UploadControllerTest {
    @Autowired
    private UploadController uploadController;

    @Test
    public void autowiredTest(){
        Assert.assertNotNull(uploadController);
    }
}
