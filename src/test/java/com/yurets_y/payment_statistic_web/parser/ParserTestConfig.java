package com.yurets_y.payment_statistic_web.parser;

import com.yurets_y.payment_statistic_web.resources.TestFilesConfig;
import com.yurets_y.payment_statistic_web.service.DocParser;
import com.yurets_y.payment_statistic_web.service.HtmlDocParser;
import com.yurets_y.payment_statistic_web.service.XmlDocParser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
public class ParserTestConfig {

    @Bean(name = "html-doc-parser")
    public DocParser getDocParser() {
        return new HtmlDocParser();
    }

    @Bean(name = "xml-doc-parser")
    public DocParser getXmlDocParser() {
        return new XmlDocParser();
    }
}
