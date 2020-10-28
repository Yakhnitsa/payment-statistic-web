package com.yurets_y.payment_statistic_web.repo;


import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.Station;
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
@Import({RailroadRepositoryTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RailroadDocumentRepositoryTest {

    @Autowired
    private RailroadDocumentsParser documentParser;

    @Autowired
    private StationsRepo stationsRepo;

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
    public void writeToDbTest() throws IOException, ParseException {
        RailroadDocument railroadDocument = documentParser.parseFromFile(testFile);
        railroadDocument.setXmlBackupFile(testFile);
        documentsRepo.save(railroadDocument);
        RailroadDocument document = documentsRepo.findAll().get(0);
        assertEquals(33230095, (int) document.getDocNumber());
        assertEquals(2, document.getVagonCount());
//        assertTrue(document.getSendStation().getCode() == 323607);
//        assertTrue(document.getReceiveStation().getCode() == 418101);
        assertEquals(43592481, (int) document.getCargoSender().getEdrpuCode());
        assertEquals(8210260, (int) document.getTarifPayer().getRailroadCode());
        assertEquals(700, document.getTarifDistance());
        assertEquals(2789200, document.getPayment());
    }

    @Test
    public void overrideStationWhileSaveTest() throws IOException, ParseException
    {

        Station station = new Station();
        station.setCode(418101);
        station.setRusName("Жовтневая(экспортная)");
        station.setUkrName("Жовтнева (експортна)");

        stationsRepo.saveAndFlush(station);

        RailroadDocument railroadDocument = documentParser.parseFromFile(testFile);
        documentsRepo.save(railroadDocument);

        RailroadDocument document = documentsRepo.findAll().get(0);
        assertEquals("Жовтневая(экспортная)",document.getReceiveStation().getRusName());
    }
}
