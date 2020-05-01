package com.yurets_y.payment_statistic_web.parser;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.service.DocParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ParserTestConfig.class)
@RunWith(SpringRunner.class)
public class HtmlDocParserTest {

    @Autowired
    private DocParser docParser;

    private File testFile;


    @Test
    public void resourceIntegrationTest(){
        assertThat(this.docParser).isNotNull();
    }

    @Test
    public void testFileExists(){
        assertThat(getTestFile()).exists();
    }

    @Test
    public void parseDocDateAndNumberTest() throws IOException {
        File file = getTestFile();
        PaymentList paymentList = docParser.parseFromFile(file);
        assertThat(paymentList.getNumber()).isEqualTo(20200225);
        assertThat(paymentList.getPayerCode()).isEqualTo(8210260);
    }

    @Test
    public void parseOpeningBalanceTest() throws IOException {
        File file = getTestFile();
        PaymentList paymentList = docParser.parseFromFile(file);

        assertThat(paymentList.getOpeningBalance()).isEqualTo(314230959);
    }
    @Test
    public void parseClosingBalanceTest() throws IOException {
        File file = getTestFile();
        PaymentList paymentList = docParser.parseFromFile(file);
        assertThat(paymentList.getClosingBalance()).isEqualTo(286605399);
    }
    @Test
    public void totalPaymentAndTaxesTest() throws IOException {
        File file = getTestFile();
        PaymentList paymentList = docParser.parseFromFile(file);
        assertThat(paymentList.getPayments()).isEqualTo(274421300);
        assertThat(paymentList.getPaymentTaxes()).isEqualTo(53204260);
        assertThat(paymentList.getPaymentVsTaxes()).isEqualTo(327625560);
    }

    @Test
    public void parseDeparturePaymentTest() throws IOException{
        File file = getTestFile();
        PaymentList paymentList = docParser.parseFromFile(file);
        long departurePayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Вiдправлення"))
                .mapToLong(PaymentDetails::getPayment).sum();
        long internDeparturePayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Вiдправлення - мiжнародне сполучення"))
                .mapToLong(PaymentDetails::getPayment).sum();
        long deliverTotalPayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Прибуття"))
                .mapToLong(PaymentDetails::getTotalPayment).sum();

        assertThat(departurePayment).isEqualTo(58923390);
        assertThat(internDeparturePayment).isEqualTo(181641800);
        assertThat(deliverTotalPayment).isEqualTo(15423432);
    }

    @Test
    public void parseStationPaymentTest() throws IOException{
        File file = getTestFile();
        PaymentList paymentList = docParser.parseFromFile(file);
        long vagUsagePayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Вiдомостi плати за користування вагонами"))
                .mapToLong(PaymentDetails::getPayment).sum();

        long cumulativeCardsPayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Накопичувальні карточки"))
                .mapToLong(PaymentDetails::getPayment).sum();
        assertThat(vagUsagePayment).isEqualTo(12521570);
        assertThat(cumulativeCardsPayment).isEqualTo(528520);
    }

    @Test
    public void parsePaymentsTest() throws IOException{
        File file = getTestFile();
        PaymentList paymentList = docParser.parseFromFile(file);
        long totalPayment = paymentList.getPaymentDetailsList()
                .stream()
                .filter(pd -> pd.getType().equals("Платіжні доручення"))
                .mapToLong(PaymentDetails::getTotalPayment).sum();
        assertThat(totalPayment).isEqualTo(300000000);
    }

    private File getTestFile(){
        if(testFile != null) return testFile;
        testFile = new File("src/test/resources/test_files/26022020_040331.html");

        return testFile;
    }

}
