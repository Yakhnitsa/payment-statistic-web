package com.yurets_y.payment_statistic_web.service.railroad_documents_services;


import com.yurets_y.payment_statistic_web.entity.RailroadDocument;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Service
public class ExcelDocumentWriterService implements ExcelDocumentWriter {

    @Override
    public void writeSingleDocToStream(OutputStream outputStream, RailroadDocument document, List<RailDocFields> fieldsList) throws IOException {
        XSSFWorkbook workbook = getWorkbookFromRailDoc(document,fieldsList);
        workbook.write(outputStream);
    }

    @Override
    public void writeDocListToStream(OutputStream outputStream, List<RailroadDocument> documents, List<RailDocFields> fieldsList) throws IOException {
        XSSFWorkbook workbook = getWorkbookFromRailDocList(documents,fieldsList);
        workbook.write(outputStream);
    }

    private XSSFWorkbook getWorkbookFromRailDoc(RailroadDocument railroadDocument, List<RailDocFields> fieldsList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");

        Row mainRow = sheet.createRow(0);
        // Write headers
        for (int i = 0; i < fieldsList.size(); i++) {
            mainRow.createCell(i).setCellValue(fieldsList.get(i).getTitle());
        }

        for (int vagIndex = 0; vagIndex < railroadDocument.getVagonList().size(); vagIndex++) {
            Row currentRow = sheet.createRow(vagIndex + 1);
            writeRow(railroadDocument,vagIndex,fieldsList,currentRow);
        }

        return workbook;
    }


    private XSSFWorkbook getWorkbookFromRailDocList(List<RailroadDocument> documents, List<RailDocFields> fieldsList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        int rowIndex = 0;

        Row mainRow = sheet.createRow(rowIndex++);
        // Write headers
        for (int i = 0; i < fieldsList.size(); i++) {
            mainRow.createCell(i).setCellValue(fieldsList.get(i).getTitle());
        }
        for(RailroadDocument document: documents){
            for(int vagIndex = 0; vagIndex < document.getVagonList().size(); vagIndex ++ ){
                Row currentRow = sheet.createRow(rowIndex++);
                writeRow(document,vagIndex,fieldsList,currentRow);
            }
        }

        return workbook;
    }

    //TODO Определить тип и уснанавливать свойства необходимого типа
    private void writeRow(RailroadDocument document, int vagonIndex, List<RailDocFields> fieldsList, Row currentRow){
        for(int i = 0; i < fieldsList.size(); i++){
            Cell cell = currentRow.createCell(i);
            Object value = fieldsList.get(i).getField(document,vagonIndex);
            if(value instanceof Double){
                cell.setCellValue((Double) value);
            }else if(value instanceof Integer){
                cell.setCellValue((Integer) value);
            }else if(value instanceof Date){
                cell.setCellValue((Date)value);
            }
            else{
                cell.setCellValue(value.toString());
            }

        }
    }

}
