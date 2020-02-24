package com.yurets_y.payment_statistic_web.entity;

import javax.persistence.*;
import java.io.File;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@IdClass(PaymentListId.class)
@Table(name = "PAYMENT_LIST")
public class PaymentList {
    @Id
    @Column(updatable = false)
    @JsonView(Views.ShortView.class)
    private int number;
    @Id
    @Column(updatable = false)
    @JsonView(Views.ShortView.class)
    private int payerCode;

    @Temporal(TemporalType.DATE)
    @JsonView(Views.ShortView.class)
    private Date date;

    private int taxCode;
    private String payerName;
    private String contractNumber;

    @JsonView(Views.ShortView.class)
    private long openingBalance;

    @JsonView(Views.ShortView.class)
    private long closingBalance;

    @JsonView(Views.NormalView.class)
    private long payments;

    @JsonView(Views.NormalView.class)
    private long paymentTaxes;

    @JsonView(Views.ShortView.class)
    private long paymentVsTaxes;

    @OneToMany(mappedBy="paymentList", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonView(Views.FullView.class)
    private List<PaymentDetails> paymentDetailsList = new ArrayList<>();

    @Transient
    private File backupFile;

    @JsonView(Views.ShortView.class)
    private String backupFilePath;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) throws Exception{
        if(this.number != 0)
            throw new Exception("Запрет повторного изменения поля, составляющего ID");
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPayerCode() {
        return payerCode;
    }

    public void setPayerCode(int paymentCode) throws Exception{
        if(this.payerCode != 0)
            throw new Exception("Запрет повторного изменения поля, составляющего ID");
        this.payerCode = paymentCode;
    }

    public int getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(int taxCode) {
        this.taxCode = taxCode;
    }

    public long getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(long openingBalance) {
        this.openingBalance = openingBalance;
    }

    public long getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(long closingBalance) {
        this.closingBalance = closingBalance;
    }

    public long getPayments() {
        return payments;
    }

    public void setPayments(long payments) {
        this.payments = payments;
    }

    public long getPaymentTaxes() {
        return paymentTaxes;
    }

    public void setPaymentTaxes(long paymentTaxes) {
        this.paymentTaxes = paymentTaxes;
    }

    public long getPaymentVsTaxes() {
        return paymentVsTaxes;
    }

    public void setPaymentVsTaxes(long paymentVsTaxes) {
        this.paymentVsTaxes = paymentVsTaxes;
    }

    public List<PaymentDetails> getPaymentDetailsList() {
        return paymentDetailsList;
    }


    public File getBackupFile() {
        return backupFile;
    }

    public void setBackupFile(File backupFile) {
        this.backupFile = backupFile;
    }

    public boolean containsDetail(Object o) {
        return paymentDetailsList.contains(o);
    }

    public Iterator<PaymentDetails> detailsIterator() {
        return Collections.unmodifiableCollection(paymentDetailsList).iterator();
    }

    public boolean addDetail(PaymentDetails paymentDetails) {
        paymentDetails.setPaymentList(this);
        return paymentDetailsList.add(paymentDetails);
    }

    public boolean removeDetail(Object o) {
        return paymentDetailsList.remove(o);
    }

    public boolean addAll(Collection<? extends PaymentDetails> c) {
        c.forEach(pl -> pl.setPaymentList(this));
        return paymentDetailsList.addAll(c);
    }

    public String getBackupFilePath() {
        return backupFilePath;
    }

    public void setBackupFilePath(String backupFilePath) {
        this.backupFilePath = backupFilePath;
    }
}
