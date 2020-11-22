package com.yurets_y.payment_statistic_web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PAYMENT_DETAILS")
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "number", referencedColumnName = "number"),
            @JoinColumn(name = "payerCode", referencedColumnName = "payerCode")
    })
//    @JsonView(Views.ShortView.class)
    private PaymentList paymentList;

    @JsonView(Views.ShortView.class)
    private String type;

    private IncomeType incomeType;

    @Temporal(TemporalType.DATE)
    @JsonView(Views.ShortView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    @JsonView(Views.ShortView.class)
    private int stationCode;

    @JsonView(Views.ShortView.class)
    private String stationName;

    @JsonView(Views.ShortView.class)
    private String documentNumber;
    private String paymentCode;
    private String paymentDescription;

    @JsonView(Views.ShortView.class)
    private long payment;
    @JsonView(Views.ShortView.class)
    private long additionalPayment;
    @JsonView(Views.ShortView.class)
    private long taxPayment;
    @JsonView(Views.ShortView.class)
    private long totalPayment;

    @JsonView(Views.ShortView.class)
    public Integer getPaymentListNumber(){
        return paymentList.getNumber();
    }

    @JsonView(Views.ShortView.class)
    public Integer getPayerCode(){
        return paymentList.getPayerCode();
    }


    public void setId(Long id) {
        this.id = id;
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    public PaymentDetails() {
    }

    public PaymentList getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(PaymentList paymentList) {
        this.paymentList = paymentList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStationCode() {
        return stationCode;
    }

    public void setStationCode(int stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public long getPayment() {
        return payment;
    }

    public void setPayment(long payment) {
        this.payment = payment;
    }

    public long getAdditionalPayment() {
        return additionalPayment;
    }

    public void setAdditionalPayment(long additionalPayment) {
        this.additionalPayment = additionalPayment;
    }

    public long getTaxPayment() {
        return taxPayment;
    }

    public void setTaxPayment(long taxPayment) {
        this.taxPayment = taxPayment;
    }

    public long getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(long totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Long getId() {
        return id;
    }



    public static enum IncomeType {
        INCOME,
        OUTCOME
    }
}
