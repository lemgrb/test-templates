package io.github.lemgrb.testtemplates.e2eweb.utilities;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


/**
 * TODO: Use BUILDER?
 * Read Excel file once then save to instance as Maps.
 */
@Slf4j
public class ExcelTestDataReader {

    private static ExcelTestDataReader single_instance = null;
    private ProjectProperties properties;
    private FileInputStream inputStream;
    private Workbook workbook;
    private Map<String, Map<String, Map<String, String>>> testDataMap = new HashMap<String, Map<String, Map<String, String>>>();

    public static ExcelTestDataReader getExcelTestDataReader() throws IOException {
        if (single_instance == null)
            single_instance = new ExcelTestDataReader();

        return single_instance;
    }

    private ExcelTestDataReader() throws IOException  {
        properties = new ProjectProperties();

        log.info("▒▒▒ READING EXCEL FILE: " + properties.getProperties().getProperty("EXCEL_TEST_DATA"));

        inputStream = new FileInputStream(properties.getProperties().getProperty("EXCEL_TEST_DATA"));

        workbook = new XSSFWorkbook(inputStream);

        Iterator<Sheet> sheetIterator = workbook.sheetIterator();

        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            log.info("▒▒▒ SHEET: " + sheet.getSheetName());

            // For every sheet, get the column header from first row
            Row headersRow = sheet.getRow(0);
            List<String> columnHeaders = new ArrayList<String>();
            Iterator<Cell> cellIterator = headersRow.cellIterator();
            while (cellIterator.hasNext()) {
                columnHeaders.add(cellIterator.next().getStringCellValue());
            }
            log.info("▒▒▒ COLUMN HEADERS: " + columnHeaders);

            // For every sheet, get the row and column values per row
            Iterator<Row> rowIterator = sheet.iterator();
            Map<String, Map<String, String>> rowData = new HashMap<String, Map<String, String>>();
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                Iterator<Cell> cellIterator2 = currentRow.cellIterator();
                Map<String, String> colData = new HashMap<String, String>();
                while (cellIterator2.hasNext()) {
                    Cell cell = cellIterator2.next();
                    colData.put(columnHeaders.get(cell.getColumnIndex()), cell.getStringCellValue());
                }
                rowData.put(currentRow.getCell(0).getStringCellValue(), colData);
            }

            testDataMap.put(sheet.getSheetName(), rowData);
        }


        inputStream.close();
        workbook.close();
    }

    // TODO: Ugly code
    public String getTestData(TestData testData) throws Exception {

        try {
            return testDataMap
                    .get(testData.getSheetName())
                    .get(testData.getRowName())
                    .get(testData.getColName()).toString();
        } catch (NullPointerException e) {
            throw new Exception("Test data not found! Check sheet name, row name, and column names");
        }
    }


}
