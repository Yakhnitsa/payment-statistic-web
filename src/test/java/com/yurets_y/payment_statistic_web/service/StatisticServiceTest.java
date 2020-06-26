package com.yurets_y.payment_statistic_web.service;


import com.yurets_y.payment_statistic_web.dto.DailyStatisticDto;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import com.yurets_y.payment_statistic_web.repo.RepositoryConfig;
import com.yurets_y.payment_statistic_web.resources.TestListsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    public void paymentStatisticDtoTest(){
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.set(2020, Calendar.MAY, 1,0,0,0);
        Date dateFrom = calendar.getTime();

        calendar.set(2020, Calendar.MAY, 20);
        Date dateUntil = calendar.getTime();

        DailyStatisticDto dto = statisticService.getDailyStatistic(dateFrom,dateUntil);
        assertThat(dto.getPayments().size()).isEqualTo(20);

    }
    @Test
    public void chartDtoTest(){
        GregorianCalendar calendar = new GregorianCalendar(2020, Calendar.MAY, 1,0,15);

        Date dateFrom = calendar.getTime();

        calendar.set(2020, Calendar.MAY, 20,1,15);
        Date dateUntil = calendar.getTime();

        statisticService.getChartStatistic(dateFrom,dateUntil,3);
    }

}
