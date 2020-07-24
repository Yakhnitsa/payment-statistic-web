package com.yurets_y.payment_statistic_web.service;


import com.yurets_y.payment_statistic_web.service.mail_service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(MailServiceConfig.class)
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Test
    public void resourceInjectionTest(){
        assertThat(mailService).isNotNull();
    }

    @Test
    public void readFromMailTest(){
        List<MultipartFile> files = mailService.readFromMail();
    }

}
