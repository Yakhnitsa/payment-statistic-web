package com.yurets_y.payment_statistic_web.service.railroad_documents_services;

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

    private Map<String,RailroadDocument> documents = new HashMap<>();

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
    public RailroadDocument getFromTempDb(RailroadDocument railroadDocument){
        return documents.get(railroadDocument.getXmlBackupFile().getName());
    }

    @Override
    public RailroadDocument deleteFromTempDB(RailroadDocument railroadDocument) {
        return documents.remove(railroadDocument.getXmlBackupFile().getName());

    }

    @Override
    public RailroadDocument fixCorruptedDocumentInTempDb(RailroadDocument railroadDocument){
        RailroadDocument document = deleteFromTempDB(railroadDocument);
        document.setDocNumber(railroadDocument.getDocNumber());
        document.setDateStamp(railroadDocument.getDateStamp());
        document.setPdfBackupFile(findPdfFileForDoc(document));

        return documents.put(railroadDocument.getXmlBackupFile().getName(),railroadDocument);
    }

    @Override
    public Collection<RailroadDocument> getAllFromTempDB() {
        return new LinkedList<RailroadDocument>(documents.values());
    }

    private RailroadDocument putPdfToTempDb(File file){
        for(RailroadDocument document : documents.values()){
            if(isDocCorrect(document) && file.getName().contains(document.getDocNumber().toString())){
                document.setPdfBackupFile(file);
                return document;
            }
        }
        pdfFiles.add(file);
        return null;
    }

    private RailroadDocument putXmlToTempDb(File file){
        RailroadDocument document;
        try {
            document = documentsParser.parseFromFile(file);
            document.setXmlBackupFile(file);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return null;
        }

        File pdfFile = findPdfFileForDoc(document);
        document.setPdfBackupFile(pdfFile);

        documents.put(document.getXmlBackupFile().getName(),document);
        return document;
    }

    private File findPdfFileForDoc(RailroadDocument document){
        Iterator<File> it = pdfFiles.iterator();
        while (it.hasNext()) {
            File pdfFile = it.next();
            if(isDocCorrect(document) && pdfFile.getName().contains(String.valueOf(document.getDocNumber()))){
               it.remove();
               return pdfFile;
            }
        }
        return null;
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

    private boolean isDocCorrect(RailroadDocument railroadDocument){
        return (railroadDocument.getDocNumber() != -1) && (railroadDocument.getDateStamp() != null);
    }

}
