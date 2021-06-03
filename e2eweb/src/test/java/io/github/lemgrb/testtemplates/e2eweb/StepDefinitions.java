package io.github.lemgrb.testtemplates.e2eweb;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class StepDefinitions {

    private WebDriver driver;

    @Before
    public void beforeScenario() {
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver\\91.0.4472.19\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @When("user opens {string} web app")
    public void user_opens_web_app(String string) {
        driver.get(string);
    }
    @Then("homepage with title {string} is displayed")
    public void homepage_with_title_is_displayed(String string) {
        String actualTitle = driver.getTitle();
        Assert.assertEquals(string, actualTitle);
    }

    @After
    public void afterScenario() {
        try {
            driver.close();
            driver.quit();
        } catch (Exception e) {
            if(driver!=null)
                driver.quit();
        }
    }

}
