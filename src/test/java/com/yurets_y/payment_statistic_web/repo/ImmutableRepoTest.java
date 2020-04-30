package com.yurets_y.payment_statistic_web.repo;


import com.yurets_y.payment_statistic_web.resources.TestResourcesConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest()
@TestPropertySource(locations= "classpath:db_properties/immutable-db.properties")
@Import({RepositoryConfig.class, TestResourcesConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ImmutableRepoTest {

    @Autowired
    private PaymentListRepo paymentListRepo;

    @Resource(name="test-file-location")
    private String testFileLocation;

    @Test
    public void autowiredRepositoryConfig() {
        assertNotNull(testFileLocation);
        assertNotNull(paymentListRepo);
    }

}
