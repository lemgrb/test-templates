package io.github.lemgrb.testtemplates.e2eweb.utilities;

import lombok.Getter;
import lombok.Setter;

public class TestData {

  @Getter
  private final String sheetName;
  @Getter
  private final String rowName;
  @Getter
  private final String colName;

  public static class Builder {

    private String sheetName; //This is important, so we'll pass it to the constructor.
    private String rowName;
    private String colName;

    public Builder() {
    }

    public Builder sheet(String sheetName) {
      this.sheetName = sheetName;
      return this;
    }

    public Builder row(String rowName) {
      this.rowName = rowName;

      return this;
    }

    public Builder column(String colName) {
      this.colName = colName;
      return this;
    }

    public TestData build() throws Exception {
      TestData testData = new TestData(this);
      validateTestData(testData);
      return testData;
    }

    private void validateTestData(TestData testData) throws Exception {
      if (sheetName == null ||
              rowName == null ||
              colName == null ||
              sheetName.isEmpty() ||
              rowName.isEmpty() ||
              colName.isEmpty())
        throw new Exception("Please provide sheetName, rowName, and colName by calling sheet(), row(), and column() methods respectively.");
    }
  }

  private TestData(Builder builder) {
    this.sheetName = builder.sheetName;
    this.rowName = builder.rowName;
    this.colName = builder.colName;
  }

}