package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TempListServiceImpl implements TempListService {



    private Path tempDir;

    private DocParser htmlDocParser;

    private List<PaymentList> paymentLists = new ArrayList<>();

    public TempListServiceImpl(){
        try {
            tempDir = Files.createTempDirectory("doc-parser-web");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания временного хранилища");
        }
    }


    @Override
    public PaymentList putToTempDB(MultipartFile multipartFile) {
        String filePath = tempDir.toAbsolutePath().toString() + File.separator + multipartFile.getOriginalFilename();
        try {
            File file = Files.createFile(Paths.get(filePath)).toFile();
            multipartFile.transferTo(file);

            PaymentList list = htmlDocParser.parseFromFile(file);
            paymentLists.add(list);

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PaymentList saveToMainDB(PaymentList paymentList) {
        return null;
    }

    @Override
    public List<PaymentList> getAllFromTempDB() {
        return Collections.unmodifiableList(paymentLists);
    }


    @Autowired
    public void setHtmlDocParser(DocParser htmlDocParser) {
        this.htmlDocParser = htmlDocParser;
    }
}
