import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.junit.jupiter.api.Assertions;

import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Attachment;
import io.qameta.allure.Story;
import io.qameta.allure.Epic;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Allure Framework is a flexible lightweight multi-language test report tool
 * that not only shows a very concise representation of what have been tested
 * in a neat web report form, but allows everyone participating in the development
 * process to extract maximum of useful information from everyday execution of tests.
 */
public class TestEinstiegAllureReport {

    @Test
    @Feature("feature 001")
    // @Description shows detailed test description in the report
    @Description("test_Oversized")
    //@Severity annotation is used in order to prioritize test methods by severity:
    @Severity(SeverityLevel.CRITICAL)
    // to link tests to Epics and Stories.
    @Epic("BMI Rechner")
    @Story("bmi calculaction")
    public void test_Oversized() {

        /**
         * What is GeckoDriver?
         GeckoDriver is a connecting link to the Firefox browser for your scripts in Selenium.
         GeckoDriver is a proxy which helps to communicate with the Gecko-based browsers (e.g. Firefox), for which it provides HTTP API.

         Firefox's geckodriver *requires* you to specify its location.
         */
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\kay.grebenstein\\workspace\\Selenium_Kay2\\libs\\geckodriver.exe");

        // Aufruf der Seite
        FirefoxDriver driver=new FirefoxDriver();
        driver.get("https://60tools.com/de/tool/bmi-calculator");
        driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);

        // Eingabe der Daten in die Textfelder
        driver.findElement(By.name("weight")).sendKeys("180");
        driver.findElement(By.name("size")).sendKeys("200");
        driver.findElement(By.name("age")).sendKeys("20");

        // Auswahl des Geschlechts
        WebElement gender = driver.findElement(By.name("sex"));
        gender.sendKeys("Männlich");
        gender.sendKeys(Keys.RETURN);

        // Starten der Berechnung per Klick auf den Button
        WebElement button = driver.findElement(By.xpath("//*[@id=\"toolForm\"]/table/tbody/tr[5]/td[2]/input[2]"));
        button.click();

        // Vergleich des Ergebnisses
        String str2 = driver.findElement(By.xpath("//*[@id=\"content\"]/div[2]")).getText().substring(0,2);
        System.out.println("str2: " + str2.substring(0,2));
        Assertions.assertTrue(str2.contains("45"));

        driver.close();
    }

    @Test
    @Feature("feature 001")
    @Description("test_Undersized")
    @Severity(SeverityLevel.MINOR)
    @Epic("BMI Rechner")
    @Story("bmi calculaction")
    @Attachment(value = "Page screenshot", type = "image/png")
    public void test_Undersized() {

        /**
         * What is GeckoDriver?
         GeckoDriver is a connecting link to the Firefox browser for your scripts in Selenium.
         GeckoDriver is a proxy which helps to communicate with the Gecko-based browsers (e.g. Firefox), for which it provides HTTP API.

         Firefox's geckodriver *requires* you to specify its location.
         */
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\kay.grebenstein\\workspace\\Selenium_Kay2\\libs\\geckodriver.exe");

        // Aufruf der Seite
        FirefoxDriver driver=new FirefoxDriver();
        driver.get("https://60tools.com/de/tool/bmi-calculator");
        driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);

        // Eingabe der Daten in die Textfelder
        driver.findElement(By.name("weight")).sendKeys("50");
        driver.findElement(By.name("size")).sendKeys("200");
        driver.findElement(By.name("age")).sendKeys("20");

        // Auswahl des Geschlechts
        WebElement gender = driver.findElement(By.name("sex"));
        gender.sendKeys("Männlich");
        gender.sendKeys(Keys.RETURN);

        // Starten der Berechnung per Klick auf den Button
        WebElement button = driver.findElement(By.xpath("//*[@id=\"toolForm\"]/table/tbody/tr[5]/td[2]/input[2]"));
        button.click();

        // Vergleich des Ergebnisses
        String str2 = driver.findElement(By.xpath("//*[@id=\"content\"]/div[2]")).getText().substring(0,4);
        System.out.println("str2: " + str2.substring(0,4));
        Assertions.assertTrue(str2.contains("45"));

        driver.close();
    }


}
