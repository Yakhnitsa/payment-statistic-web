package com.yurets_y.payment_statistic_web.entity;

import java.util.ArrayList;
import java.util.List;

public class Vagon {

    private Long id;

    private RailroadDocument railroadDocument;

    private String number;
    private int grossWeight;
    private int netWeight;
    private int tareWeight;
    private double carryingCapacity;
    private double payment;

    private List<String> zpuList;

    public Vagon() {
    }

    public Vagon(String number, int netWeight, int tareWeight) {
        zpuList = new ArrayList<>();
        this.number = number;
        this.netWeight = netWeight;
        this.tareWeight = tareWeight;
        this.grossWeight = netWeight + tareWeight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(int grossWeight) {
        this.grossWeight = grossWeight;
    }

    public int getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(int netWeight) {
        this.netWeight = netWeight;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(int tareWeight) {
        this.tareWeight = tareWeight;
    }

    public double getCarryingCapacity() {
        return carryingCapacity;
    }

    public void setCarryingCapacity(double carryingCapacity) {
        this.carryingCapacity = carryingCapacity;
    }

    public void addZpu(String zpu){
        zpuList.add(zpu);
    }
    public int getZpuCount(){
        return zpuList.size();
    }
    public String getZpuList(){
        return String.join(", ",zpuList);
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public RailroadDocument getRailroadDocument() {
        return railroadDocument;
    }

    public void setRailroadDocument(RailroadDocument railroadDocument) {
        this.railroadDocument = railroadDocument;
    }

    @Override
    public String toString() {
        return String.format("%s гп: %.1f; нетто: %d, тара %d, брутто: %d.", number, carryingCapacity, netWeight, tareWeight, grossWeight);
    }

}