package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDocParser {
    protected final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    protected final String NUMBER_PATTERN = "(-?\\d+[,.]\\d+)";
    private final String LIST_DATE_PATTERN = "\\d{2}\\.\\d{2}\\.\\d{4}";
    private final String LIST_NUMBER_PATTERN = "\\d{8}";

    public abstract PaymentList parseFromFile(File file) throws IOException;

    protected void parseTotalPayments(PaymentList paymentList, List<String> cellList) {
        String first = cellList.get(0);
        if (cellList.size() >= 2) {
            if (first.matches("Разом")) {
                paymentList.setPaymentVsTaxes(getLongFromPattern(cellList.get(1), NUMBER_PATTERN));
            } else if (first.matches("Всього проведено платежів")) {
                paymentList.setPayments(getLongFromPattern(cellList.get(1), NUMBER_PATTERN));
            } else if (first.matches("ПДВ")) {
                paymentList.setPaymentTaxes(getLongFromPattern(cellList.get(1), NUMBER_PATTERN));
            }

        }
    }

    protected void parseNumberAndCode(PaymentList paymentList, List<String> cellList) {
        String first = cellList.get(0);
        if (first.contains("Перелік")) {
            try {
                paymentList.setNumber(getListNumb(first));
            } catch (Exception e) {
                e.printStackTrace();
            }
            paymentList.setDate(getListDate(first));
        }

        String paymentCodePattern = "Код платника:(\\d*)";
        if (cellList.size() >= 2 && cellList.get(1).matches(paymentCodePattern)) {
            try {
                paymentList.setPayerCode((int) getLongFromPattern(cellList.get(1), paymentCodePattern));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void parseOpeningAndClosingBalance(PaymentList paymentList, List<String> cellList) {
        String openBalancePattern = "Сальдо на початок.+:.+?(-?\\d+[,.]\\d+)";
        if (cellList.size() > 1 && cellList.get(1).matches(openBalancePattern)) {
            paymentList.setOpeningBalance(-getLongFromPattern(cellList.get(1), openBalancePattern));
        }

        String closingBalancePattern = "Сальдо на кінець.+";
        if (cellList.size() >= 4 && cellList.get(2).matches(closingBalancePattern)) {
            paymentList.setClosingBalance(-getLongFromPattern(cellList.get(3), NUMBER_PATTERN));
        }
    }

    protected int getListNumb(String string) {
        Pattern pattern = Pattern.compile(LIST_NUMBER_PATTERN);
        Matcher m = pattern.matcher(string);
        if (m.find()) {
            return Integer.parseInt(m.group());
        }
        return -1;
    }Date getListDate(String string) {
        Pattern pattern = Pattern.compile(LIST_DATE_PATTERN);
        Matcher m = pattern.matcher(string);

        if (m.find()) {
            try {
                return DATE_FORMAT.parse(m.group());
            } catch (ParseException e) {
                throw new RuntimeException("Ошибка получения даты перечня");
            }

        }
        return new Date();
    }

    protected long getLongFromPattern(String matchedString, String stringPattern) {
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(matchedString);
        if (matcher.matches()) {
            String numbString = matcher.group(1).replaceAll("[,.]", "");
            return Long.parseLong(numbString);
        }

        return -1L;
    }

    protected boolean checkSumTest(PaymentList paymentList) {
        long openingBalance = paymentList.getOpeningBalance();
        long closingBalance = paymentList.getClosingBalance();
        long payments = paymentList.getPaymentDetailsList()
                .stream()
                .filter(paymentDetails -> paymentDetails.getType().equals("Платіжні доручення"))
                .mapToLong(PaymentDetails::getTotalPayment).sum();
        long totalPaymentsVsTaxes = paymentList.getPaymentVsTaxes();
        long totalPaymentsFromList = paymentList.getPaymentDetailsList()
                .stream()
                .filter(paymentDetails -> !paymentDetails.getType().equals("Платіжні доручення"))
                .mapToLong(PaymentDetails::getTotalPayment).sum();

        long checkSum = openingBalance + payments - totalPaymentsFromList;

        return (checkSum == closingBalance) && (totalPaymentsVsTaxes == totalPaymentsFromList);
    }

    protected List<String> parseChartRow(Element tableString) {

        List<String> cellList = new ArrayList<String>();
        Iterator<Element> cellIterator = tableString.select("th").iterator();
        while (cellIterator.hasNext()) {
            cellList.add(cellIterator.next().text());
        }

        cellIterator = tableString.select("tcol").iterator();
        while (cellIterator.hasNext()) {
            cellList.add(cellIterator.next().text());
        }
        return cellList;
    }
}
