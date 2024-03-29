package com.yurets_y.payment_statistic_web.parser;


import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.Vagon;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@Import({RailroadDocsParserConfig.class})
@RunWith(SpringRunner.class)
public class RailroadDocsParserTest {

    private RailroadDocumentsParser documentsParser;


    @Resource(name="test-file")
    private File testXmlFile;

    @Resource(name="corrupted-test-file")
    private File corruptedTestFile;

    @Test
    public void resourceIntegrationTest(){
        assertNotNull(documentsParser);
        assertTrue(testXmlFile.exists());
        assertTrue(corruptedTestFile.exists());
    }
    @Test
    public void parseDocumentTest() throws IOException, ParseException {
        RailroadDocument document = documentsParser.parseFromFile(testXmlFile);
        assertTrue(document.getDocNumber() == 33230095);
        assertTrue(document.getVagonCount() == 2);
        assertTrue(document.getSendStation().getCode() == 323607);
        assertTrue(document.getReceiveStation().getCode() == 418101);
        assertTrue(document.getCargoSender().getEdrpuCode() == 43592481);
        assertTrue(document.getTarifPayer().getRailroadCode() == 8210260);
        assertTrue(document.getTarifDistance() == 700);
        assertTrue(document.getPayment() == 2789200);

        List<Vagon> vagonList = document.getVagonList();
        assertTrue(vagonList.get(0).getNumber()==59501502);
        assertTrue(vagonList.get(1).getNumber()==59501239);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
        Date date = dateFormat.parse("15.09.2020 14:45:00");

        assertEquals(document.getDateStamp(),date);

    }

    @Test
    public void parserCorruptedFileTest() throws IOException, ParseException {
        RailroadDocument document = documentsParser.parseFromFile(corruptedTestFile);
        assertTrue(document.getDocNumber() == -1);
        assertTrue(document.getDateStamp()== null);
        assertTrue(document.getVagonCount() == 15);
        assertTrue(document.getSendStation().getCode() == 325104);
        assertTrue(document.getReceiveStation().getCode() == 418101);
        assertTrue(document.getCargoSender().getEdrpuCode() == 43592481);
        assertTrue(document.getCargoReceiver().getEdrpuCode() == 41730338);
        assertTrue(document.getTarifPayer().getRailroadCode() == 8210260);
        assertTrue(document.getTarifDistance() == -1);
        assertTrue(document.getPayment() == -1);
    }

    @Autowired
    public void setDocumentsParser(RailroadDocumentsParser documentsParser) {
        this.documentsParser = documentsParser;
    }
}
