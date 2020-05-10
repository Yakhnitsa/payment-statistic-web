package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class TempListServiceImpl implements TempListService{

    private Path tempDir;

    private DocParser htmlDocParser;

    private Map<PaymentListId,PaymentList> tempDBMap = new LinkedHashMap<>();

    public TempListServiceImpl(){
        try {
            tempDir = Files.createTempDirectory("doc-parser-web");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания временного хранилища");
        }
    }

    @Autowired
    public TempListServiceImpl(DocParser htmlDocParser) {
        this();
        this.htmlDocParser = htmlDocParser;
    }

    @Override
    public PaymentList putToTempDB(MultipartFile multipartFile) {
        String filePath = tempDir.toAbsolutePath().toString() + File.separator + multipartFile.getOriginalFilename();
        try {
            File file = Paths.get(filePath).toFile();
            Files.deleteIfExists(file.toPath());

            file = Files.createFile(Paths.get(filePath)).toFile();
            multipartFile.transferTo(file);

            PaymentList list = htmlDocParser.parseFromFile(file);
            tempDBMap.put(list.getId(),list);

            return list;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении во временную ДБ перечней" + filePath);
        }
    }


    @Override
    public PaymentList deleteFromTempDB(PaymentList paymentList) {
        PaymentList listFromTemp = tempDBMap.remove(getIdFromList(paymentList));
        return listFromTemp;
    }

    @Override
    public Collection<PaymentList> getAllFromTempDB() {
        return Collections.unmodifiableCollection(tempDBMap.values());
    }

    private PaymentListId getIdFromList(PaymentList paymentList){
        int code = paymentList.getPayerCode();
        int number = paymentList.getNumber();
        if((code!= 0) && (number)!= 0)
            return new PaymentListId(code,number);
        else{
            throw new RuntimeException("Неверный ID " + paymentList.toString());
        }
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Удаление временного хранилища файлов");
        System.out.println(tempDir.toAbsolutePath());
        try {
            Files.deleteIfExists(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
