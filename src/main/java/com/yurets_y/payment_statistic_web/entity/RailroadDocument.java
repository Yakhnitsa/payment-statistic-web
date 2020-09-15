package com.yurets_y.payment_statistic_web.entity;

import java.util.*;


public class RailroadDocument {
    private String docNumber;

    private Date docDate;
    private Date delDate;
    private Date notfDate;
    private Date credDate;

    private Station sendStation;
    private Station receiveStation;

    private Client cargoSender;
    private Client cargoReceiver;
    private Client tarifPayer;

    private List<Carrier> carriers = new ArrayList<>();

    private String cargoName;
    private String cargoCode;

    private int payment;

    private int tarifDistance;

    private String column7info;
    private String column15info;

    private List<Vagon> vagonList = new ArrayList<>();

    private Map<String, String> stamps = new HashMap<String, String>();

    /*
     * getters and setters:
     */
    public void addVagon(Vagon vagon) {
        vagonList.add(vagon);
    }
    public void addVagons(Collection vagons) {
        vagonList.addAll(vagons);
    }

    public List<Vagon> getVagonList() {
        return vagonList;
    }

    public Client getCargoReceiver() {
        return cargoReceiver;
    }

    public void setCargoReceiver(Client cargoReceiver) {
        this.cargoReceiver = cargoReceiver;
    }

    public Client getCargoSender() {
        return cargoSender;
    }

    public void setCargoSender(Client cargoSender) {
        this.cargoSender = cargoSender;
    }

    public Client getTarifPayer() {
        return tarifPayer;
    }

    public void setTarifPayer(Client tarifPayer) {
        this.tarifPayer = tarifPayer;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date date) {
        docDate = date;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    public Date getNotfDate() {
        return notfDate;
    }

    public void setNotfDate(Date notfDate) {
        this.notfDate = notfDate;
    }

    public Date getCredDate() {
        return credDate;
    }

    public void setCredDate(Date credDate) {
        this.credDate = credDate;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getCargoCode() {
        return cargoCode;
    }

    public void setCargoCode(String cargoCode) {
        this.cargoCode = cargoCode;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public Station getReceiveStation() {
        return receiveStation;
    }

    public void setReceiveStation(Station receiveStation) {
        this.receiveStation = receiveStation;
    }

    public Station getSendStation() {
        return sendStation;
    }

    public void setSendStation(Station sendStation) {
        this.sendStation = sendStation;
    }

//    public Station getOutStation() {
//        return carriers.size() > 0 ? carriers.get(0).getTo() : new Station();
//    }
//
//    public Station getInnStation() {
//        return carriers.size() > 1 ? carriers.get(1).getFrom(): new Station();
//    }

    /*
     * Получение полной массы груза
     */
    public int getFullWeight() {
        int fullWeight = 0;
        for (Vagon vagon : vagonList) {
            fullWeight += vagon.getNetWeight();
        }
        return fullWeight;
    }

    public int getVagonCount() {
        return vagonList.size();
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public void setPayment(String paymentString) {
        try {
            this.payment = Integer.parseInt(paymentString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public String getColumn7info() {
        return column7info;
    }

    public void setColumn7info(String column7info) {
        this.column7info = column7info;
    }

    public String getColumn15info() {
        return column15info;
    }

    public void setColumn15info(String column15info) {
        this.column15info = column15info;
    }

    public List<Carrier> getCarriers() {
        return carriers;
    }

    public void addCarrier(Carrier carrier){
        carriers.add(carrier);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("№ документа: %s, дата: %2$td/%2$tm/%2$tY%n", docNumber, docDate));
        sb.append(String.format("ст. Отправления: %s, код: %s%n", sendStation.getRusName(), sendStation.getCode()));
        sb.append(String.format("ст. Назначения: %s, код: %s%n", receiveStation.getRusName(), receiveStation.getCode()));
        sb.append(String.format("Отправитель: %s%n", cargoSender));
        sb.append(String.format("Получатель: %s%n", cargoReceiver));
        sb.append(String.format("Масса груза: %d кг%n", getFullWeight()));
        sb.append(String.format("Груз: %s, код груза: %s%n", cargoName, cargoCode));

        sb.append("Вагоны:\n");
        for (Vagon vagon : vagonList) {
            sb.append(String.format("\t%s%n", vagon));
        }

        return sb.toString();
    }




    public int getTarifDistance() {
        return tarifDistance;
    }

    public void setTarifDistance(int tarifDistance) {
        this.tarifDistance = tarifDistance;
    }

    public Map<String, String> getStamps() {
        return stamps;
    }

    public boolean containsStamp(String key) {
        return getStamps().containsKey(key);
    }

    public String getStampText(String key) {
        return getStamps().get(key);
    }

    public String putStamp(String key, String value) {
        return getStamps().put(key, value);
    }

    public void putStamps(Map<String,String> stamps) {
        this.stamps.putAll(stamps);
    }

}
