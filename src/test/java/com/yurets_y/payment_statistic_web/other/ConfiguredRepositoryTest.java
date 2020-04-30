package com.yurets_y.payment_statistic_web.other;


import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan(basePackages = "com.yurets_y.payment_statistic_web.entity")
@ComponentScan(basePackages = "com.yurets_y.payment_statistic_web")
@TestPropertySource(locations= "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConfiguredRepositoryTest {

    @Autowired
    private GenericEntityRepository genericEntityRepository;

    @Autowired
    private PaymentListRepo paymentListRepo;

    @Test
    public void givenGenericEntityRepository_whenSaveAndRetreiveEntity_thenOK() {
        assertNotNull(genericEntityRepository);
        assertNotNull(paymentListRepo);
    }

}
