package com.yurets_y.payment_statistic_web.repo;


import com.yurets_y.payment_statistic_web.dto.ChartDto;
import com.yurets_y.payment_statistic_web.dto.DateLongEntry;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest()
@TestPropertySource(locations= "classpath:db_properties/immutable-db.properties")
@Import({RepositoryConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class StatisticQueriesTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void injectionTest(){
        
        assertThat(entityManager).isNotNull();
    }

    @Test
    public void simpleQueryTest() throws ParseException {
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        String stringQuery = "SELECT new com.yurets_y.payment_statistic_web.dto.DateStringLongDto(pd.date, pd.type, SUM(pd.totalPayment)) " +
                "from PaymentDetails pd " +
                "WHERE pd.date BETWEEN :date_from AND :date_until " +
                "GROUP BY pd.date, pd.type";
        Query query = entityManager.createQuery(stringQuery);
        Date dateFrom = format.parse("2020-05-01");
        Date dateUntil = format.parse("2020-05-20");
        query.setParameter("date_from",dateFrom);
        query.setParameter("date_until",dateUntil);

        List<DateLongEntry> result = query.getResultList();

        result.size();
    }
}
