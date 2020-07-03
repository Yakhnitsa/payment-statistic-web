package com.yurets_y.payment_statistic_web.service;


import com.yurets_y.payment_statistic_web.dto.DailyStatisticDto;
import com.yurets_y.payment_statistic_web.dto.YearStatisticDtoEntry;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import com.yurets_y.payment_statistic_web.repo.RepositoryConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations= "classpath:db_properties/immutable-db.properties")
@Import({RepositoryConfig.class, ServiceTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class StatisticServiceTest {
    @Autowired
    StatisticService statisticService;

    @Autowired
    PaymentListRepo paymentListRepo;

    private SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void resourceIntegrationTest(){
        assertThat(statisticService).isNotNull();
    }

    @Test
    public void paymentListRepoContentTest(){
        List<PaymentList> list = paymentListRepo.findAll();
        System.out.println(list.size());
    }
    @Test
    public void paymentStatisticDtoTest() throws ParseException {
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-20");

        DailyStatisticDto dto = statisticService.getDailyStatistic(dateFrom,dateUntil);
        assertThat(dto.getPayments().size()).isEqualTo(20);

    }
    @Test
    public void chartDtoTest() throws ParseException {
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-20");

        statisticService.getDailyChartStatistic(dateFrom,dateUntil,3);
    }
    @Test
    public void yearChartDtoTest() throws ParseException {
        Date dateFrom = format.parse("2020-04-01");
        Date dateUntil = format.parse("2020-06-30");

        List<YearStatisticDtoEntry> dto = statisticService.getYearChartStatistic(dateFrom,dateUntil);
        dto.size();
    }


}
