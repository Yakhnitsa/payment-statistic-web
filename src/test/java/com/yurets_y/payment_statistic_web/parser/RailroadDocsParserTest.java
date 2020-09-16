package com.yurets_y.payment_statistic_web.parser;


import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@Import({RailroadDocsParserConfig.class})
@RunWith(SpringRunner.class)
public class RailroadDocsParserTest {

    private RailroadDocumentsParser documentsParser;


    @Resource(name="test-file")
    private File testXmlFile;

    @Test
    public void resourceIntegrationTest(){
        assertNotNull(documentsParser);
        assertTrue(testXmlFile.exists());
    }
    @Test
    public void parseDocumentTest() throws IOException, ParseException {
        RailroadDocument document = documentsParser.parseFromFile(testXmlFile);
        assertTrue(document.getDocNumber() == 33230095);
        assertTrue(document.getVagonCount() == 2);
    }








    @Autowired
    public void setDocumentsParser(RailroadDocumentsParser documentsParser) {
        this.documentsParser = documentsParser;
    }
}
