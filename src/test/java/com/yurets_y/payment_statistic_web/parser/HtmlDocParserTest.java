package com.yurets_y.payment_statistic_web.parser;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.resources.TestFilesConfig;
import com.yurets_y.payment_statistic_web.service.parser_services.DocParser;
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
public class HtmlDocParserTest {

    @Resource(name="html-doc-parser")
    private DocParser docParser;

    @Resource(name="test-html-file")
    private File testHtmlFile;

    @Resource(name="corrupted-html-dir")
    private File corruptedHtmlDir;


    @Test
    public void resourceIntegrationTest(){
        assertThat(this.docParser).isNotNull();
    }

    @Test
    public void testFileExists(){
        assertThat(testHtmlFile).exists();
    }

    @Test
    public void parseDocDateAndNumberTest() throws IOException {
        PaymentList paymentList = docParser.parseFromFile(testHtmlFile);
        assertThat(paymentList.getNumber()).isEqualTo(20200111);
        assertThat(paymentList.getPayerCode()).isEqualTo(8210260);
    }

    @Test
    public void parseOpeningAndClosingBalanceTest() throws IOException {
        PaymentList paymentList = docParser.parseFromFile(testHtmlFile);

        assertThat(paymentList.getOpeningBalance()).isEqualTo(102274877);
        assertThat(paymentList.getClosingBalance()).isEqualTo(585993051);
    }

    @Test
    public void totalPaymentAndTaxesTest() throws IOException {
        PaymentList paymentList = docParser.parseFromFile(testHtmlFile);
        assertThat(paymentList.getPayments()).isEqualTo(305810650);
        assertThat(paymentList.getPaymentTaxes()).isEqualTo(60471176);
        assertThat(paymentList.getPaymentVsTaxes()).isEqualTo(366281826);
    }

    @Test
    public void parseDeparturePaymentTest() throws IOException{
        PaymentList paymentList = docParser.parseFromFile(testHtmlFile);
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
                .filter(pd -> pd.getType().equals("Прибуття - імпорт"))
                .mapToLong(PaymentDetails::getPayment).sum();

        assertThat(departurePayment).isEqualTo(98129890);
        assertThat(internDeparturePayment).isEqualTo(179724800);
        assertThat(deliverTotalPayment).isEqualTo(3444220);
    }

    @Test
    public void parseStationPaymentTest() throws IOException{
        PaymentList paymentList = docParser.parseFromFile(testHtmlFile);
        long vagUsagePayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Відомості плати за користування вагонами"))
                .mapToLong(PaymentDetails::getPayment).sum();

        long cumulativeCardsPayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Накопичувальні карточки"))
                .mapToLong(PaymentDetails::getPayment).sum();
        assertThat(vagUsagePayment).isEqualTo(23264750);
        assertThat(cumulativeCardsPayment).isEqualTo(1236440);
    }

    @Test
    public void parsePaymentsTest() throws IOException{
        PaymentList paymentList = docParser.parseFromFile(testHtmlFile);
        long totalPayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Платіжні доручення"))
                .mapToLong(PaymentDetails::getTotalPayment).sum();
        assertThat(totalPayment).isEqualTo(850000000);
    }

    @Test
    public void parseCorruptedFilesTest() throws IOException {
        for(File file: corruptedHtmlDir.listFiles()){
            docParser.parseFromFile(file);
        }
    }

}
