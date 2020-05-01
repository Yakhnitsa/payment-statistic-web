package com.yurets_y.payment_statistic_web.resources;


import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.parser.ParserTestConfig;
import com.yurets_y.payment_statistic_web.service.DocParser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TestConfiguration
@Import({ParserTestConfig.class, TestFilesConfig.class})
public class TestListsConfig {

    @Resource(name="html-doc-parser")
    private DocParser htmlDocParser;

    @Resource(name="test-html-file")
    private File testHtmlFile;

    @Resource(name="test-html-directory")
    private File testHtmlDirectory;

    @Bean("test-list")
    public PaymentList testPaymentList() throws IOException {
        return htmlDocParser.parseFromFile(testHtmlFile);
    }

    @Bean("test-lists")
    public List<PaymentList> testPaymentLists() {
        List<PaymentList> paymentLists = new ArrayList<>();
        for (File file: testHtmlDirectory.listFiles()){
            try {
                paymentLists.add(htmlDocParser.parseFromFile(file));
            } catch (IOException e) {
                System.out.println("Ошибка разбора файла: " +  file);
                e.printStackTrace();
            }
        }
        return paymentLists;
    }

}
