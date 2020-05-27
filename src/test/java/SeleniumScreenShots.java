import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.remote.RemoteWebDriver;


public class SeleniumScreenShots {

    @Test
    public void test_Oversized() {

        // GeckoDriver is a connecting link to the Firefox browser for your scripts in Selenium.
        System.setProperty("webdriver.gecko.driver", ".\\libs\\geckodriver.exe");

        // The WebDriver is a tool for writing automated tests of websites.
        FirefoxDriver driver=new FirefoxDriver();

        // Maximize the window
        driver.manage().window().maximize();

        // Aufruf der Seite
        driver.get("https://60tools.com/de/tool/bmi-calculator");

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

        //make a screenshot
        screenShot(driver, ".\\screenshots\\" ,"test_Oversized");

        // Vergleich des Ergebnisses
        String str2 = driver.findElement(By.xpath("//*[@id=\"content\"]/div[2]")).getText().substring(0,2);
        System.out.println("str2: " + str2.substring(0,2));
        assertTrue(str2.contains("45"));

        // Webdriver schliesst die Seite
        driver.close();

    }



    private void screenShot(RemoteWebDriver driver, String folder, String filename) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp  = dateFormat.format(new Date());

        try {
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            // Now you can do whatever you need to do with it, for example copy somewhere
            FileUtils.copyFile(scrFile, new File(folder + filename + "_" + timestamp + ".png"));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }






}
