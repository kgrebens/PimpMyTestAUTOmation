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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Allure Framework is a flexible lightweight multi-language test report tool
 * that not only shows a very concise representation of what have been tested
 * in a neat web report form, but allows everyone participating in the development
 * process to extract maximum of useful information from everyday execution of tests.
 */
public class TestEinstiegAllureReportAttach {

    @Test
    @Feature("Some feature")
    @Severity(SeverityLevel.CRITICAL)
    public void testOutput() {
        start();
    }

    @Step
    private void start() {
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

        step1(driver);
        step2(driver);
        step3(driver);
        step4(driver);
        step5(driver);
        step6(driver);
    }


    @Step
    private void step1(RemoteWebDriver driver) {
        // Eingabe der Daten in die Textfelder
        driver.findElement(By.name("weight")).sendKeys("180");
        driver.findElement(By.name("size")).sendKeys("200");
        driver.findElement(By.name("age")).sendKeys("20");

    }

    @Step
    private void step2(RemoteWebDriver driver) {

        // Auswahl des Geschlechts
        WebElement gender = driver.findElement(By.name("sex"));
        gender.sendKeys("Männlich");
        gender.sendKeys(Keys.RETURN);

    }



    @Step("3")
    private void step3(RemoteWebDriver driver) {

        // Starten der Berechnung per Klick auf den Button
        WebElement button = driver.findElement(By.xpath("//*[@id=\"toolForm\"]/table/tbody/tr[5]/td[2]/input[2]"));
        button.click();
    }

    @Step("4")
    private void step4(RemoteWebDriver driver) {

        System.out.println("getScreenshot1");
        //make a screenshot
        screenShot(driver, ".\\screenshots\\" ,"test_Oversized");
        System.out.println("getScreenshot3");

        attachment();

    }

    @Step("5")
    private void step5(RemoteWebDriver driver) {

        // Vergleich des Ergebnisses
        String str2 = driver.findElement(By.xpath("//*[@id=\"content\"]/div[2]")).getText().substring(0, 2);
        System.out.println("str2: " + str2.substring(0, 2));
        Assertions.assertTrue(str2.contains("45"));

    }

    @Step("6")
    private void step6(RemoteWebDriver driver) {

        driver.close();
    }

    @Attachment(value = "String attachment", type = "text/plain")
    public String attachment() {
        return "<p>HELLO</p>";
    }



    @Attachment(value = "4", type = "image/png")
    private static byte[] screenShot(RemoteWebDriver driver, String folder, String filename) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp  = dateFormat.format(new Date());

        try {
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            // Now you can do whatever you need to do with it, for example copy somewhere
            FileUtils.copyFile(scrFile, new File(folder + filename + "_" + timestamp + ".png"));
            return driver.getScreenshotAs(OutputType.BYTES);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("getScreenshot2");
        return new byte[0];

    }


}
