package com.yurets_y.payment_statistic_web.service;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("html-doc-parser")
public class HtmlDocParser extends AbstractDocParser{

    @Override
    String fileFormat() {
        return ".html";
    }

    @Override
    String rowSeparator() {
        return "tr";
    }

    @Override
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


    @Override
    List<PaymentDetails> getTransportPayments(String type, Iterator<Element> iterator) {
        List<String> row = parseChartRow(iterator.next());
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();
        if (row.size() <= 1) return paymentDetailsList;
        while (iterator.hasNext()) {
            try {
                if (row.get(0).matches("Дата")) {
                    row = parseChartRow(iterator.next());
                    continue;
                }
                if (row.get(4).equals("Всього")) return paymentDetailsList;
                PaymentDetails pd = new PaymentDetails();
                pd.setType(type);
                pd.setDate(DATE_FORMAT.parse(row.get(0)));
                pd.setStationCode(Integer.parseInt(row.get(1)));
                pd.setStationName(row.get(2));
                pd.setDocumentNumber(row.get(3));
                pd.setPayment(getLongFromPattern(row.get(4), NUMBER_PATTERN));
                pd.setAdditionalPayment(getLongFromPattern(row.get(5), NUMBER_PATTERN));
                pd.setTaxPayment(getLongFromPattern(row.get(6), NUMBER_PATTERN));
                pd.setTotalPayment(getLongFromPattern(row.get(7), NUMBER_PATTERN));
                pd.setIncomeType(PaymentDetails.IncomeType.OUTCOME);

                paymentDetailsList.add(pd);
                row = parseChartRow(iterator.next());

            } catch (ParseException e) {
                throw new RuntimeException("Ошибка парсинга строки " + row.toString());
            }
        }
        return paymentDetailsList;
    }

    @Override
    List<PaymentDetails> getStationPayments(String type, Iterator<Element> iterator) {
        List<String> row = parseChartRow(iterator.next());
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();
        if (row.size() < 1) return paymentDetailsList;
        while (iterator.hasNext()) {
            try {
                if (row.get(0).matches("Дата")) {
                    row = parseChartRow(iterator.next());
                    continue;
                }
                if (row.get(3).equals("Всього")) return paymentDetailsList;
                PaymentDetails pd = new PaymentDetails();
                pd.setType(type);
                pd.setDate(DATE_FORMAT.parse(row.get(0)));
                pd.setStationCode(Integer.parseInt(row.get(1)));
                pd.setStationName(row.get(2));
                pd.setDocumentNumber(row.get(3));
                pd.setPaymentCode(row.get(4));
                pd.setPaymentDescription(row.get(5));
                pd.setPayment(getLongFromPattern(row.get(6), NUMBER_PATTERN));
                pd.setTaxPayment(getLongFromPattern(row.get(7), NUMBER_PATTERN));
                pd.setTotalPayment(getLongFromPattern(row.get(8), NUMBER_PATTERN));
                pd.setIncomeType(PaymentDetails.IncomeType.OUTCOME);

                paymentDetailsList.add(pd);
                row = parseChartRow(iterator.next());

            } catch (ParseException e) {
                throw new RuntimeException("Ошибка парсинга строки " + row.toString());
            }
        }
        return paymentDetailsList;
    }

    @Override
    List<PaymentDetails> getPayments(String type, Iterator<Element> iterator) {
        List<String> row = parseChartRow(iterator.next());
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();
        if (row.size() < 1) return paymentDetailsList;
        while (iterator.hasNext()) {
            try {
                if (row.get(0).matches("Дата")) {
                    row = parseChartRow(iterator.next());
                    continue;
                }
                if (row.get(2).equals("Всього")) return paymentDetailsList;
                PaymentDetails pd = new PaymentDetails();
                pd.setType(type);
                pd.setDate(DATE_FORMAT.parse(row.get(0)));
                pd.setDocumentNumber(row.get(1));
                pd.setPaymentCode(row.get(2));
                pd.setTotalPayment(getLongFromPattern(row.get(3), NUMBER_PATTERN));
                pd.setIncomeType(PaymentDetails.IncomeType.INCOME);

                paymentDetailsList.add(pd);
                row = parseChartRow(iterator.next());

            } catch (ParseException e) {
                throw new RuntimeException("Ошибка парсинга строки " + row.toString());
            }
        }
        return paymentDetailsList;
    }

}


