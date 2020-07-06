package com.yurets_y.payment_statistic_web.parser;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class PatternsTest {
    @Test
    public void matcherGroupTest(){
//        String one = "Накопичувальнi карточки";
//        String two = "Накопичувальні карточки";
//        System.out.println(one.equals(two));
        String testString = "Сальдо на початок розрахункової доби :      -1132310.17";
        String stringPattern = "Сальдо на початок.+:.+?(\\d+[,.]?\\d+)";
        System.out.println(testString.matches(stringPattern));
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(testString);
        if(matcher.matches()){
            Double numb = Double.parseDouble(matcher.group(1));
            System.out.println(matcher.group(1));
            System.out.println("parsed: "+ numb.longValue());

//            System.out.println(matcher.group(2));
//            System.out.println(matcher.group(2).replaceAll("[,.]",""));
        }

    }
    @Test
    public void simplePatternTest(){
        String testString1 = "Платіжні доручення";
        String testString2 = "Вiдправлення - мiжнародне сполучення";
        Pattern pattern = Pattern.compile("Вiдправлення.*");
        assertThat(pattern.matcher(testString1).matches()).isTrue();
        assertThat(pattern.matcher(testString2).matches()).isTrue();
    }
    @Test
    public void matchesTest(){
        String testString = "-23326432";
        System.out.println(testString.matches("\\d+"));
    }
}
