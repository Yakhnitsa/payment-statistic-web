package com.yurets_y.payment_statistic_web.resources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;

@TestConfiguration
public class TestResourcesConfig {

    @Value("${test.resources.html-test-file-location}")
    public String testFileLocation;

    @Value("${test.resources.html-test-directory-location}")
    private String testDirectoryLocation;



    @Bean(name = "test-html-file")
    public File testFile() {
        System.out.println("Test file location" + testFileLocation);
        File file = new File(testFileLocation);
        if (!file.exists()) throw new RuntimeException("Тестовый файл не найден");
        return file;
    }
    @Bean(name="test-file-location")
    public String testFileLocation(){
        return testFileLocation;
    }
}
