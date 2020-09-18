package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import com.yurets_y.payment_statistic_web.repo.PaymentListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service("paymentListServiceRepoBased")
@Primary
public class PaymentListServiceRepoImpl implements PaymentListService {

    @Value("${service.backup-path}")
    private String backupDir;

    private PaymentListRepo paymentListRepo;
//
//    public PaymentListServiceRepoImpl() {
//    }

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
    public List<String> getPaymentCodes() {
        return paymentListRepo.findAllPaymentCodes();
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
    public Page<PaymentList> getAll(Pageable pageable, Integer paymentCode) {
        Page<PaymentList> page = paymentListRepo.findAllByPayerCode(pageable,paymentCode);
        return page;
    }

    @Override
    public Page<PaymentList> getPageByPeriod(Pageable pageable, Date from, Date until, Integer paymentCode) {
        Page<PaymentList> page = paymentListRepo.findAllByDateBetweenAndPayerCode(pageable, from, until,paymentCode);
        return page;
    }

    @Override
    public List<PaymentList> getByPeriod(Date from, Date until, Integer paymentCode) {
        return paymentListRepo.findAllByDateBetweenAndPayerCode(from, until,paymentCode);
    }

    @Override
    public boolean contains(PaymentList paymentList) {
        return paymentListRepo.existsById(paymentList.getId());
    }

    @Override
    public Resource getFileAsResource(String filename) throws FileNotFoundException {
        try {
            Path file = Paths.get(backupDir, filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename);
        }
    }

    @Override
    public Resource getFilesArchiveAsResource(Date dateFrom, Date dateUntil, Integer paymentCode) {
        List<PaymentList> paymentLists = paymentListRepo.findAllByDateBetweenAndPayerCode(dateFrom, dateUntil,paymentCode);
        List<String> fileNames = paymentLists
                .stream()
                .map(PaymentList::getBackupFilePath)
                .collect(Collectors.toList());
        try {
            Path file = getFilesArchive(fileNames);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Path getFilesArchive(List<String> files) throws IOException {
        Path zipFilePath = Files.createTempFile("zip_archive", ".zip");

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            files.forEach(filename -> {
                ZipEntry zipEntry = new ZipEntry(filename);
                try {
                    zipOutputStream.putNextEntry(zipEntry);
                    Path path = Paths.get(backupDir, filename);
                    zipOutputStream.write(Files.readAllBytes(path));
                    zipOutputStream.closeEntry();
                } catch (Exception e) {
                    System.err.println(e);
                }
            });

            return zipFilePath;
        }
    }

    private void loadBackupFile(@NotNull PaymentList paymentList) {
        File file = new File(backupDir + File.separator + paymentList.getBackupFilePath());
        if (!file.exists()) {

            throw new RuntimeException("Ошибка загрузки файла для перечня " + paymentList.toString());
        }
        paymentList.setBackupFile(file);
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

    private void saveBackupFile(@NotNull PaymentList paymentList) {
        String fileExtension = paymentList.getBackupFile().getName();
        fileExtension = fileExtension.substring(fileExtension.lastIndexOf("."));
        String fileName = paymentList.getPayerCode() + "_" + paymentList.getNumber() + fileExtension;
        File backupFile = new File(backupDir + File.separator + fileName);

        try {
            File fileFromList = paymentList.getBackupFile();
//            System.out.println("backupdir exists: " + backupFile.getParentFile().exists() + " : " + backupFile.getParentFile().getAbsolutePath());
//            boolean fileCreated = backupFile.createNewFile();
//            System.out.println(fileCreated + ":"+ backupFile.toPath());
//            if (!fileFromList.exists())
//                Files.createFile(backupFile.toPath());
//            System.out.println(backupFile.exists() + ":" + backupFile.toPath());
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
    private void testBeforeSave(@NotNull PaymentList paymentList) {
        if (paymentList.getBackupFilePath() == null) {
            throw new NullPointerException("Ошибка backup файла для перечня " + paymentList);
        }
    }
}

