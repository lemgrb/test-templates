package io.github.lemgrb.testtemplates.e2eweb;

import static org.junit.Assert.assertTrue;

import com.saucelabs.saucebindings.Browser;
import com.saucelabs.saucebindings.DataCenter;
import com.saucelabs.saucebindings.SauceOptions;
import com.saucelabs.saucebindings.SaucePlatform;
import com.saucelabs.saucebindings.SauceSession;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.lemgrb.testtemplates.e2eweb.utilities.ExcelTestDataReader;
import io.github.lemgrb.testtemplates.e2eweb.utilities.ProjectProperties;
import io.github.lemgrb.testtemplates.e2eweb.utilities.Screenshoter;
import io.github.lemgrb.testtemplates.e2eweb.utilities.TestData;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.lemgrb.testtemplates.e2eweb.utilities.VideoRecorder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public class StepDefinitions {

  protected ExcelTestDataReader excelTestDataReader;
  protected ProjectProperties projectProperties;
  protected WebDriverWait wait;
  protected WebDriver driver;
  protected String currentScenario;
  protected String currentFeature;
  protected VideoRecorder videoRecorder;

  protected static ThreadLocal<SauceSession> session = new ThreadLocal<>();
  protected static ThreadLocal<SauceOptions> sauceOptions = new ThreadLocal<>();

  public SauceSession getSession() {
    return session.get();
  }

  public WebDriver getDriver() {
    if (projectProperties.getEnvironment().equalsIgnoreCase("local")
            || projectProperties.getEnvironment().equalsIgnoreCase("remote")) {
      return driver;
    }
    return getSession().getDriver();
  }

  // TODO: Refactor
  private String getFeatureName(String scenarioId) {
    Pattern pattern = Pattern.compile(".*/(.*).feature:[0-9]*");
    Matcher matcher = pattern.matcher(scenarioId);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return scenarioId;
  }

  @Before
  public void setup(Scenario scenario) throws Exception {
    this.currentScenario = scenario.getName();
    this.currentFeature = getFeatureName(scenario.getId());
    log.info("▒▒▒  Thread ID: " + Thread.currentThread().getId());
    log.info("▒▒▒  Current feature: " + getFeatureName(scenario.getId()));

    projectProperties = new ProjectProperties();
    excelTestDataReader = ExcelTestDataReader.getExcelTestDataReader();

    if (projectProperties.getEnvironment().equalsIgnoreCase("saucelabs")) {

      log.info("▒▒▒ SAUCE_USERNAME: " + ((System.getenv("SAUCE_USERNAME") != null
              && !System.getenv("SAUCE_USERNAME").isBlank())
              ? "SAUCE_USERNAME OK" : "SAUCE_USERNAME NOT FOUND!!!"));
      log.info("▒▒▒ SAUCE_ACCESS_KEY: " + ((System.getenv("SAUCE_ACCESS_KEY") != null
              && !System.getenv("SAUCE_ACCESS_KEY").isBlank())
              ? "SAUCE_ACCESS_KEY OK" : "SAUCE_ACCESS_KEY NOT FOUND!!!"));

      sauceOptions.set(new SauceOptions());
      sauceOptions.get().setName(scenario.getName());

      if (System.getenv("START_TIME") != null) {
        sauceOptions.get().setBuild("Build Time: " + System.getenv("START_TIME"));
      }

      String platform;
      if (System.getProperty("platform") != null) {
        platform = System.getProperty("platform");
      } else {
        platform = "default";
      }

      log.info("▒▒▒ PLATFORM: " + platform);

      switch (platform) {
        case "windows_10_edge":
          sauceOptions.get().setPlatformName(SaucePlatform.WINDOWS_10);
          sauceOptions.get().setBrowserName(Browser.EDGE);
          break;
        case "mac_sierra_chrome":
          sauceOptions.get().setPlatformName(SaucePlatform.MAC_SIERRA);
          sauceOptions.get().setBrowserName(Browser.CHROME);
          break;
        case "windows_8_ff":
          sauceOptions.get().setPlatformName(SaucePlatform.WINDOWS_8);
          sauceOptions.get().setBrowserName(Browser.FIREFOX);
          break;
        case "windows_8_1_ie":
          sauceOptions.get().setPlatformName(SaucePlatform.WINDOWS_8_1);
          sauceOptions.get().setBrowserName(Browser.INTERNET_EXPLORER);
          break;
        case "mac_mojave_safari":
          sauceOptions.get().setPlatformName(SaucePlatform.MAC_MOJAVE);
          sauceOptions.get().setBrowserName(Browser.SAFARI);
          break;
        default:
          // accept Sauce defaults
          break;
      }

      SauceSession sauceSession = new SauceSession(sauceOptions.get());
      sauceSession.setDataCenter(DataCenter.EU_CENTRAL);

      session.set(sauceSession);
      getSession().start();

    } else if (projectProperties.getEnvironment().equalsIgnoreCase("local")) {
      String platform;
      if (System.getProperty("platform") != null) {
        platform = System.getProperty("platform");
      } else {
        platform = "chrome";
      }

      log.info("▒▒▒ PLATFORM: " + platform);

      switch (platform) {
        case "firefox":
          driver = new FirefoxDriver();
          break;
        case "edge":
          driver = new EdgeDriver();
          break;
        default:
          driver = new ChromeDriver();
          break;
      }
    } else if (projectProperties.getEnvironment().equalsIgnoreCase("remote")) {
      String platform;
      if (System.getProperty("platform") != null) {
        platform = System.getProperty("platform");
      } else {
        platform = "firefox";
      }

      log.info("▒▒▒ PLATFORM: " + platform);
      log.info("▒▒▒ HOST AND PORT: "
              + projectProperties.getProperties().getProperty("HOST_AND_PORT"));

      URL remoteURL = new URL(projectProperties.getProperties().getProperty("HOST_AND_PORT"));

      switch (platform) {
        case "edge":
          EdgeOptions edgeOptions = new EdgeOptions();
          edgeOptions.setCapability("se:recordVideo", true);
          edgeOptions.setCapability("se:timeZone", "Asia/Manila");
          edgeOptions.setCapability("se:screenResolution", "1920x1080");
          driver = new RemoteWebDriver(remoteURL, edgeOptions);
          break;
        case "chrome":
          ChromeOptions options = new ChromeOptions();
          options.setCapability("se:recordVideo", true);
          options.setCapability("se:timeZone", "Asia/Manila");
          options.setCapability("se:screenResolution", "1920x1080");
          driver = new RemoteWebDriver(remoteURL, options);
          break;
        case "firefox":
        default:
          FirefoxOptions firefoxOptions = new FirefoxOptions();
          firefoxOptions.setCapability("se:recordVideo", true);
          firefoxOptions.setCapability("se:timeZone", "Asia/Manila");
          firefoxOptions.setCapability("se:screenResolution", "1920x1080");
          driver = new RemoteWebDriver(remoteURL, firefoxOptions);
          break;
      }
    }



    wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    getDriver().manage().window().maximize();

    videoRecorder = new VideoRecorder(projectProperties, currentFeature, currentScenario);
    videoRecorder.start();
  }

  @When("user visits {string} website")
  public void user_opens_web_app(String url) {
    getDriver().get(url);
  }

  @When("user enters {string} email")
  public void user_enters_email(String username) {
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
    element.sendKeys(username);
  }

  @When("user enters {string} password")
  public void user_enters_password(String password) {
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
    element.sendKeys(password);
  }

  @When("user clicks {string}")
  public void user_clicks(String label) {
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[contains(text(), '" + label + "')]")));
    element.click();
  }

  @Then("{string} is displayed")
  public void textIsDisplayed(String text) {
    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[contains(text(),'" + text + "')]")));
    Screenshoter.getScreenshoterInstance(projectProperties)
            .takeScreenshot(getDriver(), currentFeature, currentScenario);
    assertTrue(element.isDisplayed());
  }

  @Given("user visits a website")
  public void user_visits_a_website() throws Exception {
    // This step is expected to pull data from Excel
    log.info("▒▒▒ CURRENT FEATURE " + this.currentFeature);
    TestData testData = new TestData.Builder()
            .sheet(this.currentFeature)
            .row(this.currentScenario)
            .column("Website")
            .build();
    String website = ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
    log.info("▒▒▒ WEBSITE: " + website);
    getDriver().get(website);
  }

  @Then("the page is displayed")
  public void the_page_is_displayed() throws Exception {

    TestData testData = new TestData.Builder()
            .sheet(this.currentFeature)
            .row(this.currentScenario)
            .column("Text to verify")
            .build();
    String textToVerify = ExcelTestDataReader.getExcelTestDataReader().getTestData(testData);
    log.info("▒▒▒ textToVerify: " + textToVerify);
    log.info("▒▒▒ LOCATOR: " + ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[contains(text(),'" + textToVerify + "')]")));
    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[contains(text(),'" + textToVerify + "')]")));
    Screenshoter.getScreenshoterInstance(projectProperties)
            .takeScreenshot(getDriver(), currentFeature, currentScenario);
    assertTrue(element.isDisplayed());
  }


  @After
  public void tearDown(Scenario scenario) throws IOException, AWTException {
    if (projectProperties.getEnvironment().equalsIgnoreCase("local")
            || projectProperties.getEnvironment().equalsIgnoreCase("remote")) {
      try {
        driver.close();
        driver.quit();
      } catch (Exception e) {
        if (driver != null) {
          driver.quit();
        }
      }
    } else {
      getSession().stop(!scenario.isFailed());
    }

    videoRecorder.stop();
  }
}
