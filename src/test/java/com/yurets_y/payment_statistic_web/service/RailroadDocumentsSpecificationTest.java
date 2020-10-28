package com.yurets_y.payment_statistic_web.service;


import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.repo.StationsRepo;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsService;
import com.yurets_y.payment_statistic_web.service.railroad_documents_services.RailroadDocumentsSpecification;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Import(RailroadDocumentServiceTestConfig.class)
@RunWith(SpringRunner.class)
@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.properties.hibernate.show_sql=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RailroadDocumentsSpecificationTest {

    private SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-Z");

    @Resource(name = "test-files-dir-for-spec-test")
    private File testDir;

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
        assertTrue(testDir.exists());
    }

    @Test
    public void findDocumentNumberTest() throws IOException, ParseException {
        Integer testDocNumber = 33816570;
        Specification<RailroadDocument> docNumberSpec = documentsSpecification.docNumberSpecification(testDocNumber);
        Collection<RailroadDocument> foundDocs = documentsService.getAllBySpecification(docNumberSpec,defaultPageRequest()).getContent();
        assertEquals(testDocNumber,((List<RailroadDocument>) foundDocs).get(0).getDocNumber());
        assertTrue(foundDocs.size() == 1);
    }

    @Test
    public void findByDocsDateTest() throws ParseException {

        Collection<RailroadDocument> allRailDocuments = documentsService.getAll(defaultPageRequest()).getContent();

//        Document periond test
        Date dateFrom = format.parse("2020-10-25-GMT");
        Date dateUntil = format.parse("2020-10-27-GMT");

        Specification<RailroadDocument> docDateSpecification = documentsSpecification.docDateSpecification(dateFrom,dateUntil);
        Collection<RailroadDocument> foundDocs = documentsService
                .getAllBySpecification(docDateSpecification,defaultPageRequest())
                .getContent();

        Collection<RailroadDocument> filteredDocs = allRailDocuments
                .stream()
                .filter(doc -> !doc.getDateStamp().after(dateUntil) && ! doc.getDateStamp().before(dateFrom))
                .collect(Collectors.toList());
        assertEquals(foundDocs.size(),filteredDocs.size());


// Test with null values
       docDateSpecification = documentsSpecification.docDateSpecification(null,dateUntil);
        foundDocs = documentsService
                .getAllBySpecification(docDateSpecification,defaultPageRequest())
                .getContent();

        filteredDocs = allRailDocuments
                .stream()
                .filter(doc -> !doc.getDateStamp().after(dateUntil))
                .collect(Collectors.toList());

        assertEquals(foundDocs.size(),filteredDocs.size());

        //Test with all null values
        docDateSpecification = documentsSpecification.docDateSpecification(null,null);
        foundDocs = documentsService
                .getAllBySpecification(docDateSpecification,defaultPageRequest())
                .getContent();
        assertEquals(foundDocs.size(),allRailDocuments.size());

    }

    @Test
    public void findByStationCodesTest(){
        Collection<RailroadDocument> allRailDocuments = documentsService.getAll(defaultPageRequest()).getContent();

        int receiveStationCode = 415601;
        Specification<RailroadDocument> receiveStationSpec = documentsSpecification.receiveStationSpec(receiveStationCode);

        Collection<RailroadDocument> foundDocs = documentsService
                .getAllBySpecification(receiveStationSpec,defaultPageRequest()).getContent();
        Collection<RailroadDocument> filteredDocs = allRailDocuments
                .stream()
                .filter(doc -> doc.getReceiveStation().getCode()==receiveStationCode)
                .collect(Collectors.toList());
        assertEquals(foundDocs.size(),filteredDocs.size());

        int sendStationCode = 444905;
        Specification<RailroadDocument> sendStationSpec = documentsSpecification.sendStationSpec(sendStationCode);
        foundDocs = documentsService
                .getAllBySpecification(sendStationSpec,defaultPageRequest()).getContent();

        filteredDocs = allRailDocuments
                .stream()
                .filter(doc -> doc.getSendStation().getCode()==sendStationCode)
                .collect(Collectors.toList());
        assertEquals(foundDocs.size(),filteredDocs.size());

        int sendStation = 329209;
        int receiveStation = 424507;
        sendStationSpec = documentsSpecification.sendStationSpec(sendStation);
        receiveStationSpec = documentsSpecification.receiveStationSpec(receiveStation);
        Specification<RailroadDocument> groupSpec = sendStationSpec.and(receiveStationSpec);
        foundDocs = documentsService
                .getAllBySpecification(groupSpec,defaultPageRequest()).getContent();
        System.out.println(foundDocs);

        filteredDocs = allRailDocuments
                .stream()
                .filter(doc -> doc.getSendStation().getCode()==sendStation && doc.getReceiveStation().getCode() == receiveStation)
                .collect(Collectors.toList());

        assertEquals(foundDocs,filteredDocs);

    }


    @Before
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void loadTestDocumentsToDb() throws IOException, ParseException {
        for(File file : testDir.listFiles()){
            if(file.isFile() && file.toString().endsWith(".xml")){
                RailroadDocument document = documentsParser.parseFromFile(file);
                documentsService.add(document);
            }
        }
    }

    private Pageable defaultPageRequest(){
        Sort dateSort = Sort.by("docNumber").descending();
        return PageRequest.of(0, 1000, dateSort);
    }
}
