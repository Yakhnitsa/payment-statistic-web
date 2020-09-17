package com.yurets_y.payment_statistic_web.service.parser_services;


import com.yurets_y.payment_statistic_web.entity.Client;
import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.Station;
import com.yurets_y.payment_statistic_web.entity.Vagon;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



@Service("doc-parser-service")
public class RailroadDocumentParserImpl implements RailroadDocumentsParser {

    /*
     * Парсинг жд документа из файла
     */
    @Override
    public RailroadDocument parseFromFile(File file) throws IOException, ParseException {
        Document document = Jsoup.parse(file, "UTF-8");
        if(!file.getName().toLowerCase().endsWith(".xml")){
            throw new IOException("Неизвесный формат файла!!!");
        }
        RailroadDocument railDoc = parseDocument(document);
        if(railDoc != null){
            updateVagonInformation(document,railDoc);
            updateNetWeight(document,railDoc);
            setVagonPaymentToDoc(document,railDoc);

        }

        return railDoc;
    }

    /*
     * Парсинг данных по документу из представления xml документа
     */
    private RailroadDocument parseDocument(Document jSoupDocument) throws ParseException {

//        String docNumb = getAttributeValue(jSoupDocument,"nom_doc");
//        if(!docNumb.matches("\\d{8}"))throw new RuntimeException("Ошибка парсинга номера накладной");
        Date docDate = getDateFromString(getAttributeValue(jSoupDocument,"date_otpr"));
        int docNumb = parseNumber(getAttributeValue(jSoupDocument,"nom_doc"),"\\d{8}");

        RailroadDocument railDoc = new RailroadDocument(docNumb,docDate);

        Date deliveryDate = getDateFromString(getAttributeValue(jSoupDocument,"date_grpol"));
        railDoc.setDelDate(deliveryDate);

        Date credDate = getDateFromString(getAttributeValue(jSoupDocument,"date_vid"));
        railDoc.setCredDate(credDate);

        //Добавление учасников трансп процесса:
        Client sender = getTransportMember(jSoupDocument.getElementsByAttributeValue("type", "1"));
        railDoc.setCargoSender(sender);
        Client receiver = getTransportMember(jSoupDocument.getElementsByAttributeValue("type", "2"));
        railDoc.setCargoReceiver(receiver);

        //добавление инфо про плательщика
        Client tarifPayer = parseTarifPayer(jSoupDocument.getElementsByAttributeValue("type", "0"));
        railDoc.setTarifPayer(tarifPayer);

        //добавление инфы про маршрут:
        railDoc.setSendStation(getSendStation(jSoupDocument));
        railDoc.setReceiveStation(getReceiveStation(jSoupDocument));

//        //добавление информации о перевозчиках
//        parseAndAddCarriers(jSoupDocument,railDoc);

        //добавление инфо по вагонам:
        railDoc.addVagons(getVagonsInfo(jSoupDocument));

        //Добавление инфо по грузу:
        railDoc.setCargoName(getAttributeValue(jSoupDocument,"name_etsng"));
        railDoc.setCargoCode(getAttributeValue(jSoupDocument,"kod_etsng"));

        //Определение платы по документу
        String payment = jSoupDocument.getElementsByAttributeValue("type_pay", "0").attr("osum");
        railDoc.setPayment(parseNumber(payment,"\\d+"));

        //Определение информации 7 и 15 графы
        String column7 = getAttributeValue(jSoupDocument,"zayava");
        String column15 = getAttributeValue(jSoupDocument,"marks");
        railDoc.setColumn7info(column7);
        railDoc.setColumn15info(column15);
        //Определение тарифного рассотяния
        String tarifDistance = getAttributeValue(jSoupDocument,"distance_way");
        railDoc.setTarifDistance(parseNumber(tarifDistance,"\\d+"));

        railDoc.putStamps(getStamps(jSoupDocument));

        return railDoc;
    }

    /*
     * Добавление инфы о станциях из документа
     */
    private Station getSendStation(Document jsoupDoc){
        Integer stationCode = parseNumber(getAttributeValue(jsoupDoc,"stn_from"),"\\d{4,6}");
        String stationName = getAttributeValue(jsoupDoc,"name_from");
        return new Station(stationCode,stationName);
    }

    private Station getReceiveStation(Document jsoupDoc){
        Integer stationCode = parseNumber(getAttributeValue(jsoupDoc,"stn_to"),"\\d{4,6}");
        String stationName = getAttributeValue(jsoupDoc,"name_to");
        return new Station(stationCode,stationName);
    }

//    private void parseAndAddCarriers(Document jSoupDoc, RailroadDocument railDoc){
//
//        Elements carrierElements = jSoupDoc.getElementsByTag("CARRIER");
//        carrierElements.forEach(element ->{
//            String codeInn = element.attr("esr_in");
//            String nameInn = element.attr("esr_name_in");
//            String codeOut = element.attr("esr_out");
//            String nameOut = element.attr("esr_name_out");
//            RailroadDocument.Station innStation = new RailroadDocument.Station(nameInn,codeInn);
//            RailroadDocument.Station outStation = new RailroadDocument.Station(nameOut,codeOut);
//            RailroadDocument.Carrier carrier =  new RailroadDocument.Carrier(innStation,outStation);
//            railDoc.addCarrier(carrier);
//                }
//        );
//    }

//    private void parseAndAddCarriers(Document jSoupDoc, RailroadDocument railDoc){
//
//        Elements carrierElements = jSoupDoc.getElementsByTag("CARRIER");
//        carrierElements.forEach(element ->{
//                    String codeInn = element.attr("esr_in");
//                    String nameInn = element.attr("esr_name_in");
//                    String codeOut = element.attr("esr_out");
//                    String nameOut = element.attr("esr_name_out");
//                    RailroadDocument.Station innStation = new RailroadDocument.Station(nameInn,codeInn);
//                    RailroadDocument.Station outStation = new RailroadDocument.Station(nameOut,codeOut);
//                    RailroadDocument.Carrier carrier =  new RailroadDocument.Carrier(innStation,outStation);
//                    railDoc.addCarrier(carrier);
//                }
//        );
//    }

    /*
     * Добавление инфо о получателе/отправителе из єлемента документа // Completed
     */
    private Client getTransportMember(Elements element) {

        Client client = new Client();
        client.setName(element.attr("name"));
        client.setAddress(element.attr("adress"));

        client.setRailroadCode(parseNumber(element.attr("kod"),"\\d+"));
        client.setEdrpuCode(parseNumber(element.attr("okpo"),"\\d+"));

        return client;
    }

    /*
     *
     */
    private Client parseTarifPayer(Elements elements) {
        Client tarifPayer = new Client();
        tarifPayer.setName(elements.attr("name_plat"));
        tarifPayer.setRailroadCode(parseNumber(elements.attr("kod_plat"),"\\d+"));
        return tarifPayer;
    }

    /*
     * Добавление инфо про вагоны //Completed
     */
    private List<Vagon> getVagonsInfo(Document jSoupDoc) {
        List<Vagon> vagonList = new ArrayList<>();
        Element docBody = jSoupDoc
                .getElementsByTag("document-data")
                .first()
                .getElementsByTag("uz-rwc-doc").first();

        if(docBody == null){
            docBody = jSoupDoc
                    .getElementsByTag("uz-rwc-doc")
                    .first()
                    .getElementsByTag("document-data").first();
        }

        Elements elements = docBody.getElementsByTag("VAGON");
        for (Element element : elements) {
            String number = element.getElementsByAttribute("nomer").attr("nomer");
            int netWeight = 0;
            int tareWeight = 0;
            try {
                netWeight = parseNumber(element.getElementsByAttribute("vesg").attr("vesg"),"\\d+");
                tareWeight = parseNumber(element.getElementsByAttribute("ves_tary_arc").attr("u_tara"),"\\d+");
                if(tareWeight == -1){
                    tareWeight = parseNumber(element.getElementsByAttribute("ves_tary_arc").attr("ves_tary_arc"),"\\d+");
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if((number==null)||(number.equals(""))){
                continue;
            }

            Vagon vagon = new Vagon(number, netWeight, tareWeight);

            double capasity = 0;
            try {
                capasity = Double.parseDouble(element.getElementsByAttribute("gruzp").attr("gruzp"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            vagon.setCarryingCapacity(capasity);

            Elements zpuSet = element.getElementsByAttribute("nom_zpu");
            for(Element zpu: zpuSet){
                vagon.addZpu(zpu.getElementsByAttribute("nom_zpu").attr("nom_zpu"));
            }

            vagonList.add(vagon);
        }
        return vagonList;
    }

    /*
     * Проверка документа на предмет изменения веса
     */
    private void updateNetWeight(Document jSoupDoc, RailroadDocument railDoc){
        int vagonCount = railDoc.getVagonCount();
        String atributeKey = "target";
        String valueFormat = "OTPR/VAGON[%1$d]/COLLECT_V[1]";
        for(int i = 0; i < vagonCount;){
            Vagon vagon = railDoc.getVagonList().get(i++);
            String ves = null;
            try {
                String value = String.format(valueFormat,i);
                Elements elements = jSoupDoc.getElementsByAttributeValue(atributeKey,value);
                for(Element element: elements){
                    ves = element.getElementsByAttribute("vesg").attr("vesg");
                    if(ves.matches("\\d+")){
                        vagon.setNetWeight(Integer.parseInt(ves));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


/*    private void updateDocument(Document jSoupDoc, RailroadDocument railDoc){
        Element updateBody = jSoupDoc.getElementsByTag("changes").first();
        Elements updatedElements = updateBody.getElementsByTag("update");
//        int vagonCount = railDoc.getVagonCount();
//        String paymentFormat = "OTPR/VAGON[%1$d]/PAY_V[1]";
//        String vagonFormat = "OTPR/VAGON[%1$d]";
//        for(Element element: updatedElements){
//            Elements vagon1changes = element.getElementsByAttributeValue("target","OTPR/VAGON[1]");
//            System.out.println(vagon1changes);
//        }
    }*/
    /*
     * Обновление информации по номерам вагонов
     */
    private void updateVagonInformation(Document jSoupDoc, RailroadDocument railDoc){
        int vagonCount = railDoc.getVagonCount();
        String attributeKey = "target";
        String valueFormat = "OTPR/VAGON[%1$d]";
        for(int i = vagonCount; i >0 ;i--){
            Vagon vagon = railDoc.getVagonList().get(i-1);
            String vagNumb = null;
            String UpdateUTara = null;
            String tagname = "";
            try {
                String value = String.format(valueFormat,i);
                Elements elements = jSoupDoc.getElementsByAttributeValue(attributeKey,value);
                if(elements.first() != null){
                    tagname = elements.first().tagName();
                    vagNumb = elements.first().getElementsByAttribute("nomer").attr("nomer");
                    UpdateUTara = elements.first().getElementsByAttribute("u_tara").attr("u_tara");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if(tagname.equals("delete")) {
                railDoc.getVagonList().remove(i-1);
                continue;
            }
            if((vagNumb != null)&&(!vagNumb.equals(""))){
                try{
                    vagon.setNumber(vagNumb);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if((UpdateUTara != null)&&(!UpdateUTara.equals(""))){
                try{
                    vagon.setTareWeight(Integer.parseInt(UpdateUTara));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    /*
     * Добавление ЖД тарифа на вагон
     */
    private void setVagonPaymentToDoc(Document jSoupDoc, RailroadDocument railDoc){
        int vagonCount = railDoc.getVagonCount();
        String atributeKey = "target";
        String valueFormat = "OTPR/VAGON[%1$d]/PAY_V[1]";
        for(int i = 0; i < vagonCount;){
            Vagon vagon = railDoc.getVagonList().get(i++);
            String paymentStr = null;
            try {
                String value = String.format(valueFormat,i);
                Elements elements = jSoupDoc.getElementsByAttributeValue(atributeKey,value);
                if(elements.first() != null){
                    paymentStr = elements.first().getElementsByAttribute("summa").attr("summa");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(paymentStr != null){
                vagon.setPayment(parseNumber(paymentStr,"\\d+"));
            }
        }
    }

    private Map<String,String> getStamps(Document jSoupDoc){
        Map<String,String> stampsMap = new HashMap<>();
        Elements stamps = jSoupDoc.getElementsByTag("SHTEMPEL");

        for(Element element : stamps){
            String stampNumb = element.attr("nom_sht");
            String stampText = element.attr("info_sht");
            stampsMap.put(stampNumb,stampText);
        }
        return stampsMap;
    }

    private Date getDateFromString(String stringDate){
        if ((stringDate == null)||(stringDate.equals("")))
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
            try {
                date = dateFormat.parse(stringDate);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return date;
    }

    private String getAttributeValue(Document jSoupDocument, String attribute){
        return jSoupDocument.getElementsByAttribute(attribute).attr(attribute);
    }
    private int parseNumber(String numbString, String testPattern){
        return numbString.matches(testPattern) ? Integer.parseInt(numbString) : -1;
    }

}
