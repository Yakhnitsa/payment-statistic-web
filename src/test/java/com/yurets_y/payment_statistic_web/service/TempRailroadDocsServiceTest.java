package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.parser.RailroadDocsParserConfig;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@Import({TempRailroadDocsServiceTestConfig.class})
@RunWith(SpringRunner.class)
public class TempRailroadDocsServiceTest {

    @Autowired
    private TempRailroadDocsService tempDocService;

    @Resource(name="valid-files-directory")
    private File testDirectory;

    @Resource(name="corrupted-files-directory")
    private File corruptedFilesTestDirectory;

    @Test
    public void resourceIntegrationTest(){
        assertNotNull(tempDocService);
        assertTrue(testDirectory.exists());
        assertTrue(testDirectory.isDirectory());
    }

    @Test
    public void saveToTempDbTest() throws IOException {
        File[] files = testDirectory.listFiles();

        for(File file: files){

            if(!file.isFile()) continue;
            byte[] content = Files.readAllBytes(file.toPath());
            MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(),"",content);
            tempDocService.putToTempDB(multipartFile);
        }

        Collection<RailroadDocument> documents = tempDocService.getAllFromTempDB();

        assertEquals(documents.size(),16);

        documents.forEach( document ->{
            assertTrue(document.getDocNumber() != -1);
            assertNotNull(document.getDateStamp());
            assertNotNull(document.getSendStation());
            assertNotNull(document.getReceiveStation());
            assertNotNull(document.getXmlBackupFile());
            assertNotNull(document.getPdfBackupFile());
                }
        );

    }

    @Test
    public void saveCorruptedFilesToTempDbTest() throws IOException {
        File[] files = corruptedFilesTestDirectory.listFiles();

        for(File file: files){

            if(!file.isFile()) continue;
            byte[] content = Files.readAllBytes(file.toPath());
            MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(),"",content);

            tempDocService.putToTempDB(multipartFile);
        }

        Collection<RailroadDocument> documents = tempDocService.getAllFromTempDB();
        int count = documents.size();
        int corruptedFilesCount = (int)documents.stream()
                .filter(doc -> doc.getDocNumber() == -1)
                .count();

        assertTrue(count==7);
        assertTrue(corruptedFilesCount==1);

    }

    @Test
    public void fixCorruptedFileTest() throws IOException {
        File[] files = corruptedFilesTestDirectory.listFiles();
        for(File file: files){
            if(!file.isFile()) continue;
            byte[] content = Files.readAllBytes(file.toPath());
            MultipartFile multipartFile = new MockMultipartFile(file.getName(),file.getName(),"",content);
            tempDocService.putToTempDB(multipartFile);
        }

        Collection<RailroadDocument> documents = tempDocService.getAllFromTempDB();
        RailroadDocument corruptedDoc = documents.stream()
                .filter(doc -> doc.getDocNumber() == -1)
                .findFirst().get();

        corruptedDoc.setDocNumber(33248824);
        corruptedDoc.setDateStamp(new Date());

        tempDocService.fixCorruptedDocumentInTempDb(corruptedDoc);

        int count = documents.size();
        int corruptedFilesCount = (int)documents.stream()
                .filter(doc -> doc.getDocNumber() == -1)
                .count();

        assertTrue(count==7);
        assertTrue(corruptedFilesCount==0);

    }
    @Before
    public void cleanTestDb(){
        Collection<RailroadDocument> documents = tempDocService.getAllFromTempDB();
        System.out.println(documents.size());
        documents.forEach(document -> tempDocService.deleteFromTempDB(document));
        System.out.println(tempDocService.getAllFromTempDB().size());
    }

}



@TestConfiguration
@ComponentScan(
        basePackages="com.yurets_y.payment_statistic_web.service",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                    TempRailroadDocsService.class,
                    RailroadDocumentsParser.class
                })
)
class TempRailroadDocsServiceTestConfig{

    @Bean("valid-files-directory")
    public File validTestfilesDirectory(){
        return new File("src/test/resources/test_files/railroad-documents/correct");
    }

    @Bean("corrupted-files-directory")
    public File corruptedFilesDirectory(){
        return new File("src/test/resources/test_files/railroad-documents/corrupted");
    }
}

