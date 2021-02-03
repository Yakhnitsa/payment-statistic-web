package com.yurets_y.payment_statistic_web.service.parser_services;

import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Service("xml-doc-parser")
public class XmlDocParser extends AbstractDocParser{

    @Override
    protected List<String> parseChartRow(Element tableString) {

        List<String> cellList = new ArrayList<String>();
        for (Element element : tableString.select("Data")) {
            cellList.add(element.text());
        }
        return cellList;
    }

    @Override
    String fileFormat() {
        return ".xml";
    }

    @Override
    String rowSeparator() {
        return "Row";
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
        else if (cellList.size() >= 3 && cellList.get(1).matches(closingBalancePattern)) {
            paymentList.setClosingBalance(-getLongFromPattern(cellList.get(2), NUMBER_PATTERN));
        }
    }

    @Override
    List<PaymentDetails> getPaymentDetailsByType(String type, ListIterator<Element> iterator) {
        if(type.equals("ПДВ")){
            return new ArrayList<>();
        }


        if(!iterator.hasNext()) return new ArrayList<>();

        List<String> elements = parseChartRow(iterator.next());
        if(elements.size() == 8){
            return getTransportPayments(type,iterator);
        }
        else if(elements.size() == 9){
            return getStationPayments(type,iterator);
        }
        else if(elements.size() == 4 || elements.size() == 5){
            return getPayments(type,iterator);
        }
        iterator.previous();
        return new ArrayList<>();
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
                if (row.get(0).equals("Всього")) return paymentDetailsList;
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
                if (row.get(0).equals("Всього")) return paymentDetailsList;
                PaymentDetails pd = new PaymentDetails();
                pd.setType(type);
                pd.setDate(DATE_FORMAT.parse(row.get(0)));
                pd.setStationCode(parseStationCode(row.get(1)));
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
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();
        List<String> row = parseChartRow(iterator.next());
        if (row.size() < 1 ) return paymentDetailsList;
        while (iterator.hasNext()) {
            try {
                if (row.get(0).matches("Дата")) {
                    row = parseChartRow(iterator.next());
                    continue;
                }
                if (row.get(0).equals("Всього")) return paymentDetailsList;
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

    private int parseStationCode(String s) {
        if(s.matches("\\d{5,6}")){
            return Integer.parseInt(s);
        }
        else return -1;
    }

}
