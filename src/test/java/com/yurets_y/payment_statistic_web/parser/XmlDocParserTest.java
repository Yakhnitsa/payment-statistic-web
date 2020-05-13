package com.yurets_y.payment_statistic_web.parser;


import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.resources.TestFilesConfig;
import com.yurets_y.payment_statistic_web.service.DocParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Import({ParserTestConfig.class,TestFilesConfig.class})
@RunWith(SpringRunner.class)
public class XmlDocParserTest {

    @Resource(name="xml-doc-parser")
    private DocParser docParser;

    @Resource(name="test-xml-file")
    private File testXmlFile;

    @Test
    public void resourceIntegrationTest(){
        assertThat(this.docParser).isNotNull();
    }

    @Test
    public void testFileExists(){
        assertThat(testXmlFile).exists();
    }

    @Test
    public void docNumberAndDateTest() throws IOException{
        PaymentList paymentList = docParser.parseFromFile(testXmlFile);
        assertThat(paymentList.getNumber()).isEqualTo(20200206);
        assertThat(paymentList.getPayerCode()).isEqualTo(8210260);
    }

    @Test
    public void openingAndClosingBalanceTest() throws IOException{
        PaymentList paymentList = docParser.parseFromFile(testXmlFile);
        assertThat(paymentList.getOpeningBalance()).isEqualTo(113231014);
        assertThat(paymentList.getPayments()).isEqualTo(308360472);
        assertThat(paymentList.getPaymentTaxes()).isEqualTo(61672095);
        assertThat(paymentList.getPaymentVsTaxes()).isEqualTo(370032567);
        assertThat(paymentList.getClosingBalance()).isEqualTo(293198447);
    }

}
