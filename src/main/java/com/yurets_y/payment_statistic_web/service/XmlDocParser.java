package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlDocParser extends HtmlDocParser implements DocParser {

    @Override
    public PaymentList parseFromFile(File file) throws IOException {

        if(!file.getName().toLowerCase().endsWith(".xml")){
            throw new IOException("Неизвесный формат файла!!!");
        }

        Document document = Jsoup.parse(file, "UTF-8");

        PaymentList paymentList = parseFromJsoup(document);

        paymentList.setBackupFile(file);
        return paymentList;
    }

    private PaymentList parseFromJsoup(Document document) {
        PaymentList paymentList = new PaymentList();

        Iterator<Element> stringIterator = document.select("Row").iterator();

        while (stringIterator.hasNext()) {
            List<String> cellList = parseChartRow(stringIterator.next());

            if (cellList.size() <= 0) {
                continue;
            }
            parseNumberAndCode(paymentList, cellList);

            parseOpeningAndClosingBalance(paymentList, cellList);

            parseTotalPayments(paymentList, cellList);


        }

        return paymentList;
    }

    @Override
    protected List<String> parseChartRow(Element tableString) {

        List<String> cellList = new ArrayList<String>();
        Iterator<Element> cellIterator = tableString.select("Data").iterator();
        while (cellIterator.hasNext()) {
            cellList.add(cellIterator.next().text());
        }
        return cellList;
    }

    @Override
    protected void parseOpeningAndClosingBalance(PaymentList paymentList, List<String> cellList) {
        String openBalancePattern = "Сальдо на початок.+:.+?(-?\\d+[,.]\\d+)";
        if (cellList.get(0).matches(openBalancePattern)) {
            paymentList.setOpeningBalance(-getLongFromPattern(cellList.get(0), openBalancePattern));
        }

        String closingBalancePattern = "Сальдо на кінець.+";
        if (cellList.size() >= 4 && cellList.get(2).matches(closingBalancePattern)) {
            paymentList.setClosingBalance(-getLongFromPattern(cellList.get(3), NUMBER_PATTERN));
        }
    }

}
