package com.yurets_y.payment_statistic_web.other;

import org.junit.Test;

import java.util.Date;

public class SimpleTest {
    @Test
    public void clearDateTest(){
        Date date = new Date();
        Long time = (date.getTime() / 86400000) * 86400000;
        Date date2 = new Date(time);
        System.out.println(date);
        System.out.println(date2);
    }
}
