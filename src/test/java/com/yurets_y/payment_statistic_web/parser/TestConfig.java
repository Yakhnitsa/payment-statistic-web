package com.yurets_y.payment_statistic_web.parser;

import com.yurets_y.payment_statistic_web.service.DocParser;
import com.yurets_y.payment_statistic_web.service.HtmlDocParser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

        @Bean
        public DocParser getDocParser() {

            return new HtmlDocParser();
        }


}
