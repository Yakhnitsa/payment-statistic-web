package com.yurets_y.payment_statistic_web.repo;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest()
@TestPropertySource(locations= "classpath:db_properties/immutable-db.properties")
@Import({RepositoryConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PaymentDetailsRepoTest {
    @Autowired
    private PaymentDetailsRepo paymentDetailsRepo;

    SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void resourceInjectionTest(){
        assertThat(paymentDetailsRepo).isNotNull();
    }

    @Test
    public void readFromDbTest(){
        List<PaymentDetails> list = paymentDetailsRepo.findAll();
        list.size();
    }

    @Test
    public void readFromDbWithParametersTest() throws ParseException {
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-20");
        Integer payerCode = 8210260;
        String paymentType = "Відправлення";

        List<PaymentDetails> paymentDetailsList = paymentDetailsRepo.findAllByQuery(
                payerCode,
                paymentType,
                dateFrom,
                dateUntil);
        paymentDetailsList.size();
    }

    @Test
    public void findAllByParametersWithPaginationTest() throws ParseException {
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-20");

        Pageable pageRequest = PageRequest.of(0, 20, Sort.by("date").descending());


        Integer payerCode = 8210260;
        String paymentType = "Відправлення";
        Integer stationCode = null;
        String docNumber = "";
        Long paymentSum = null;

        Page<PaymentDetails> paymentDetailsList = paymentDetailsRepo.findAllByQuery(
                payerCode,
                paymentType,
                dateFrom,
                dateUntil,
                pageRequest,
                stationCode,
                docNumber,
                paymentSum

        );
        List<PaymentDetails> list = paymentDetailsList.getContent();
    }

    @Test
    public void getPaymentTypesTest(){
        List<String> types = paymentDetailsRepo.findAllPaymentTypes();
        types.forEach(System.out::println);
    }
}
