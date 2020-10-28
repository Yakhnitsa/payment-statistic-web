package com.yurets_y.payment_statistic_web.service.railroad_documents_services;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import com.yurets_y.payment_statistic_web.entity.Station;
import com.yurets_y.payment_statistic_web.repo.RailroadDocumentsRepo;
import com.yurets_y.payment_statistic_web.service.StationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class RailroadDocumentsServiceImpl implements RailroadDocumentsService{

    private StationServiceImpl stationService;

    private RailroadDocumentsRepo documentsRepo;

    @Autowired
    public RailroadDocumentsServiceImpl(StationServiceImpl stationService, RailroadDocumentsRepo documentsRepo) {
        this.stationService = stationService;
        this.documentsRepo = documentsRepo;
    }

//    @Value("#{${service.backup-path} + systemProperties.line.separator + 'rail_docs'}")
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
        return null;
    }

    @Override
    public Resource getFilesArchiveAsResource(List<RailroadDocument> documents) {
        return null;
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

    private boolean isValid(RailroadDocument railroadDocument){
        return railroadDocument.getDocNumber() != -1 && railroadDocument.getDateStamp() != null;
    }
}
