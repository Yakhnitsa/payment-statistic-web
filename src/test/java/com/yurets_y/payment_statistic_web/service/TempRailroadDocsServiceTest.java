package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.parser.RailroadDocsParserConfig;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@Import({TempRailroadDocsServiceTestConfig.class})
@RunWith(SpringRunner.class)
public class TempRailroadDocsServiceTest {

    @Autowired
    private TempRailroadDocsService tempDocService;

    @Autowired
    private File testDirectory;

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

        int count = tempDocService.getAllFromTempDB().size();
//        TODO выполнить логику проверки файлов
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

    @Bean("backup-files-directory")
    public File backupFileDirectory(){
        return new File("src/test/resources/test_files/railroad-documents/correct");
    }
}

