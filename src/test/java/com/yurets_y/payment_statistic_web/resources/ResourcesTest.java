package com.yurets_y.payment_statistic_web.resources;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@Import({TestFilesConfig.class, TestListsConfig.class})
@ActiveProfiles("test")
public class ResourcesTest {

    @Resource(name="test-html-file")
    private File testHtmlFile;

    @Resource(name="test-html-directory")
    private File testHtmlFilesDirectory;

    @Resource(name="test-list")
    private PaymentList testPaymentList;

    @Resource(name="test-lists")
    private List<PaymentList> testPaymentLists;

    @Test
    public void testFilesExistTest() {
        System.out.println("test html file: " + testHtmlFile);
        assertNotNull(testHtmlFile);

        System.out.println("test html directory: " + testHtmlFilesDirectory);
        assertNotNull(testHtmlFilesDirectory);
    }

    @Test
    public void testPaymentListExist(){
        assertNotNull(testPaymentList);
        assertEquals(testPaymentList.getNumber(),20200111);
        assertEquals(testPaymentList.getPayerCode(),8210260);
    }

    @Test
    public void allFilesParsedTest(){
        int parsedLists = testPaymentLists.size();
        int testedFiles = testHtmlFilesDirectory.listFiles().length;
        assertTrue(parsedLists == testedFiles);
    }
}
