package com.yurets_y.payment_statistic_web.other;

import org.junit.Test;

import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTest {
    @Test
    public void clearDateTest(){
        String contentType = "name=\"=?UTF-8?B?cGVyXzI1MDcyMDIwXzEwMDAwMV81NjM1ODQxLnhtbA==?=\"";

        Pattern pattern = Pattern.compile(".+name=(.+)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(contentType);
        String result = "unknown_file";
        if (matcher.matches()) {
            result = matcher.group(1);
        }
        String[] parts = contentType.split("\\?");
        if(parts.length > 4){
            result = new String(Base64.getDecoder().decode(parts[3]));
        }

        System.out.println(result);
    }
}
