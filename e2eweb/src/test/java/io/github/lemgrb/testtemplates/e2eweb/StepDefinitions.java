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
import io.github.lemgrb.testtemplates.e2eweb.utilities.TestData;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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

  protected static ThreadLocal<SauceSession> session = new ThreadLocal<>();
  protected static ThreadLocal<SauceOptions> options = new ThreadLocal<>();

  public SauceSession getSession() {
    return session.get();
  }

  public WebDriver getDriver() {
    if (projectProperties.getEnvironment().equalsIgnoreCase("local")) {
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

      options.set(new SauceOptions());
      options.get().setName(scenario.getName());

      if (System.getenv("START_TIME") != null) {
        options.get().setBuild("Build Time: " + System.getenv("START_TIME"));
      }

      String platform;
      if (System.getProperty("platform") != null) {
        platform = System.getProperty("platform");
      } else {
        platform = "default";
      }

      switch (platform) {
        case "windows_10_edge":
          options.get().setPlatformName(SaucePlatform.WINDOWS_10);
          options.get().setBrowserName(Browser.EDGE);
          break;
        case "mac_sierra_chrome":
          options.get().setPlatformName(SaucePlatform.MAC_SIERRA);
          options.get().setBrowserName(Browser.CHROME);
          break;
        case "windows_8_ff":
          options.get().setPlatformName(SaucePlatform.WINDOWS_8);
          options.get().setBrowserName(Browser.FIREFOX);
          break;
        case "windows_8_1_ie":
          options.get().setPlatformName(SaucePlatform.WINDOWS_8_1);
          options.get().setBrowserName(Browser.INTERNET_EXPLORER);
          break;
        case "mac_mojave_safari":
          options.get().setPlatformName(SaucePlatform.MAC_MOJAVE);
          options.get().setBrowserName(Browser.SAFARI);
          break;
        default:
          // accept Sauce defaults
          break;
      }

      SauceSession sauceSession = new SauceSession(options.get());
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

    }

    wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    getDriver().manage().window().maximize();
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
    assertTrue(element.isDisplayed());
  }


  @After
  public void tearDown(Scenario scenario) {
    if (projectProperties.getEnvironment().equalsIgnoreCase("local")) {
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
  }
}