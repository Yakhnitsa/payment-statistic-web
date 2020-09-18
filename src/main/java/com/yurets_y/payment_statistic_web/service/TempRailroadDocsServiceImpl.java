package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.service.parser_services.RailroadDocumentsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.ParseException;
import java.util.*;

@Service
public class TempRailroadDocsServiceImpl implements TempRailroadDocsService{

    private static Logger logger = LoggerFactory.getLogger(TempRailroadDocsServiceImpl.class);

    private Map<Integer,RailroadDocument> documentMap = new HashMap<>();

    private Set<File> pdfFiles = new HashSet<>();

    private RailroadDocumentsParser documentsParser;

    private Path tempDir;

    @Autowired
    public TempRailroadDocsServiceImpl(RailroadDocumentsParser documentsParser) {
        this.documentsParser = documentsParser;
    }

    @Override
    public RailroadDocument putToTempDB(MultipartFile multipartFile) {

        File file = putMultipartToTempDir(multipartFile);

        if(file.getName().endsWith(".pdf")){
            return putPdfToTempDb(file);
        } else if(file.getName().endsWith(".xml")){
            return putXmlToTempDb(file);
        }
        return null;
    }

    @Override
    public RailroadDocument deleteFromTempDB(RailroadDocument railroadDocument) {
        return documentMap.remove(railroadDocument.getDocNumber());
    }
    public RailroadDocument fixCorruptedDocumentInTempDb(RailroadDocument railroadDocument){
        return railroadDocument;
    }

    @Override
    public Collection<RailroadDocument> getAllFromTempDB() {
        return documentMap.values();
    }

    private RailroadDocument putPdfToTempDb(File file){
        for(Map.Entry<Integer,RailroadDocument> docEntity : documentMap.entrySet()){
            if(file.getName().contains(docEntity.getKey().toString())){
                docEntity.getValue().setPdfBackupFile(file);
                return docEntity.getValue();
            }
        }
        pdfFiles.add(file);
        return null;
    }

    private RailroadDocument putXmlToTempDb(File file){
        RailroadDocument document = null;
        try {
            document = documentsParser.parseFromFile(file);
            document.setXmlBackupFile(file);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return null;
        }

        int docNumb = document.getDocNumber();
        Iterator<File> it = pdfFiles.iterator();

        while (it.hasNext()) {
            File pdfFile = it.next();
            if(pdfFile.getName().contains(String.valueOf(docNumb)) && docNumb != -1){
                document.setPdfBackupFile(pdfFile);
                it.remove();
                break;
            }

        }
        documentMap.put(docNumb,document);
        return document;
    }

    @PostConstruct
    public void initTempDirectory() {
        try {
            tempDir = Files.createTempDirectory("doc-parser-web-raildocs");
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

    private File putMultipartToTempDir(MultipartFile multipartFile){
        String filePath = tempDir.toAbsolutePath().toString() + File.separator + multipartFile.getOriginalFilename();
        try {
            File file = Paths.get(filePath).toFile();
            Files.deleteIfExists(file.toPath());
            file = Files.createFile(Paths.get(filePath)).toFile();
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении во временную ДБ файла ЖД накладной " + filePath);
        }
    }

}
