package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service("paymentListServiceRepoBased")
@Primary
public class PaymentListServiceRepoImpl implements PaymentListService {

    @Value("${service.backup-path}")
    private String backupDir;

    private PaymentListRepo paymentListRepo;

    public PaymentListServiceRepoImpl() {
    }

    @Autowired
    public PaymentListServiceRepoImpl(PaymentListRepo paymentListRepo) {
        this.paymentListRepo = paymentListRepo;
    }

    @Override
    public void add(PaymentList paymentList) {
        saveBackupFile(paymentList);
        testBeforeSave(paymentList);
        paymentListRepo.save(paymentList);
    }

    @Override
    public void update(PaymentList paymentList) {
        add(paymentList);
    }

    @Override
    public boolean remove(PaymentList paymentList) {

        boolean exists = paymentListRepo.existsById(paymentList.getId());
        if (exists) {
            paymentListRepo.delete(paymentList);
            deleteBackupFile(paymentList);
        }
        return exists;
    }

    @Override
    public PaymentList getById(PaymentListId id) {

        PaymentList listFromRepo = paymentListRepo.getOne(id);
        if (listFromRepo != null) {
            loadBackupFile(listFromRepo);
        }
        return listFromRepo;
    }

    @Override
    public List<PaymentList> getAll() {
        List<PaymentList> list = paymentListRepo.findAll();
        list.forEach(this::loadBackupFile);
        return list;
    }

    @Override
    public List<PaymentList> getByPeriod(Date from, Date until) {
        List<PaymentList> list = paymentListRepo.getAllByDateBetween(from,until);
        list.forEach(this::loadBackupFile);
        return list;
    }

    @Override
    public boolean contains(PaymentList paymentList) {
        return paymentListRepo.existsById(paymentList.getId());
    }

    private void testBeforeSave(@NotNull PaymentList paymentList) {
        if (paymentList.getBackupFilePath() == null) {
            throw new NullPointerException("Ошибка backup файла для перечня " + paymentList);
        }
    }

    private void saveBackupFile(@NotNull PaymentList paymentList) {
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

    private void deleteBackupFile(@NotNull PaymentList list) {
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

    private void loadBackupFile(@NotNull PaymentList paymentList) {
        File file = new File(backupDir + File.separator + paymentList.getBackupFilePath());
        if (!file.exists()) {

            throw new RuntimeException("Ошибка загрузки файла для перечня " + paymentList.toString());
        }
        paymentList.setBackupFile(file);
    }
}
