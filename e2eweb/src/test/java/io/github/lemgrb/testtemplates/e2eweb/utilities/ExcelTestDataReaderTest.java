package io.github.lemgrb.testtemplates.e2eweb.utilities;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for ExcelTestDataReader custom codes.
 */
@Slf4j
public class ExcelTestDataReaderTest {

  @Test
  public void testSingleInstance() throws IOException {
    ExcelTestDataReader one = ExcelTestDataReader.getExcelTestDataReader();
    ExcelTestDataReader two = ExcelTestDataReader.getExcelTestDataReader();
    Assert.assertEquals(one, two);
  }

  @Test
  public void testRetrieveTestDataGivenSheetRowAndColumnName() throws Exception {
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
    ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
  }

  @Test(expected = Exception.class)
  public void testWhenSheetnameAndRownamesAreEmptyString() throws Exception {
    TestData testData = new TestData.Builder()
            .build();
    ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
  }

}
