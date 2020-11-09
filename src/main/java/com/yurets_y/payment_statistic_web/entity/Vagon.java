package com.yurets_y.payment_statistic_web.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Vagon  extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.FullView.class)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "docNumber", referencedColumnName = "docNumber"),
            @JoinColumn(name = "dateStamp", referencedColumnName = "dateStamp")
    })
    private RailroadDocument railroadDocument;

    @JsonView(Views.ShortView.class)
    private int number;

    private int grossWeight;

    @JsonView(Views.ShortView.class)
    private int netWeight;

    @JsonView(Views.ShortView.class)
    private int tareWeight;

    private double carryingCapacity;

    private int payment;

    @OneToOne(
            cascade = {CascadeType.ALL},
            orphanRemoval = true )
    @JsonView(Views.NormalView.class)
    private AditionalVagonInfo vagonInfo;

    @ElementCollection
//            (fetch=FetchType.EAGER)
    private List<String> zpuList;

    public Vagon() {
    }

    public Vagon(int number, int netWeight, int tareWeight) {
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
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

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public RailroadDocument getRailroadDocument() {
        return railroadDocument;
    }

    public void setRailroadDocument(RailroadDocument railroadDocument) {
        this.railroadDocument = railroadDocument;
    }


    public AditionalVagonInfo getVagonInfo() {
        return vagonInfo;
    }

    public void setVagonInfo(AditionalVagonInfo vagonInfo) {
        this.vagonInfo = vagonInfo;
    }

    @Override
    public String toString() {
        return String.format("%s гп: %.1f; нетто: %d, тара %d, брутто: %d.", number, carryingCapacity, netWeight, tareWeight, grossWeight);
    }

}