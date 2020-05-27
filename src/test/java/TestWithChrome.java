import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@TestMethodOrder(OrderAnnotation.class)
public class TestWithChrome {

    ChromeDriver driver;

    @BeforeEach
    public void starten() {

        String pathToChromeDriver = ".\\libs\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);

        driver = new ChromeDriver();

    }

    @Test
    @Order(1)
    @DisplayName("Custom test name containing spaces")
    public void testenUebergewicht() {
        // Aufruf der Webseite
        driver.get("https://60tools.com/de/tool/bmi-calculator");

        // IN Felder was eingeben
        driver.findElement(By.name("weight")).sendKeys("200");
        driver.findElement(By.name("size")).sendKeys("200");
        driver.findElement(By.name("age")).sendKeys("100");

        // EIngabe männlich
        driver.findElement(By.name("sex")).sendKeys("Männlich");

        driver.findElement(By.xpath("//*[@id=\"toolForm\"]/table/tbody/tr[5]/td[2]/input[2]")).click();
        String text = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/span[1]")).getText();
        System.out.println("text: " + text);
        Assertions.assertTrue(text.contains("Übergewicht"));

    }

    @Test
    @Order(2)
    @DisplayName("😱")
    public void testenUntergewicht() {

        // Aufruf der Webseite
        driver.get("https://60tools.com/de/tool/bmi-calculator");

        // IN Felder was eingeben
        driver.findElement(By.name("weight")).sendKeys("50");
        driver.findElement(By.name("size")).sendKeys("180");
        driver.findElement(By.name("age")).sendKeys("20");

        // EIngabe männlich
        driver.findElement(By.name("sex")).sendKeys("Männlich");

        driver.findElement(By.xpath("//*[@id=\"toolForm\"]/table/tbody/tr[5]/td[2]/input[2]")).click();
        String text = driver.findElement(By.xpath("//*[@id=\"content\"]/div[4]/span[1]")).getText();
        System.out.println("text: " + text);
        Assertions.assertTrue(text.contains("Untergewicht"));

    }


    @AfterEach
    public void closePage() {
        driver.close();
    }


}

