package com.yurets_y.payment_statistic_web.repo;


import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest(properties = {
        "spring.flyway.enabled=false"
})
//@TestPropertySource(locations="classpath:db_properties/inmemory-db.properties")
@Import({RailroadRepositoryTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RailroadDocumentRepositoryTest {

    @Autowired
    private RailroadDocumentsParser documentParser;

    @Resource(name="test-file")
    private File testFile;

    @Autowired
    private RailroadDocumentsRepo documentsRepo;

    @Test
    public void resourceIntegrationTest(){
        assertNotNull(documentParser);
        assertTrue(testFile.exists());
        assertNotNull(documentsRepo);
    }

    @Test
    public void readToDbTest() throws IOException, ParseException {
        RailroadDocument railroadDocument = documentParser.parseFromFile(testFile);
        documentsRepo.save(railroadDocument);
        RailroadDocument document = documentsRepo.findAll().get(0);
        assertTrue(document.getDocNumber() == 33230095);
        assertTrue(document.getVagonCount() == 2);
//        assertTrue(document.getSendStation().getCode() == 323607);
//        assertTrue(document.getReceiveStation().getCode() == 418101);
        assertTrue(document.getCargoSender().getEdrpuCode() == 43592481);
        assertTrue(document.getTarifPayer().getRailroadCode() == 8210260);
        assertTrue(document.getTarifDistance() == 700);
        assertTrue(document.getPayment() == 2789200);
    }

}
