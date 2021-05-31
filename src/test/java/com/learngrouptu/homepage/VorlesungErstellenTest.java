package com.learngrouptu.homepage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class VorlesungErstellenTest {
    private static ChromeDriver driver;
    private static Connection connection;

    public ChromeDriver init() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/vorlesungsubersichterstellen");
        return driver;
    }

    @Test
    public void verifyTitle() {
        ChromeDriver driver = init();
        String title = driver.getTitle();
        assertTrue(title.contains("vorlesung erstellen"));
        driver.close();
    }

    @Test
    public void testUrlCorrectForCorrectInput() throws InterruptedException {
        ChromeDriver driver = init();
        driver.findElement(By.id("kursnr")).sendKeys("vlad");
        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");
        driver.findElement(By.id("titel")).sendKeys("math 4");

        driver.findElement(By.id("button1")).click();
        String realUrl = driver.getCurrentUrl();
        String expectedUrl = "http://localhost:8080/vorlesungadd";

        assertEquals(expectedUrl, realUrl);
        driver.close();
    }

    @Test
    public void testUrlCorrectForIncorrectInput() throws InterruptedException {
        ChromeDriver driver = init();

        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");
        driver.findElement(By.id("titel")).sendKeys("math 4");

        driver.findElement(By.id("button1")).click();
        String realUrl = driver.getCurrentUrl();
        String expectedUrl = "http://localhost:8080/vorlesungsubersichterstellen";

        assertEquals(expectedUrl, realUrl);
        driver.close();
    }

    @Test
    public void testContentCorrectForCorrectInput() throws InterruptedException {
        ChromeDriver driver = init();
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String vorlName = "vlad" + rand;
        String kontakt = "vlad" + rand + "@gmail.com";
        String nachricht = "testnachricht" + rand;
        String cp = rand;

        driver.findElement(By.id("kursnr")).sendKeys(vorlName);
        driver.findElement(By.id("cp")).sendKeys(cp);
        driver.findElement(By.id("titel")).sendKeys(kontakt);
        driver.findElement(By.id("studiengang")).sendKeys(nachricht);
        driver.findElement(By.id("button1")).click();
        String realContent = driver.getPageSource().toString();
        Assertions.assertTrue(realContent.contains(vorlName));
        Assertions.assertTrue(realContent.contains(kontakt));
        Assertions.assertTrue(realContent.contains(nachricht));
        driver.close();
    }

    @Test
    public void testUrlCorrectForWrongInput() throws InterruptedException {
        ChromeDriver driver = init();
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");
        driver.findElement(By.id("cp")).sendKeys("9");
        String before = driver.getCurrentUrl();
        driver.findElement(By.id("button1")).click();
        String after = driver.getCurrentUrl();
        assertEquals(before, after);
        driver.close();
    }

    @Test
    public void testUrlCorrectForWrongAndCorrectInput() throws InterruptedException {
        ChromeDriver driver = init();
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");

        String before = driver.getCurrentUrl();
        driver.findElement(By.id("button1")).click();
        String after = driver.getCurrentUrl();

        assertEquals(before, after);
        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("kursnr")).sendKeys("testnachricht");
        driver.findElement(By.id("titel")).sendKeys("testnachricht");
        driver.findElement(By.id("button1")).click();
        after = driver.getCurrentUrl();
        assertNotEquals(before, after);
        driver.close();
    }

    @Test
    public void testDBSuccessfullyInserted() throws SQLException {
        ChromeDriver driver = init();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        VorlesungErstellenTest.connection = connection;
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");
        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("kursnr")).sendKeys("testnachricht");
        driver.findElement(By.id("titel")).sendKeys("testnachricht");
        driver.findElement(By.id("button1")).click();

        String sqlStatement = "SELECT titel, kursnr, studiengang, cp FROM vorlesung";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        boolean present = false;
        while (rs.next()) {
            String dbVorl = (rs.getString("titel"));
            String dbTyp = (rs.getString("kursnr"));
            String dbKontakt = (rs.getString("studiengang"));
            String dbNachricht = (rs.getString("cp"));
            if (dbVorl.equals("testnachricht") && dbTyp.equals("testnachricht") && dbKontakt.equals("Sozioinformatik") && dbNachricht.equals("9")) {
                present = true;
            }
        }
        assertEquals(present, true);
        driver.close();
    }
}
