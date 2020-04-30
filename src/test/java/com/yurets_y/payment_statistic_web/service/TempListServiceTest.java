package com.yurets_y.payment_statistic_web.service;


import com.yurets_y.payment_statistic_web.parser.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Import(TestConfig.class)
@RunWith(SpringRunner.class)
public class TempListServiceTest {
    @Value("${service.temp-dir}")
    private String tempDirPath;

    @Test
    public void testDirectories() throws IOException {
        String testFile = "testFile.html";
        Path tempDir = Files.createTempDirectory("doc-parser-web");
        String filePath = tempDir.toAbsolutePath().toString() + File.separator + testFile;
        File file = Files.createFile(Paths.get(filePath)).toFile();


    }

}
