package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.PaymentList;
import com.yurets_y.payment_statistic_web.entity.PaymentListId;
import com.yurets_y.payment_statistic_web.service.parser_services.DocParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service("temp-list-service-inmemory-map")
public class TempListServiceImpl implements TempListService {

    private static Logger logger = LoggerFactory.getLogger(TempListServiceImpl.class);

    private Path tempDir;

    private DocParser htmlDocParser;

    private DocParser xmlDocParser;

    private Map<PaymentListId, PaymentList> tempDBMap;


    @Autowired
    public TempListServiceImpl(@Qualifier("html-doc-parser") DocParser htmlDocParser,
                               @Qualifier("xml-doc-parser") DocParser xmlDocParser) {
        tempDBMap = new LinkedHashMap<>();
        this.htmlDocParser = htmlDocParser;
        this.xmlDocParser = xmlDocParser;
    }

    @Override
    public PaymentList putToTempDB(MultipartFile multipartFile) {
        String filePath = tempDir.toAbsolutePath().toString() + File.separator + multipartFile.getOriginalFilename();
        try {
            File file = Paths.get(filePath).toFile();
            Files.deleteIfExists(file.toPath());

            file = Files.createFile(Paths.get(filePath)).toFile();
            multipartFile.transferTo(file);

            PaymentList list = parseFromFile(file);
            if(list.getNumber() > 0){
                tempDBMap.put(list.getId(), list);
            }



            return list;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении во временную ДБ файла перечня: " + filePath);
        }
    }

    private PaymentList parseFromFile(File file) throws IOException {

        if (file.getName().toLowerCase().endsWith(".html")) {
            return htmlDocParser.parseFromFile(file);
        } else if (file.getName().toLowerCase().endsWith(".xml")) {
            return xmlDocParser.parseFromFile(file);
        } else {
            throw new RuntimeException("Неверный формат файла " + file);
        }
    }


    @Override
    public PaymentList deleteFromTempDB(PaymentList paymentList) {
        PaymentList listFromTemp = tempDBMap.remove(paymentList.getId());
        return listFromTemp;
    }

    @Override
    public Collection<PaymentList> getAllFromTempDB() {
        return Collections.unmodifiableCollection(tempDBMap.values());
    }

    @PostConstruct
    public void initTempDirectory() {
        try {
            tempDir = Files.createTempDirectory("doc-parser-web");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания временного хранилища");
        }
        logger.info("Временное храниличе успешно создано:" + tempDir.toAbsolutePath());

    }

    @PreDestroy
    public void destroy() {
        logger.info("Удаление временного хранилища файлов: " + tempDir.toAbsolutePath());
        try {
            deleteDirectoryRecursion(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteDirectoryRecursion(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursion(entry);
                }
            }
        }
        Files.delete(path);
    }
}
