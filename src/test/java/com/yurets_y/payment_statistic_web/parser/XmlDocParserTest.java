package com.yurets_y.payment_statistic_web.parser;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.resources.TestFilesConfig;
import com.yurets_y.payment_statistic_web.service.parser_services.DocParser;
import com.yurets_y.payment_statistic_web.service.parser_services.HtmlDocParser;
import com.yurets_y.payment_statistic_web.service.parser_services.XmlDocParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({XmlDocParserTestConfig.class})
@RunWith(SpringRunner.class)
public class XmlDocParserTest {

    Logger logger = LoggerFactory.getLogger(XmlDocParserTest.class);

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

    @Test
    public void parseDeparturePaymentTest() throws IOException{
        PaymentList paymentList = docParser.parseFromFile(testXmlFile);
        long departurePayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Відправлення"))
                .mapToLong(PaymentDetails::getPayment).sum();

        long internDeparturePayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Відправлення - міжнародне сполучення"))
                .mapToLong(PaymentDetails::getPayment).sum();

        long deliverTotalPayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Прибуття"))
                .mapToLong(PaymentDetails::getTotalPayment).sum();

        assertThat(departurePayment).isEqualTo(89018200);
        assertThat(internDeparturePayment).isEqualTo(208069900);
        assertThat(deliverTotalPayment).isEqualTo(0);
    }

    @Test
    public void parseStationPaymentTest() throws IOException{
        PaymentList paymentList = docParser.parseFromFile(testXmlFile);
        long vagUsagePayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Відомості плати за користування вагонами"))
                .mapToLong(PaymentDetails::getPayment).sum();

        long cumulativeCardsPayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Накопичувальні карточки"))
                .mapToLong(PaymentDetails::getPayment).sum();

        assertThat(vagUsagePayment).isEqualTo(8554760);
        assertThat(cumulativeCardsPayment).isEqualTo(2717612);
    }

    @Test
    public void parsePaymentsTest() throws IOException{
        PaymentList paymentList = docParser.parseFromFile(testXmlFile);
        long totalPayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Платіжні доручення"))
                .mapToLong(PaymentDetails::getTotalPayment).sum();
        assertThat(totalPayment).isEqualTo(550000000);
    }

    @Test
    public void complexXmlDocParserTest() throws IOException{
        File file = new File("src/test/resources/test_files/xml/8210260_20200930.xml");
        PaymentList paymentList = docParser.parseFromFile(file);

        logger.info("Doc number and code test");
        assertThat(paymentList.getNumber()).isEqualTo(20200930);
        assertThat(paymentList.getPayerCode()).isEqualTo(8210260);

        logger.info("Opening and closing balances test");
        assertThat(paymentList.getOpeningBalance()).isEqualTo(247660659);
        assertThat(paymentList.getPayments()).isEqualTo(1067965828);
        assertThat(paymentList.getPaymentTaxes()).isEqualTo(48409806);
        assertThat(paymentList.getPaymentVsTaxes()).isEqualTo(1116375634);
        assertThat(paymentList.getClosingBalance()).isEqualTo(-718714975);


        logger.info("Payment details tests...");

        long departurePayment = getPaymDetailsSumByType(paymentList.getPaymentDetailsList(),"Відправлення");
        assertThat(departurePayment).isEqualTo(35623600);

        long internDeparturePayment = getPaymDetailsSumByType(paymentList.getPaymentDetailsList(),"Відправлення - міжнародне сполучення");
        assertThat(internDeparturePayment).isEqualTo(195847970);

        long cumCards = getPaymDetailsSumByType(paymentList.getPaymentDetailsList(),"Накопичувальні карточки");
        assertThat(cumCards).isEqualTo(3467310);

        long vagPayments = getPaymDetailsSumByType(paymentList.getPaymentDetailsList(),"Відомості плати за користування вагонами");
        assertThat(vagPayments).isEqualTo(1179690);

        long infoMessages = getPaymDetailsSumByType(paymentList.getPaymentDetailsList(),"Інформаційні повідомлення");
        assertThat(infoMessages).isEqualTo(827820000);



//        long deliverTotalPayment = paymentList.getPaymentDetailsList()
//                .stream()
//                .filter(pd -> pd.getType().equals("Прибуття"))
//                .mapToLong(PaymentDetails::getTotalPayment).sum();
//
//        assertThat(deliverTotalPayment).isEqualTo(0);






    }

    private long getPaymDetailsSumByType(List<PaymentDetails> pdList, String paymentsType){
        return pdList.stream()
                .filter(pd -> pd.getType().equals(paymentsType))
                .mapToLong(PaymentDetails::getPayment).sum();
    }

}

class XmlDocParserTestConfig {

    private final String XML_FILE_LOCATION = "src/test/resources/test_files/xml/per_06022020_100001_8210260.xml";
    private final String XML_FILES_DIRECTORY_LOCATION = "src/test/resources/test_files/xml";

    @Bean(name = "xml-doc-parser")
    public DocParser getXmlDocParser() {
        return new XmlDocParser();
    }

    @Bean(name="test-xml-file")
    public File testXmlFile() {
        File file = new File(XML_FILE_LOCATION);
        if (!file.exists())
            throw new RuntimeException("Тестовый файл не найден: " + XML_FILE_LOCATION);
        return file;
    }

    @Bean(name="test-xml-directory")
    public File testXmlDirectory() {
        File file = new File(XML_FILES_DIRECTORY_LOCATION);
        if (!file.exists() || !file.isDirectory())
            throw new RuntimeException("Путь не найден или не является директорией" + XML_FILES_DIRECTORY_LOCATION);
        return file;
    }
}


