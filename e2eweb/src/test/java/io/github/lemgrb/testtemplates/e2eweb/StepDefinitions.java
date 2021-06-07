package io.github.lemgrb.testtemplates.e2eweb;

import com.saucelabs.saucebindings.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

@Slf4j
public class StepDefinitions {

    private boolean isLocal;

    protected WebDriverWait wait;
    protected WebDriver driver;

    protected static ThreadLocal<SauceSession> session = new ThreadLocal<>();
    protected static ThreadLocal<SauceOptions> options = new ThreadLocal<>();

    public SauceSession getSession() {
        return session.get();
    }

    public WebDriver getDriver() {
        if(isLocal)
            return driver;
        return getSession().getDriver();
    }

    @Before
    public void setup(Scenario scenario) throws Exception {

        isLocal = !System.getProperty("env").equalsIgnoreCase("saucelabs");
        String config = isLocal?
          "config.properties":"config.saucelabs.properties";

        log.info("▒ USING CONFIGURATION FILE: " + config);

        // TODO: Add to separate class
        Properties prop = new Properties();
        FileInputStream ip = new FileInputStream(config);
        prop.load(ip);

        String platform = prop.getProperty("platform");

        log.info("▒ PLATFORM: " + platform);
        log.info("▒ SAUCE_USERNAME: " + System.getenv("SAUCE_USERNAME")!=null?"FOUND":"NOT FOUND");
        log.info("▒ SAUCE_ACCESS_KEY: " + System.getenv("SAUCE_ACCESS_KEY")!=null?"FOUND":"NOT FOUND");

        if(!isLocal) {
          options.set(new SauceOptions());
          options.get().setName(scenario.getName());

          if (System.getenv("START_TIME") != null) {
              options.get().setBuild("Build Time: " + System.getenv("START_TIME"));
          }

          if (System.getProperty("platform") != null) {
              platform = System.getProperty("platform");
          } else {
              platform = "default";
          }

          switch(platform) {
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
          wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        } else {

          switch(platform) {
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
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(), '"+label+"')]")));
        element.click();
    }

    @Then("{string} is displayed")
    public void textIsDisplayed(String text) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'"+text+"')]")));
        assertTrue(element.isDisplayed());
    }

    @After
    public void tearDown(Scenario scenario) {
        if(isLocal) {
          try {
              driver.close();
              driver.quit();
          } catch (Exception e) {
              if(driver!=null)
                  driver.quit();
          }
        } else {
          getSession().stop(!scenario.isFailed());
        }
    }

}
