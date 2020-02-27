package com.yurets_y.paymentstatisticweb.service;

import com.yurets_y.payment_statistic_web.service.DocParser;
import com.yurets_y.payment_statistic_web.service.HtmlDocParser;
import com.yurets_y.payment_statistic_web.service.TempListService;
import com.yurets_y.payment_statistic_web.service.TempListServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceTestConfig {

        @Bean
        public DocParser getDocParser() {

            return new HtmlDocParser();
        }

        @Bean
        public TempListService getTempListService(){
            return new TempListServiceImpl();
        }


}
