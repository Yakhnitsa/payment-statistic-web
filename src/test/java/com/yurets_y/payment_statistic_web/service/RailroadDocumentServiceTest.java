package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import com.yurets_y.payment_statistic_web.entity.Station;
import com.yurets_y.payment_statistic_web.parser.RailroadDocsParserConfig;
import com.yurets_y.payment_statistic_web.repo.RailroadDocumentsRepo;
import com.yurets_y.payment_statistic_web.repo.StationsRepo;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Import(RailroadDocumentServiceTestConfig.class)
@RunWith(SpringRunner.class)
@DataJpaTest(properties = {
        "spring.flyway.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RailroadDocumentServiceTest {

    @Resource(name="test-file")
    private File testFile;
    @Autowired
    private RailroadDocumentsService documentsService;

    @Autowired
    private RailroadDocumentsParser documentsParser;

    @Autowired
    private StationsRepo stationsRepo;

    @Test
    public void resourceIntegrationTest(){
        assertNotNull(documentsService);
        assertNotNull(documentsParser);
        assertTrue(testFile.exists());
    }
    @Test
    public void saveToDbTest() throws IOException, ParseException, InterruptedException {
        RailroadDocument document = documentsParser.parseFromFile(testFile);
        document.setXmlBackupFile(testFile);
        documentsService.add(document);

        Sort dateSort = Sort.by("docNumber").descending();
        Pageable pageRequest = PageRequest.of(0,10,dateSort);
        RailroadDocumentId id = new RailroadDocumentId(document.getDocNumber(),document.getDateStamp());
        Thread.sleep(5000);
        RailroadDocument documentFromDb = documentsService.getById(id);

        assertNotNull(documentFromDb.getDateStamp());
        assertEquals(33230095, (int) documentFromDb.getDocNumber());
        assertNotNull(documentFromDb.getXmlBackupFilePath());

        List<Station> stationList = stationsRepo.findAll();
        assertEquals(2, stationList.size());

        List<RailroadDocument> documents = documentsService.getAll(pageRequest).getContent();
        assertEquals(documents.size(),1);
    }

}

@TestConfiguration
@TestPropertySource(locations= "classpath:db_properties/inmemory-db.properties")
@EntityScan(basePackages = "com.yurets_y.payment_statistic_web.entity")
@ComponentScan(
        basePackages="com.yurets_y.payment_statistic_web.service",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        RailroadDocumentsService.class,
                        RailroadDocumentsParser.class,
                        RailroadDocumentsRepo.class,
                        StationService.class,
                        StationsRepo.class
                })
)
class RailroadDocumentServiceTestConfig{

    @Bean("test-file")
    public File testFile(){
        return new File("src/test/resources/test_files/railroad-documents/33230095.xml");
    }
}
