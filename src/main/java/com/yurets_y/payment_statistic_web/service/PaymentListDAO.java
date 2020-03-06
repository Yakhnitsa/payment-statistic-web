package com.yurets_y.payment_statistic_web.service;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TemporalType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service("paymentListDao")
public class PaymentListDAO {

    private EntityManagerFactory emf;

    private EntityManager em;

    @Value("${service.backup-path}")
    private String backupDir;

    private List<PaymentList> tempList = new ArrayList<>();


    public PaymentListDAO(@Qualifier("defaultEMF") EntityManagerFactory entityManager) {
        this.emf = entityManager;
    }

    public void add(PaymentList paymentList) {
        openEntityManager();
        beginTransaction();
        saveBackupFile(paymentList);
        em.persist(paymentList);
        commitTransaction();
        closeEntityManager();
    }

    public void update(PaymentList paymentList) {
        openEntityManager();
        beginTransaction();
        em.merge(paymentList);
        commitTransaction();
        closeEntityManager();
    }

    public void remove(PaymentList paymentList) {
        removeById(paymentList.getId());
    }

    public boolean removeById(PaymentListId id){
        openEntityManager();
        beginTransaction();
        PaymentList list = em.find(PaymentList.class, id);
        if (list != null) {

            em.remove(list);
            commitTransaction();
            closeEntityManager();
            return true;
        }

        //TODO Настроить удаление backup файлов!!!
        commitTransaction();
        closeEntityManager();
        return false;
    }


    public PaymentList getById(PaymentListId id) {
        openEntityManager();
        beginTransaction();
        PaymentList listFromRepo = em.find(PaymentList.class, id);
        if(listFromRepo != null){
            Hibernate.initialize(listFromRepo.getPaymentDetailsList());
            commitTransaction();
            closeEntityManager();
            loadBackupFile(listFromRepo);
        }

        return listFromRepo;

    }

    public List<PaymentList> getAll() {
        openEntityManager();
        beginTransaction();
        List<PaymentList> paymentLists = em.createQuery("FROM PaymentList", PaymentList.class).getResultList();
        paymentLists.forEach(this::loadBackupFile);
        commitTransaction();
        closeEntityManager();
        return paymentLists;
    }

    public List<PaymentList> getByPeriod(Date from, Date until){
        openEntityManager();
        beginTransaction();
        List<PaymentList> sortedList = em
                .createQuery("FROM PaymentList WHERE date BETWEEN :dateFrom AND :dateUntil", PaymentList.class)
                .setParameter("dateFrom",from, TemporalType.DATE)
                .setParameter("dateUntil", until, TemporalType.DATE)
                .getResultList();
//        List<PaymentList> sortedList = em.createQuery("from PaymentList",PaymentList.class).getResultList();

        sortedList.forEach(this::loadBackupFile);
        commitTransaction();
        closeEntityManager();
        return sortedList;
    }

    public List<PaymentDetails> getPaymentDetailsByStationCode(int stationCode){
        openEntityManager();
        beginTransaction();
        List<PaymentDetails> list = em
                .createQuery("FROM PaymentDetails WHERE stationCode = :stationCode",PaymentDetails.class)
                .setParameter("stationCode",stationCode)
                .getResultList();
        commitTransaction();
        closeEntityManager();
        return list;
    }

    public List<PaymentDetails> getPaymentDetailsByDate(Date from, Date until){
        openEntityManager();
        beginTransaction();
        List<PaymentDetails> list = em
                .createQuery("FROM PaymentDetails WHERE date BETWEEN :dateFrom and :dateUntil",PaymentDetails.class)
                .setParameter("dateFrom",from)
                .setParameter("dateUntil",until)
                .getResultList();
        commitTransaction();
        closeEntityManager();
        return list;
    }

    private void openEntityManager(){
        if(em == null || !em.isOpen()){
            this.em = emf.createEntityManager();
        }
    }

    private void closeEntityManager(){
        if(em.isOpen()) em.close();
    }


    private void beginTransaction() {
        em.getTransaction().begin();
    }

    private void commitTransaction() {
        em.getTransaction().commit();
    }

    private void saveBackupFile(PaymentList paymentList){
        String fileExtension = paymentList.getBackupFile().getName();
        fileExtension = fileExtension.substring(fileExtension.lastIndexOf("."));
        String fileName = paymentList.getPayerCode() + "_" + paymentList.getNumber() + fileExtension;
        File backupFile = new File(backupDir + File.separator + fileName);

        try {
            File fileFromList = paymentList.getBackupFile();
            if(!fileFromList.exists())
                Files.createFile(backupFile.toPath());

            Files.copy(fileFromList.toPath(),
                    backupFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при сохранении файла " + fileName);
        }
        paymentList.setBackupFilePath(fileName);

    }

    private void loadBackupFile(PaymentList paymentList){
        File file = new File(backupDir + File.separator + paymentList.getBackupFilePath());
        if(!file.exists()){
            throw new RuntimeException("Ошибка загрузки файла перечня " + file);
        }
        paymentList.setBackupFile(file);
    }

    public boolean contains(PaymentList paymentList){
        openEntityManager();
        beginTransaction();
        PaymentList listFromDB = em.find(PaymentList.class,paymentList.getId());
        commitTransaction();
        closeEntityManager();
        return listFromDB != null;
    }

}
