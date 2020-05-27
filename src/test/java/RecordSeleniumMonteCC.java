import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.IOException;
import org.monte.media.math.Rational;
import org.monte.media.Format;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

import java.io.File;
import java.util.List;




public class RecordSeleniumMonteCC {

    // The WebDriver is a tool for writing automated tests of websites.
    FirefoxDriver driver;

    private ScreenRecorder screenRecorder;

    @BeforeEach
    public void beforeTest() {

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        try {
            this.screenRecorder = new ScreenRecorder(gc,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,"black",
                            FrameRateKey, Rational.valueOf(30)),
                    null);
            this.screenRecorder.start();

        } catch (Exception e) {
            System.out.println("screenRecorder.start " + e.getMessage());
        }
    }

    @AfterEach
    public void afterTest()  {

        try {
            this.screenRecorder.stop();
            List createdMovieFiles = screenRecorder.getCreatedMovieFiles();
            for (Object movie: createdMovieFiles) {
                File m = (File)movie;
                System.out.println("New movie created: " + m.getAbsolutePath());
            }

        } catch (IOException ioe) {
            System.out.println("screenRecorder.stop " + ioe.getMessage());
        }
    }



    @Test
    public void test_Oversized() {

        // GeckoDriver is a connecting link to the Firefox browser for your scripts in Selenium.
        System.setProperty("webdriver.gecko.driver", ".\\libs\\geckodriver.exe");

        // create driver
        driver = new FirefoxDriver();

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

        // Vergleich des Ergebnisses
        String str2 = driver.findElement(By.xpath("//*[@id=\"content\"]/div[2]")).getText().substring(0,2);
        System.out.println("str2: " + str2.substring(0,2));
        assertTrue(str2.contains("45"));

        // Webdriver schliesst die Seite
        driver.close();

    }








}
