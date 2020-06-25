package com.yurets_y.payment_statistic_web.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

@TestConfiguration
@ComponentScan(basePackages = {
        "com.yurets_y.payment_statistic_web.service",
        "com.yurets_y.payment_statistic_web.util"
})
public class ServiceTestConfig {

        @Bean
        @Lazy
        public DocParser getHtmlDocParser() {

            return new HtmlDocParser();
        }

        @Bean
        @Lazy
        DocParser getXmlDocParser(){
            return new XmlDocParser();
        }

        @Bean
        @Lazy
        public TempListService getTempListService(){
            return new TempListServiceImpl(getHtmlDocParser(),getXmlDocParser());
        }


}
