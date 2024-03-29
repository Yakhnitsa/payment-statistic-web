package com.yurets_y.payment_statistic_web.parser;

import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.io.File;

@TestConfiguration
@ComponentScan(
        useDefaultFilters = false,
        basePackages="com.yurets_y.payment_statistic_web.service.parser_services",
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            classes = RailroadDocumentsParser.class)
)
public class RailroadDocsParserConfig {

    @Bean("test-file")
    File testFile(){
        return new File("src/test/resources/test_files/railroad-documents/33230095.xml");
    }
    @Bean("corrupted-test-file")
    File corruptedTestFile(){
        return new File("src/test/resources/test_files/railroad-documents/corrupted/33248824.xml");
    }
//
//    @Bean
//    RailroadDocumentsParser getDocParser(){
//        return new RailroadDocumentParserImpl();
//    }


}
