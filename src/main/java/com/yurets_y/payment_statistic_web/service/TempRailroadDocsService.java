package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;


public interface TempRailroadDocsService {

    RailroadDocument putToTempDB(MultipartFile file);

    RailroadDocument deleteFromTempDB(RailroadDocument paymentList);

    Collection<RailroadDocument> getAllFromTempDB();

}
