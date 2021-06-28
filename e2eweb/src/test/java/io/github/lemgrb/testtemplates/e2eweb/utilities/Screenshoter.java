package io.github.lemgrb.testtemplates.e2eweb.utilities;

import static org.apache.commons.io.FilenameUtils.normalize;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Utility class to take screenshots.
 */
@Slf4j
public class Screenshoter {

  private ProjectProperties properties;
  private String screenshotFolder;
  private static Screenshoter single_instance;

  /**
   * Get the single instance of Screenshoter class.
   *
   * @param properties ProjectProperties.
   * @return instance of Screenshoter class.
   */
  public static Screenshoter getScreenshoterInstance(ProjectProperties properties) {
    if (single_instance == null) {
      single_instance = new Screenshoter(properties);
    }
    return single_instance;
  }

  private Screenshoter(ProjectProperties properties) {
    this.properties = properties;
    log.info("▒▒▒ SCREENSHOT_FOLDER: " + properties.getProperties()
            .getProperty("SCREENSHOT_FOLDER"));
  }

  /**
   * Take screenshot.
   *
   * @param driver WebDriver instance.
   * @param feature Feature name coming from .feature file
   * @param scenario Scenario name coming from .feature file
   */
  public void takeScreenshot(WebDriver driver, String feature, String scenario) {
    File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = format.format(new Date());
    String screenshotFolder = normalize(properties.getProperties()
            .getProperty("SCREENSHOT_FOLDER")).toUpperCase();
    String fileName = screenshotFolder + "_" + date + "/"
            + feature + "/"
            + scenario + ".png";
    log.info("▒▒▒ Filename: " + fileName);
    try {
      FileUtils.copyFile(scrFile, new File(fileName));
    } catch (IOException e) {
      log.error("▒▒▒  Error in taking screenshot. Message: " + e.getMessage());
    }
  }
}
