package com.yurets_y.payment_statistic_web.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceTestConfig {

        @Bean
        public DocParser getHtmlDocParser() {

            return new HtmlDocParser();
        }
        @Bean DocParser getXmlDocParser(){
            return new XmlDocParser();
        }

        @Bean
        public TempListService getTempListService(){
            return new TempListServiceImpl(getHtmlDocParser(),getXmlDocParser());
        }


}
