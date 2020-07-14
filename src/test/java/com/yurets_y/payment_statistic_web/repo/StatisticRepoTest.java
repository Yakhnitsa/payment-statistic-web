package com.yurets_y.payment_statistic_web.repo;


import com.yurets_y.payment_statistic_web.dto.DateStringLongDto;
import org.junit.Assert;
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
@DataJpaTest()
@TestPropertySource(locations= "classpath:db_properties/immutable-db.properties")
@Import({RepositoryConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class StatisticRepoTest {

    @Autowired
    private StatisticRepo repo;

    SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void injectionTest(){
        
        assertThat(repo).isNotNull();
    }

    @Test
    public void simpleQueryTest() throws ParseException {
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-20");
        Integer payerCode = 8210260;

        List result = repo.getChartExpensesStatistic(dateFrom,dateUntil);
        result.size();

        result = repo.getChartStatisticByPaymentType(dateFrom,dateUntil,"Платіжні доручення");
        result.size();
    }

    @Test
    public void getStatisticByTypeTest() throws ParseException {
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-20");
        Integer payerCode = 8210260;

        List result = repo.getChartStatisticGroupByType(dateFrom,dateUntil);
        result.size();

    }

    @Test
    public void getStatisticByStationTest() throws ParseException {
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-20");
        Integer payerCode = 8210260;

        List result = repo.getChartStatisticGroupByStation(dateFrom,dateUntil);
        result.size();

    }
    @Test
    public void getDailyStatisticByStationTest() throws ParseException{
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-02");
        Integer payerCode = 8210260;

        List<DateStringLongDto> dataList = repo.getDailyStatisticByPaymentCodeGroupByStation(dateFrom,dateUntil,payerCode);
        String mykolaiv = "415207 МИКОЛАїВ-ВАНТАЖНИЙ";
        Long summary = dataList
                .stream()
                .filter(dto -> dto.getType().equals(mykolaiv))
                .mapToLong(dto -> dto.getValue()).sum();
        Assert.assertEquals(summary.longValue(),41894928L);

        String viry = "444905 ВИРИ";
        summary = dataList
                .stream()
                .filter(dto -> dto.getType().equals(viry))
                .mapToLong(dto -> dto.getValue()).sum();
        Assert.assertEquals(summary.longValue(),18930708L);

        String poltava = "448501 ПОЛТАВА-КИїВСЬКА";
        summary = dataList
                .stream()
                .filter(dto -> dto.getType().equals(poltava))
                .mapToLong(dto -> dto.getValue()).sum();
        Assert.assertEquals(summary.longValue(),64348932L);

        dataList.size();
    }

    @Test
    public void getYearStatisticTest() throws ParseException {
        Date dateFrom = format.parse("2020-02-01");
        Date dateUntil = format.parse("2020-06-30");
        Integer payerCode = 8210260;

        List result = repo.getYearExpensesStatisticGroupByMonth(dateFrom,dateUntil);
        result.size();
    }
    @Test
    public void getYearStatisticByTypesTest() throws ParseException {
        Date dateFrom = format.parse("2020-02-01");
        Date dateUntil = format.parse("2020-06-30");
        Integer payerCode = 8210260;

        List result = repo.getYearStatisticGroupByMonthAndType(dateFrom,dateUntil);
        result.size();
    }


}
