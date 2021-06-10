package io.github.lemgrb.testtemplates.e2eweb.utilities;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class ExcelTestDataReaderTest {

  @Test
  public void testInstantiation() throws IOException {
    ExcelTestDataReader dataReader = ExcelTestDataReader.getExcelTestDataReader();
  }

  @Test
  public void testRetrieveTestDataGivenSheetRowAndColumnName() throws Exception {
    ExcelTestDataReader dataReader = ExcelTestDataReader.getExcelTestDataReader();
    TestData testData = new TestData.Builder()
            .sheet("Login")
            .row("TC001 Valid username and password")
            .column("Username")
            .build();
    String data = ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
    Assert.assertEquals("test@mailinator.com", data);
  }

  @Test
  public void testRetrieveAnotherTestDataGivenSheetRowAndColumnName() throws Exception {
    TestData testData = new TestData.Builder()
            .sheet("#SITECONFIG")
            .row("BACKEND_URL")
            .column("VALUE")
            .build();
    String data = ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
    Assert.assertEquals("http://localhost:1337/admin", data);
  }

  @Test
  public void testRetrieveTestDataOnCellWithReference() throws Exception {
    TestData testData = new TestData.Builder()
            .sheet("Login")
            .row("TC001 Valid username and password")
            .column("Site")
            .build();
    String data = ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
    Assert.assertEquals("http://localhost:1337/admin", data);
  }

  @Test(expected = Exception.class)
  public void testWhenRownameIsEmptyString() throws Exception {
    TestData testData = new TestData.Builder()
            .sheet("Healthcheck")
            .row("")
            .column("Site")
            .build();
    String data = ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
  }

  @Test(expected = Exception.class)
  public void testWhenSheetnameAndRownamesAreEmptyString() throws Exception {
    TestData testData = new TestData.Builder()
            .build();
    String data = ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
  }

}
