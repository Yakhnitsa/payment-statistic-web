package com.yurets_y.payment_statistic_web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.File;
import java.util.*;

@Entity
@IdClass(RailroadDocumentId.class)
@Table(name = "RAILROAD_DOCUMENT")
public class RailroadDocument extends AuditableEntity {
    @Id
    @Column(updatable = false)
    @JsonView(Views.ShortView.class)
    private Integer docNumber;

    @Id
    @Column(updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonView(Views.ShortView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateStamp;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(Views.ShortView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private Date docDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private Date delDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private Date notfDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private Date credDate;

    @ManyToOne
    @NotFound(action=NotFoundAction.IGNORE)
    @JsonView(Views.ShortView.class)
    private Station sendStation;

    @ManyToOne
    @NotFound(action=NotFoundAction.IGNORE)
    @JsonView(Views.ShortView.class)
    private Station receiveStation;

    @OneToOne(
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    @JsonView(Views.ShortView.class)
    private Client cargoSender;

    @OneToOne(
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    @JsonView(Views.ShortView.class)
    private Client cargoReceiver;

    @OneToOne(
            cascade = {CascadeType.ALL},
            orphanRemoval = true)
    @JsonView(Views.ShortView.class)
    private Client tarifPayer;

    @Transient
    @JsonView(Views.ShortView.class)
    private File xmlBackupFile;

    @JsonView(Views.NormalView.class)
    private String xmlBackupFilePath;

    @Transient
    @JsonView(Views.ShortView.class)
    private File pdfBackupFile;

    @JsonView(Views.NormalView.class)
    private String pdfBackupFilePath;

    @JsonView(Views.ShortView.class)
    private String cargoName;

    @JsonView(Views.ShortView.class)
    private String cargoCode;

    @JsonView(Views.NormalView.class)
    private int payment;

    @JsonView(Views.NormalView.class)
    private int tarifDistance;

    @JsonView(Views.NormalView.class)
    private String column7info;

    @JsonView(Views.NormalView.class)
    private String column15info;

    @OneToMany(mappedBy="railroadDocument", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Vagon> vagonList = new ArrayList<>();

    @ElementCollection
            (fetch=FetchType.EAGER)
    @JsonView(Views.NormalView.class)
    private Map<String, String> stamps = new HashMap<String, String>();

    public RailroadDocument() {
    }

    public RailroadDocument(Integer docNumber, Date dateStamp) {
        this.docNumber = docNumber;
        this.dateStamp = dateStamp;
        this.docDate = dateStamp;
    }

    /*
     * getters and setters:
     */
    public void addVagon(Vagon vagon) {
        vagon.setRailroadDocument(this);
        vagonList.add(vagon);
    }

    public void addVagons(Collection<Vagon> vagons) {
        vagons.forEach(this::addVagon);
    }

    @JsonView(Views.ExtendedView.class)
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

    public Integer getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(Integer docNumber) {
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

    public Date getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(Date dateStamp) {
        this.dateStamp = dateStamp;
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
    @JsonView(Views.NormalView.class)
    public int getFullWeight() {
        int fullWeight = 0;
        for (Vagon vagon : vagonList) {
            fullWeight += vagon.getNetWeight();
        }
        return fullWeight;
    }

    @JsonView(Views.NormalView.class)
    public int getVagonCount() {
        return vagonList.size();
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
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

//    public List<Carrier> getCarriers() {
//        return carriers;
//    }
//
//    public void addCarrier(Carrier carrier){
//        carriers.add(carrier);
//    }

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

    public File getXmlBackupFile() {
        return xmlBackupFile;
    }

    public void setXmlBackupFile(File xmlBackupFile) {
        this.xmlBackupFile = xmlBackupFile;
    }

    public File getPdfBackupFile() {
        return pdfBackupFile;
    }

    public void setPdfBackupFile(File pdfBackupFile) {
        this.pdfBackupFile = pdfBackupFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RailroadDocument that = (RailroadDocument) o;

        if (!docNumber.equals(that.docNumber)) return false;
        return dateStamp != null ? dateStamp.equals(that.dateStamp) : that.dateStamp == null;
    }

    @Override
    public int hashCode() {
        int result = docNumber.hashCode();
        result = 31 * result + (dateStamp != null ? dateStamp.hashCode() : 0);
        return result;
    }

    public String getXmlBackupFilePath() {
        return xmlBackupFilePath;
    }

    public void setXmlBackupFilePath(String xmlBackupFilePath) {
        this.xmlBackupFilePath = xmlBackupFilePath;
    }

    public String getPdfBackupFilePath() {
        return pdfBackupFilePath;
    }

    public void setPdfBackupFilePath(String pdfBackupFilePath) {
        this.pdfBackupFilePath = pdfBackupFilePath;
    }
}
