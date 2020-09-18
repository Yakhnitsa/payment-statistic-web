package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import com.yurets_y.payment_statistic_web.entity.Station;
import com.yurets_y.payment_statistic_web.repo.RailroadDocumentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class RailroadDocumentsServiceImpl implements RailroadDocumentsService{

    private StationServiceImpl stationService;

    private RailroadDocumentsRepo documentsRepo;

    @Autowired
    public RailroadDocumentsServiceImpl(StationServiceImpl stationService, RailroadDocumentsRepo documentsRepo) {
        this.stationService = stationService;
        this.documentsRepo = documentsRepo;
    }

    @Value("#{${service.backup-path} + systemProperties.line.separator + 'rail_docs'}")
    private String backupDir;

    @Override
    public void add(RailroadDocument rDoc) {
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
        rDoc.setPdfBackupFilePath(xmlBackup);

        documentsRepo.save(rDoc);
    }

    @Override
    public void remove(RailroadDocument rDoc) {

        removeBackupFile(rDoc.getXmlBackupFile());
        removeBackupFile(rDoc.getPdfBackupFile());
        documentsRepo.delete(rDoc);
    }

    @Override
    public RailroadDocument getById(RailroadDocumentId id) {
        return null;
    }

    @Override
    public Page<RailroadDocument> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean contains(RailroadDocument rDoc) {
        return false;
    }

    @Override
    public Resource getFileAsResource(String filename) throws FileNotFoundException {
        return null;
    }

    @Override
    public Resource getFilesArchiveAsResource(List<RailroadDocument> documents) {
        return null;
    }

    private String saveBackupFile(RailroadDocument document, File file){
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

    private File savePdfBackupFile(RailroadDocument railroadDocument){
        return null;
    }

    private void removeBackupFile(File file){

    }
}
