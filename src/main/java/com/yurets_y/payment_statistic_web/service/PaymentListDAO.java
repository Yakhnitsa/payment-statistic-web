package com.yurets_y.payment_statistic_web.service;


import com.yurets_y.payment_statistic_web.entity.PaymentDetails;
import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import com.yurets_y.payment_statistic_web.repo.PaymentDetailsRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Date;
import java.util.List;


//@Service("paymentListDao")
public class PaymentListDAO implements PaymentListService {

    private EntityManagerFactory emf;

    private EntityManager em;

    private PaymentDetailsRepo paymentDetailsRepo;

//    @Value("${service.backup-path}")
    private String backupDir;

    private List<PaymentList> tempList = new ArrayList<>();

//    @Autowired
//    public PaymentListDAO(
//            @Qualifier("defaultEMF") EntityManagerFactory entityManager,
//            PaymentDetailsRepo paymentDetailsRepo) {
//        this.emf = entityManager;
//        this.paymentDetailsRepo = paymentDetailsRepo;
//    }
//
//    public PaymentListDAO(
//            @Qualifier("defaultEMF") EntityManagerFactory entityManager) {
//        this.emf = entityManager;
//    }

    @Override
    public synchronized void add(PaymentList paymentList) {
        openEntityManager();
        beginTransaction();
        saveBackupFile(paymentList);
        testBeforeSave(paymentList);
        em.persist(paymentList);
        commitTransaction();
        closeEntityManager();
    }

    private void testBeforeSave(PaymentList paymentList) {
        if (paymentList.getBackupFilePath() == null) {
            throw new NullPointerException("Ошибка backup файла для перечня " + paymentList);
        }
    }

    @Override
    public synchronized void update(PaymentList paymentList) {
        openEntityManager();
        beginTransaction();
        saveBackupFile(paymentList);
        testBeforeSave(paymentList);
        em.merge(paymentList);
        commitTransaction();
        closeEntityManager();
    }

    @Override
    public synchronized boolean remove(PaymentList paymentList) {
        return removeById(paymentList.getId());
    }

    public synchronized boolean removeById(PaymentListId id) {
        openEntityManager();
        beginTransaction();
        PaymentList list = em.find(PaymentList.class, id);
        if (list != null) {
            em.remove(list);
            deleteBackupFile(list);
            commitTransaction();
            closeEntityManager();
            return true;
        }
        commitTransaction();
        closeEntityManager();
        return false;
    }

    @Override
    public synchronized PaymentList getById(PaymentListId id) {
        openEntityManager();
        beginTransaction();
        PaymentList listFromRepo = em.find(PaymentList.class, id);
        if (listFromRepo != null) {
            Hibernate.initialize(listFromRepo.getPaymentDetailsList());
            commitTransaction();
            closeEntityManager();
            loadBackupFile(listFromRepo);
        }

        return listFromRepo;

    }

    @Override
    public synchronized List<PaymentList> getAll() {
        openEntityManager();
        beginTransaction();
        List<PaymentList> paymentLists = em.createQuery("FROM PaymentList", PaymentList.class).getResultList();
        paymentLists.forEach(this::loadBackupFile);
        commitTransaction();
        closeEntityManager();
        return paymentLists;
    }

    @Override
    public synchronized List<PaymentList> getByPeriod(Date from, Date until) {
        openEntityManager();
        beginTransaction();
        List<PaymentList> sortedList = em
                .createQuery("FROM PaymentList WHERE date BETWEEN :dateFrom AND :dateUntil", PaymentList.class)
                .setParameter("dateFrom", from, TemporalType.DATE)
                .setParameter("dateUntil", until, TemporalType.DATE)
                .getResultList();
//        List<PaymentList> sortedList = em.createQuery("from PaymentList",PaymentList.class).getResultList();

        sortedList.forEach(this::loadBackupFile);
        commitTransaction();
        closeEntityManager();
        return sortedList;
    }

    @Override
    public boolean contains(PaymentList paymentList) {
        openEntityManager();
        beginTransaction();
        PaymentList listFromDB = em.find(PaymentList.class, paymentList.getId());
        commitTransaction();
        closeEntityManager();
        return listFromDB != null;
    }

    /*Методы для получения деталей платежей*/

    public synchronized List<PaymentDetails> getDetailsByPeriod(Date from, Date until) {
        openEntityManager();
        beginTransaction();
        List<PaymentDetails> sortedList = em
                .createQuery("FROM PaymentDetails WHERE date BETWEEN :dateFrom AND :dateUntil", PaymentDetails.class)
                .setParameter("dateFrom", from, TemporalType.DATE)
                .setParameter("dateUntil", until, TemporalType.DATE)
                .getResultList();

        commitTransaction();
        closeEntityManager();
        return sortedList;
    }

    public synchronized List<PaymentDetails> getPaymentDetailsByStationCode(int stationCode) {
        openEntityManager();
        beginTransaction();
        List<PaymentDetails> list = em
                .createQuery("FROM PaymentDetails WHERE stationCode = :stationCode", PaymentDetails.class)
                .setParameter("stationCode", stationCode)
                .getResultList();
        commitTransaction();
        closeEntityManager();
        return list;
    }

    public synchronized List<PaymentDetails> getPaymentDetailsByDate(Date from, Date until) {
        openEntityManager();
        beginTransaction();
        List<PaymentDetails> list = em
                .createQuery("FROM PaymentDetails WHERE date BETWEEN :dateFrom and :dateUntil", PaymentDetails.class)
                .setParameter("dateFrom", from)
                .setParameter("dateUntil", until)
                .getResultList();
        commitTransaction();
        closeEntityManager();
//        return list;
//        TODO протестить и отпустить
//         paymentDetailsRepo.findAllByDateBetween(from,until);
        List<PaymentDetails> result = paymentDetailsRepo.findAllByDateBetween(from,until);
        return result;
    }


    /*Служебные методы...*/

    private void saveBackupFile(PaymentList paymentList) {
        String fileExtension = paymentList.getBackupFile().getName();
        fileExtension = fileExtension.substring(fileExtension.lastIndexOf("."));
        String fileName = paymentList.getPayerCode() + "_" + paymentList.getNumber() + fileExtension;
        File backupFile = new File(backupDir + File.separator + fileName);

        try {
            File fileFromList = paymentList.getBackupFile();
            if (!fileFromList.exists())
                Files.createFile(backupFile.toPath());

            Files.copy(fileFromList.toPath(),
                    backupFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при сохранении файла " + fileName);
        }
        if (fileName == null) {
            throw new NullPointerException("Отсутствует файл для перечня " + paymentList);
        }
        paymentList.setBackupFilePath(fileName);

    }

    private void deleteBackupFile(PaymentList list) {
        File file = new File(backupDir + File.separator + list.getBackupFilePath());
        if (file.exists()) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException("Ошибка удаления файла перечня " + file);
            }
        }
        if (file.exists()) {
            throw new RuntimeException("Файл какого-то хера не удалился " + file);
        }

    }

    private void loadBackupFile(PaymentList paymentList) {
        File file = new File(backupDir + File.separator + paymentList.getBackupFilePath());
        if (!file.exists()) {

            throw new RuntimeException("Ошибка загрузки файла для перечня " + paymentList.toString());
        }
        paymentList.setBackupFile(file);
    }


    private void openEntityManager() {
        if (em == null || !em.isOpen()) {
            this.em = emf.createEntityManager();
        }
    }

    private void closeEntityManager() {
        if (em.isOpen()) em.close();
    }

    private void beginTransaction() {
        em.getTransaction().begin();
    }

    private void commitTransaction() {
        em.getTransaction().commit();
    }

}
