package com.yurets_y.payment_statistic_web.service.railroad_documents_services;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;


public interface TempRailroadDocsService {

    RailroadDocument putToTempDB(MultipartFile file);

    RailroadDocument getFromTempDb(RailroadDocument railroadDocument);

    RailroadDocument deleteFromTempDB(RailroadDocument paymentList);

    RailroadDocument fixCorruptedDocumentInTempDb(RailroadDocument railroadDocument);

    Collection<RailroadDocument> getAllFromTempDB();

}
