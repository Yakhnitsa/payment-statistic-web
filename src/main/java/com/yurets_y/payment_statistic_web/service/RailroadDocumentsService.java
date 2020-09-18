package com.yurets_y.payment_statistic_web.service;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import com.yurets_y.payment_statistic_web.entity.RailroadDocumentId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.util.List;

public interface RailroadDocumentsService {

    void add(RailroadDocument paymentList);

    void remove(RailroadDocument paymentList);

    RailroadDocument getById(RailroadDocumentId id);

    Page<RailroadDocument> getAll(Pageable pageable);


    boolean contains(RailroadDocument paymentList);

    Resource getFileAsResource(String filename) throws FileNotFoundException;

    Resource getFilesArchiveAsResource(List<RailroadDocument> documents);
}
