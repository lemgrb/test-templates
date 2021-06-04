package io.github.lemgrb.testtemplates.e2eweb;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Slf4j
public class StepDefinitions {

    private WebDriver driver;

    @Before
    public void beforeEachScenario() {
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver\\91.0.4472.19\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @When("user visits {string} website")
    public void user_opens_web_app(String url) {
        driver.get(url);
    }

    @When("user enters {string} email")
    public void user_enters_email(String username) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.id("email")));
        element.sendKeys(username);
    }

    @When("user enters {string} password")
    public void user_enters_password(String password) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.id("password")));
        element.sendKeys(password);
    }

    @When("user clicks {string}")
    public void user_clicks(String label) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(), '"+label+"')]")));
        element.click();
    }

    @Then("{string} is displayed")
    public void textIsDisplayed(String text) {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'"+text+"')]")));
        assertTrue(element.isDisplayed());
    }

    @After
    public void afterEachScenario() {
        try {
            driver.close();
            driver.quit();
        } catch (Exception e) {
            if(driver!=null)
                driver.quit();
        }
    }

}
