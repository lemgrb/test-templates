package io.github.lemgrb.testtemplates.e2eweb.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Read Excel file once then save to a Map (of Maps of Maps of Strings).
 */
@Slf4j
public class ExcelTestDataReader {

  private static ExcelTestDataReader single_instance = null;
  private final Map<String, Map<String, Map<String, String>>> testDataMap = new HashMap<>();

  /**
   * Get instance of ExcelTestDataReader.
   *
   * @return ExcelTestDataReader single instance.
   * @throws IOException Error reading Excel file.
   */
  public static ExcelTestDataReader getExcelTestDataReader() throws IOException {
    if (single_instance == null) {
      single_instance = new ExcelTestDataReader();
    }
    return single_instance;
  }

  private ExcelTestDataReader() throws IOException {
    ProjectProperties properties = new ProjectProperties();

    log.info("▒▒▒ READING EXCEL FILE: " + properties.getProperties()
            .getProperty("EXCEL_TEST_DATA"));

    FileInputStream inputStream =
            new FileInputStream(properties.getProperties().getProperty("EXCEL_TEST_DATA"));

    Workbook workbook = new XSSFWorkbook(inputStream);

    Iterator<Sheet> sheetIterator = workbook.sheetIterator();

    while (sheetIterator.hasNext()) {
      Sheet sheet = sheetIterator.next();
      log.info("▒▒▒ SHEET: " + sheet.getSheetName());

      // For every sheet, get the column header from first row
      Row headersRow = sheet.getRow(0);
      List<String> columnHeaders = new ArrayList<>();
      Iterator<Cell> cellIterator = headersRow.cellIterator();
      while (cellIterator.hasNext()) {
        columnHeaders.add(cellIterator.next().getStringCellValue());
      }
      log.info("▒▒▒ COLUMN HEADERS: " + columnHeaders);

      // For every sheet, get the row and column values per row
      Iterator<Row> rowIterator = sheet.iterator();
      Map<String, Map<String, String>> rowData = new HashMap<>();
      while (rowIterator.hasNext()) {
        Row currentRow = rowIterator.next();
        Iterator<Cell> cellIterator2 = currentRow.cellIterator();
        Map<String, String> colData = new HashMap<>();
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

  /**
   * Get the actual value from Excel file as String.
   *
   * @param testData Instance of TestData containing information about sheet name, row name,
   *                 and column name.
   * @return String value of corresponding cell.
   * @throws Exception Test data not found.
   */
  public String getTestData(TestData testData) throws Exception {

    try {
      return testDataMap
              .get(testData.getSheetName())
              .get(testData.getRowName())
              .get(testData.getColName());
    } catch (NullPointerException e) {
      throw new Exception("Test data not found! Check sheet name, row name, and column names");
    }
  }


}
