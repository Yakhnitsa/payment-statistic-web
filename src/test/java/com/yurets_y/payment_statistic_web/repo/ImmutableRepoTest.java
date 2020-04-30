package com.yurets_y.payment_statistic_web.repo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest()
@TestPropertySource(locations= "classpath:db_properties/immutable-db.properties")
@Import(RepositoryConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ImmutableRepoTest {

    @Autowired
    private PaymentListRepo paymentListRepo;

    @Test
    public void autowiredRepositoryConfig() {
        assertNotNull(paymentListRepo);
    }

}
