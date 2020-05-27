import de.retest.web.selenium.RecheckDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;


public class TestReCheck007 {

    RecheckDriver driver;

    @BeforeEach
    public void openPage() {
        //Firefox's geckodriver *requires* you to specify its location.
        System.setProperty("webdriver.gecko.driver", ".\\libs\\geckodriver.exe");
        // Aufruf der Seite
        driver = new RecheckDriver(new FirefoxDriver());
        driver.get("https://www.uebungen-online.de/BMI-Rechner.html");
        driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);

    }

    @Test
    public void test_Recheck_Normal() {
        driver.startTest();

        // Eingabe der Daten in die Textfelder
        driver.findElement(By.id("weight")).sendKeys("100");
        driver.findElement(By.id("size")).sendKeys("200");

        // Starten der Berechnung per Klick auf den Button
        WebElement button = driver.findElement(By.name("calculate"));
        button.click();

        driver.capTest();
    }

    @Test
    public void test_Recheck_Over() {
        driver.startTest();

        // Eingabe der Daten in die Textfelder
        driver.findElement(By.id("weight")).sendKeys("200");
        driver.findElement(By.id("size")).sendKeys("200");

        // Starten der Berechnung per Klick auf den Button
        WebElement button = driver.findElement(By.name("calculate"));
        button.click();

        driver.capTest();
    }

    @AfterEach
    public void closePage() {

        driver.close();
    }

}
