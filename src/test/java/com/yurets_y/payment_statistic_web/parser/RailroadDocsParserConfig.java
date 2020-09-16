package com.yurets_y.payment_statistic_web.parser;

import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@TestConfiguration
@ComponentScan(
        basePackageClasses={RailroadDocumentsParser.class})
class RailroadDocsParserConfig {

    @Bean("test-file")
    File testFile(){
        return new File("src/test/resources/test_files/railroad-documents/33230095.xml");
    }
//
//    @Bean
//    RailroadDocumentsParser getDocParser(){
//        return new RailroadDocumentParserImpl();
//    }


}
