package io.github.lemgrb.testtemplates.e2eweb.utilities;

import lombok.Getter;

/**
 * The class will hold the information about the sheet name, row name, and column name
 * of the test data to be retrieved via ExcelTestDataReader.
 *
 * @see io.github.lemgrb.testtemplates.e2eweb.utilities.ExcelTestDataReader
 */
@SuppressWarnings("checkstyle:RequireEmptyLineBeforeBlockTagGroup")
public class TestData {

  @Getter
  private final String sheetName;
  @Getter
  private final String rowName;
  @Getter
  private final String colName;

  /**
   *  Use this Builder class to create a TestData object representing the sheet, row, and column
   *  from TestData.xlsx.
   */
  public static class Builder {

    private String sheetName;
    private String rowName;
    private String colName;

    public Builder() {
    }

    /**
     * Should be called after .Builder().
     *
     * @param sheetName Name of the sheet from "TestData.xlsx"
     * @return Builder
     */
    public Builder sheet(String sheetName) {
      this.sheetName = sheetName;
      return this;
    }

    /**
     * Should be called after sheet().
     *
     * @param rowName Refers to the first column in TestData.xlsx.
     * @return Builder
     */
    public Builder row(String rowName) {
      this.rowName = rowName;

      return this;
    }

    /**
     * Should be called after .sheet().row().
     *
     * @param colName refers to the column headers from TestData.xlsx
     * @return Builder
     */
    public Builder column(String colName) {
      this.colName = colName;
      return this;
    }

    /**
     * Should be called after .sheet().row().col().
     *
     * @return TestData object representing the sheet, row, and column from TestData.xlsx.
     * @throws Exception When sheet, row, and column names are empty or null.
     */
    public TestData build() throws Exception {
      TestData testData = new TestData(this);
      validateTestData();
      return testData;
    }

    private void validateTestData() throws Exception {
      if (sheetName == null
              || rowName == null
              || colName == null
              || sheetName.isEmpty()
              || rowName.isEmpty()
              || colName.isEmpty()) {
        throw new Exception("Please provide sheetName, rowName, and colName by calling sheet()"
                + ", row(), and column() methods respectively.");

      }
    }
  }

  private TestData(Builder builder) {
    this.sheetName = builder.sheetName;
    this.rowName = builder.rowName;
    this.colName = builder.colName;
  }

}