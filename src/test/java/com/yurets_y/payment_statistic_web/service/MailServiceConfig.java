package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.service.mail_service.MailService;
import com.yurets_y.payment_statistic_web.service.parser_services.DocParser;
import com.yurets_y.payment_statistic_web.service.parser_services.HtmlDocParser;
import com.yurets_y.payment_statistic_web.service.parser_services.XmlDocParser;
import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:/mail.properties")
//@Import(ServiceTestConfig.class)
@ComponentScan(
        basePackages = {"com.yurets_y.payment_statistic_web.service.mail_service"}/*,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = MailService.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TempListService.class)
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = PaymentDetailsService.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ServiceTestConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = PaymentListService.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = StatisticService.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserService.class),
        }*/
)
public class MailServiceConfig {

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
