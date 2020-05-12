package com.yurets_y.payment_statistic_web.resources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;

@TestConfiguration
@PropertySource("classpath:application-test.properties")
public class TestFilesConfig {

    @Value("${test.resources.html-test-file-location}")
    public String testFileLocation;

    @Value("${test.resources.html-test-directory-location}")
    private String testDirectoryLocation;

    @Value("${test.resources.xml-test-file-location}")
    public String xmlTestFileLocation;

    @Value("${test.resources.xml-test-directory-location}")
    private String xmlTestDirectoryLocation;



    @Bean(name="test-html-file")
    public File testHtmlFile() {
        File file = new File(testFileLocation);
        if (!file.exists()) throw new RuntimeException("Тестовый файл не найден: " + testFileLocation);
        return file;
    }
    @Bean(name="test-file-location")
    public String testFileLocation(){
        return testFileLocation;
    }

    @Bean(name="test-html-directory")
    public File testHtmlDirectory() {
        File file = new File(testDirectoryLocation);
        if (!file.exists() || !file.isDirectory()) throw new RuntimeException("Путь не найден или не является директорией" + testDirectoryLocation);
        return file;
    }

    @Bean(name="test-xml-file")
    public File testXmlFile() {
        File file = new File(xmlTestFileLocation);
        if (!file.exists()) throw new RuntimeException("Тестовый файл не найден: " + xmlTestFileLocation);
        return file;
    }

    @Bean(name="test-xml-directory")
    public File testXmlDirectory() {
        File file = new File(xmlTestDirectoryLocation);
        if (!file.exists() || !file.isDirectory()) throw new RuntimeException("Путь не найден или не является директорией" + testDirectoryLocation);
        return file;
    }
}
