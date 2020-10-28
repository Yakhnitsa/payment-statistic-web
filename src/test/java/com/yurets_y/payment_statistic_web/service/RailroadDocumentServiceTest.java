package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import com.yurets_y.payment_statistic_web.entity.Station;
import com.yurets_y.payment_statistic_web.repo.RailroadDocumentsRepo;
import com.yurets_y.payment_statistic_web.repo.StationsRepo;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsService;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-Z");

    @Resource(name = "test-file")
    private File testFile;

    @Resource(name = "test-files-dir")
    private File testFilesDirectory;

    @Resource(name = "test-files-dir-for-spec-test")
    private File testFilesDirectoryForSpecTest;



    @Resource(name = "corrupted-test-file")
    private File corruptedTestFile;

    @Value("${service.backup-path}" + "/rail_docs")
    private String backupDirPath;


    @Autowired
    private RailroadDocumentsService documentsService;

    @Autowired
    private RailroadDocumentsParser documentsParser;

    @Autowired
    private StationsRepo stationsRepo;

    @Autowired
    private RailroadDocumentsSpecification documentsSpecification;

    @Test
    public void resourceIntegrationTest() {
        assertNotNull(documentsService);
        assertNotNull(documentsParser);
        assertTrue(testFile.exists());
        assertTrue(testFilesDirectoryForSpecTest.exists());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void saveToDbTest() throws IOException, ParseException, InterruptedException {
        RailroadDocument document = documentsParser.parseFromFile(testFile);

        File backupDir = new File(backupDirPath);
        // Проверка пуста ли папка с бекап файлами.
        assertTrue(backupDir.exists() && backupDir.listFiles().length == 0);

        document.setXmlBackupFile(testFile);
        documentsService.add(document);

        Sort dateSort = Sort.by("docNumber").descending();
        Pageable pageRequest = PageRequest.of(0, 10, dateSort);
        RailroadDocumentId id = new RailroadDocumentId(document.getDocNumber(), document.getDateStamp());
        RailroadDocument documentFromDb = documentsService.getById(id);

        assertNotNull(documentFromDb.getDateStamp());
        assertEquals(33230095, (int) documentFromDb.getDocNumber());

        assertNotNull(documentFromDb.getSendStation());
        assertNotNull(documentFromDb.getReceiveStation());

        assertNotNull(documentFromDb.getXmlBackupFilePath());

        File backupFile = new File(backupDirPath + File.separator + documentFromDb.getXmlBackupFilePath());
        assertTrue(backupFile.exists());

        List<Station> stationList = stationsRepo.findAll();
        assertEquals(2, stationList.size());

        List<RailroadDocument> documents = documentsService.getAll(pageRequest).getContent();
        assertEquals(documents.size(), 1);
    }

    @Test(expected = RuntimeException.class)
    public void corruptedFileSavingTest() throws IOException, ParseException {
        RailroadDocument document = documentsParser.parseFromFile(corruptedTestFile);
        document.setXmlBackupFile(testFile);
        documentsService.add(document);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void deleteRailDocTest() throws IOException, ParseException {
        Sort dateSort = Sort.by("docNumber").descending();
        Pageable pageRequest = PageRequest.of(0, 10, dateSort);
        File backupDir = new File(backupDirPath);
        // Проверка пуста ли папка с бекап файлами.
        assertTrue(backupDir.exists() && backupDir.listFiles().length == 0);

        Collection<RailroadDocument> docsBeforeTest = documentsService.getAll(pageRequest).getContent();

        RailroadDocument document = documentsParser.parseFromFile(testFile);
        document.setXmlBackupFile(testFile);
        documentsService.add(document);
        File backupFile = new File(backupDirPath + File.separator + document.getXmlBackupFilePath());
        assertTrue(backupFile.exists());

        Collection<RailroadDocument> docsAfterSave = documentsService.getAll(pageRequest).getContent();

        documentsService.remove(document);
        Collection<RailroadDocument> docsAfterDelete = documentsService.getAll(pageRequest).getContent();
        assertTrue(!backupFile.exists());

    }

    @Before
    public void cleanBackupDir() throws IOException {
        File backup = new File(backupDirPath);
        for (File file : backup.listFiles()) {
            Files.deleteIfExists(file.toPath());
        }

    }


}

@TestConfiguration
@TestPropertySource(locations = "classpath:db_properties/inmemory-db.properties")
@EntityScan(basePackages = "com.yurets_y.payment_statistic_web.entity")
@ComponentScan(
        basePackages = "com.yurets_y.payment_statistic_web.service",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        RailroadDocumentsService.class,
                        RailroadDocumentsParser.class,
                        RailroadDocumentsRepo.class,
                        StationService.class,
                        StationsRepo.class,
                        RailroadDocumentsSpecification.class
                })
)
class RailroadDocumentServiceTestConfig {

    @Bean("test-file")
    public File testFile() {
        return new File("src/test/resources/test_files/railroad-documents/33230095.xml");
    }

    @Bean("test-files-dir")
    public File getTestFilesDirectory() {
        return new File("src/test/resources/test_files/railroad-documents/correct");
    }

    @Bean("test-files-dir-for-spec-test")
    public File getTestFilesForSpecTest() {
        return new File("src/test/resources/test_files/railroad-documents/spec-test-files");
    }


    @Bean("corrupted-test-file")
    public File corruptedTestFile() {
        return new File("src/test/resources/test_files/railroad-documents/corrupted/33248824.xml");
    }
}
