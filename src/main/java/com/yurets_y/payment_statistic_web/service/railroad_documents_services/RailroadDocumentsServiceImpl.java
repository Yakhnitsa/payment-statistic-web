package com.yurets_y.payment_statistic_web.service.railroad_documents_services;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import com.yurets_y.payment_statistic_web.entity.Station;
import com.yurets_y.payment_statistic_web.repo.RailroadDocumentsRepo;
import com.yurets_y.payment_statistic_web.service.StationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class RailroadDocumentsServiceImpl implements RailroadDocumentsService{

    private StationServiceImpl stationService;

    private RailroadDocumentsRepo documentsRepo;

    @Autowired
    public RailroadDocumentsServiceImpl(StationServiceImpl stationService, RailroadDocumentsRepo documentsRepo) {
        this.stationService = stationService;
        this.documentsRepo = documentsRepo;
    }

    @Value("${service.backup-path}" + "/rail_docs")
    private String backupDir;

    @Override
    public void add(RailroadDocument rDoc) {

        if(!isValid(rDoc)) throw new RuntimeException("Попытка сохранить документ неверного формата " + rDoc);

        Station sendStation = stationService.getByStationCode(rDoc.getSendStation().getCode());
        if(sendStation == null){
            stationService.addStation(rDoc.getSendStation());
        }
        Station receiveStation = stationService.getByStationCode(rDoc.getReceiveStation().getCode());
        if(receiveStation == null){
            stationService.addStation(rDoc.getReceiveStation());
        }

        String pdfBackup = saveBackupFile(rDoc,rDoc.getPdfBackupFile());
        rDoc.setPdfBackupFilePath(pdfBackup);

        String xmlBackup = saveBackupFile(rDoc,rDoc.getXmlBackupFile());
        rDoc.setXmlBackupFilePath(xmlBackup);

        documentsRepo.save(rDoc);
    }

    @Override
    public void remove(RailroadDocument rDoc) {
        removeBackupFiles(rDoc);
        documentsRepo.delete(rDoc);
    }

    @Override
    public RailroadDocument getById(RailroadDocumentId id) {
        return documentsRepo.getOne(id);
    }

    @Override
    public Page<RailroadDocument> getAll(Pageable pageable) {
        return documentsRepo.findAll(pageable);
    }

    @Override
    public Page<RailroadDocument> getAllBySpecification(Specification<RailroadDocument> spec, Pageable pageable) {
        return documentsRepo.findAll( spec , pageable);
    }

    @Override
    public boolean contains(RailroadDocument rDoc) {
        return documentsRepo.existsById(new RailroadDocumentId(rDoc.getDocNumber(),rDoc.getDateStamp()));
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
    public Resource getFilesArchiveAsResource(List<RailroadDocumentId> documentIds) throws IOException {
        List<RailroadDocument> documents = documentsRepo.findAllById(documentIds);
        Map<String,Path> documentMap = getFilesMap(documents);
        Path filesArch = getFilesArchive(documentMap);
        return new UrlResource(filesArch.toUri());
    }

    private String saveBackupFile(RailroadDocument document, File file){
        if(file == null) return null;
        String fileExtension = file.getName();
        fileExtension = fileExtension.substring(fileExtension.lastIndexOf("."));
        String fileName = String.format(
                "%1$tY_%1$tm_%1$td__%2$s%3$s",
                document.getDateStamp(),
                document.getDocNumber(),
                fileExtension);

        File backupFile = new File(backupDir + File.separator + fileName);
        try {
            Files.copy(file.toPath(),
                    backupFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при сохранении файла " + fileName);
        }
        return fileName;
    }

    private void removeBackupFiles(RailroadDocument rDoc) {
        File xmlBackup = new File(backupDir + File.separator + rDoc.getXmlBackupFilePath());
        File pdfBackup = new File(backupDir + File.separator + rDoc.getPdfBackupFilePath());
        if(xmlBackup.exists()){
            try {
                Files.deleteIfExists(xmlBackup.toPath());
            } catch (IOException e) {
                throw new RuntimeException("Ошибка удаления файла " + xmlBackup);
            }
        }

        if(pdfBackup.exists()){
            try {
                Files.deleteIfExists(pdfBackup.toPath());
            } catch (IOException e) {
                throw new RuntimeException("Ошибка удаления файла " + xmlBackup);
            }
        }
    }

    private Path getFilesArchive(Map<String,Path> filesMap) throws IOException {
        Path zipFilePath = Files.createTempFile("zip_archive", ".zip");

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            filesMap.forEach((fileName,path) -> {
                ZipEntry zipEntry = new ZipEntry(fileName);
                try {
                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write(Files.readAllBytes(path));
                    zipOutputStream.closeEntry();
                } catch (Exception e) {
                    System.err.println(e);
                }
            });

            return zipFilePath;
        }
    }

    private Map<String,Path> getFilesMap(List<RailroadDocument> documents){
        Map<String,Path> resultMap = new HashMap<>();
        documents.forEach(document ->{
            String fileName = getFileName(document);
            if(document.getPdfBackupFilePath() != null){
                Path pdfFile = Paths.get(backupDir, document.getPdfBackupFilePath());
                if(Files.exists(pdfFile)) resultMap.put(fileName + ".pdf",pdfFile);
            }

            if(document.getXmlBackupFilePath() != null){
                Path xmlFile = Paths.get(backupDir,document.getXmlBackupFilePath());
                if(Files.exists(xmlFile)) resultMap.put(fileName + ".xml",xmlFile);
            }


        });
        return resultMap;
     }

    private String getFileName(RailroadDocument document){
        Date date = document.getDocDate();
        String station = document.getSendStation().getRusName();
        int vagCount = document.getVagonCount();
        int docNumber = document.getDocNumber();
        int senderCode = document.getCargoSender().getRailroadCode();
        int receiverCode = document.getCargoReceiver().getRailroadCode();
        return String.format("%1$td_%1$tm_%1$tY__(%2$04d)_%3$s_%4$d_ваг_ЖД_%5$d_(%6$04d)",
                date,senderCode, station, vagCount, docNumber, receiverCode);
    }

    private boolean isValid(RailroadDocument railroadDocument){
        return railroadDocument.getDocNumber() != -1 && railroadDocument.getDateStamp() != null;
    }
}
