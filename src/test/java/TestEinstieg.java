import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.junit.jupiter.api.Assertions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestEinstieg {

    @Test
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
        assertTrue(str2.contains("45"));

        driver.close();

    }

}
