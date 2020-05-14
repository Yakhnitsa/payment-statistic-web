package com.yurets_y.payment_statistic_web.entity;


import com.yurets_y.payment_statistic_web.resources.TestListsConfig;
import com.yurets_y.payment_statistic_web.util.PaymentDetailsByTypeComparator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@Import({TestListsConfig.class})
public class PaymentDetailsComparatorTest {
    @Resource(name="test-list")
    public PaymentList paymentList;
    @Test
    public void detailsSortTest(){
        List<PaymentDetails> pd = paymentList.getPaymentDetailsList();
        Collections.sort(pd,new PaymentDetailsByTypeComparator());
        System.out.println(pd);
    }
}
