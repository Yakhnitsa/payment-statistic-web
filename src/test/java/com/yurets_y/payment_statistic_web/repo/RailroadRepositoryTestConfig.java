package com.yurets_y.payment_statistic_web.repo;


import com.yurets_y.payment_statistic_web.parser.RailroadDocsParserConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import javax.swing.text.html.parser.DocumentParser;
import java.io.File;

@Configuration
@Import(RailroadDocsParserConfig.class)
@TestPropertySource(locations= "classpath:db_properties/inmemory-db.properties")
@EntityScan(basePackages = "com.yurets_y.payment_statistic_web.entity")
@ComponentScan(
//        basePackages = "com.yurets_y.payment_statistic_web.repo",
        basePackageClasses = {RailroadDocumentsRepo.class}
)
public class RailroadRepositoryTestConfig {





}
