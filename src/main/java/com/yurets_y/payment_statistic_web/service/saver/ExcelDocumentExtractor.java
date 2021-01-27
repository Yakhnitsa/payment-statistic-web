package com.yurets_y.payment_statistic_web.service.saver;

import com.yurets_y.payment_statistic_web.entity.RailroadDocument;

import java.io.File;
import java.util.List;

public interface ExcelDocumentExtractor {

    File getExcelFileFromDoc(RailroadDocument document);

    File getExcelFileFromDocList(List<RailroadDocument> documents);
}
