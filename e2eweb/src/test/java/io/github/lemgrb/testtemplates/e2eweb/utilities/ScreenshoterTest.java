package io.github.lemgrb.testtemplates.e2eweb.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Unit Test class for Screenshoter.java
 */
@Slf4j
public class ScreenshoterTest {

  @Ignore
  @Test
  public void takeScreenshotAllValid() throws IOException {
    // Arrange
    ProjectProperties properties = new ProjectProperties();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    String feature = "Feature one";
    String scenario = "Scenario one";
    String date = format.format(new Date());
    WebDriver driver = new ChromeDriver();
    Screenshoter screenshot = Screenshoter.getScreenshoterInstance(properties);
    final String fileName = "SCREENSHOTS_" + date + "/Feature one/Scenario one.png";

    // Act
    driver.get("https://www.google.com");
    screenshot.takeScreenshot(driver, feature, scenario);

    // Assert
    driver.quit();
    Assert.assertTrue(new File(fileName).exists());
  }

  @Ignore
  @Test
  public void featureNameGreaterThan255Characters() throws IOException {
    // Arrange
    ProjectProperties properties = new ProjectProperties();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    String feature = "FEATURE This is an example 256 character feature name. Lorem ipsum dolor "
            + " sit amet, consectetur adipiscing elit. Quisque non consectetur justo. Suspendisse"
            + " eleifend, ex eget convallis pharetra, nulla sapien fermentum orci, tempus "
            + "fermentum nisi eros sit amet diam. Quisque faucibus sem at congue finibus. Fusce "
            + "sed lacinia lacus, et suscipit mauris. Donec nec lacinia elit, in interdum mi.";
    String scenario = "Scenario one";
    String date = format.format(new Date());
    WebDriver driver = new ChromeDriver();
    Screenshoter screenshot = Screenshoter.getScreenshoterInstance(properties);

    // Act
    driver.get("https://www.google.com");
    screenshot.takeScreenshot(driver, feature, scenario);

    // Assert = No exception will be thrown. The IOException filename too long is catched
    // and just logged in console.
    // TODO: [Optional] test if logged.  
    driver.quit();
  }

  @Ignore
  @Test
  public void scenarioNameGreaterThan255Characters() throws Exception {
  }

  @Ignore
  @Test
  public void feautureNameContainsCharactersThatAreNotValidFilename() throws Exception {
  }

  @Ignore
  @Test
  public void scenarioNameContainsCharactersThatAreNotValidFilename() throws Exception {
  }

  @Ignore
  @Test
  public void screenShotFolderPermissionError() {
  }

}
