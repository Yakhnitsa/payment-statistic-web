package com.yurets_y.paymentstatisticweb.service;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.service.PaymentListDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(DAOTestConfiguration.class)
public class PaymentListDAOTest {
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Resource(name="dao-vs-immutable-db")
    private PaymentListDAO paymentListDAOImmutable;

    @Test
    public void autowireComponentTest(){
        assertThat(this.paymentListDAOImmutable).isNotNull();
    }

    @Test
    public void filterByDateTest() throws ParseException {

        Date dateFrom = DATE_FORMAT.parse("2020-01-04");
        Date dateUntil = DATE_FORMAT.parse("2020-01-10");
        List<PaymentList> filteredList = paymentListDAOImmutable.getByPeriod(dateFrom,dateUntil);
        assertThat(filteredList.size()).isEqualTo(7);

    }

    @Test
    public void getDetailsByDateTest() throws ParseException{
        Date dateFrom = DATE_FORMAT.parse("2020-01-05");
        Date dateUntil = DATE_FORMAT.parse("2020-01-09");
        List<PaymentDetails> testList = paymentListDAOImmutable.getPaymentDetailsByDate(dateFrom,dateUntil);

        testList.forEach(detail ->{
            if(detail.getStationName() == null) detail.setStationName("other");
        });

        Map<Date,Map<String,List<PaymentDetails>>> resultMap = testList.stream()
                .collect(Collectors.groupingBy(PaymentDetails::getDate,
                        Collectors.groupingBy(PaymentDetails::getStationName)));

        resultMap.forEach((date,map) ->{
            System.out.println(date);
            map.forEach((station,list)->{
                System.out.println("   " + station);
                list.forEach(detail ->{
                            System.out.println("      " + detail.getType() + " - " + detail.getTotalPayment());
                        }
                );
            });
        });

    }


}
