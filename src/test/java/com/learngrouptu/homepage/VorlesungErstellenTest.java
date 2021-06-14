package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class VorlesungErstellenTest {
    private static ChromeDriver driver;
    private static Connection connection;

    @BeforeAll
    public static void init() throws SQLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        VorlesungErstellenTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        VorlesungErstellenTest.connection = connection;
        register();
        login();
    }

    private static void register() {
        driver.get("http://localhost:8080/login.html");
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys("testuser");
        driver.findElementByName("password").sendKeys("testpassword");
        driver.findElementByName("email").sendKeys("testmail");
        driver.findElementByName("register-submit-button").click();
    }

    private static void login() {
        driver.get("http://localhost:8080/login.html");
        driver.findElement(By.name("username")).sendKeys("testuser");
        driver.findElement(By.name("password")).sendKeys("testpassword");
        driver.findElement(By.name("login-button")).click();
    }

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080/vorlesungsubersichterstellen");
    }

    @AfterAll
    public static void close() throws SQLException {
        driver.close();
        connection.close();
    }

    @Test
    public void testUrlCorrectForCorrectInput() throws InterruptedException { ;
        driver.findElement(By.id("kursnr")).sendKeys("vlad");
        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");
        driver.findElement(By.id("titel")).sendKeys("math 4");

        driver.findElement(By.name("vorlesung-submit")).click();
        String realUrl = driver.getCurrentUrl();
        String expectedUrl = "http://localhost:8080/vorlesungsubersicht";

        assertEquals(expectedUrl, realUrl);
    }

    @Test
    public void testUrlCorrectForIncorrectInput() throws InterruptedException {
        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");
        driver.findElement(By.id("titel")).sendKeys("math 4");

        driver.findElement(By.name("vorlesung-submit")).click();
        String realUrl = driver.getCurrentUrl();
        String expectedUrl = "http://localhost:8080/vorlesungsubersichterstellen";

        assertEquals(expectedUrl, realUrl);
    }

    @Test
    public void testContentCorrectForCorrectInput() throws InterruptedException {
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
        driver.findElement(By.name("vorlesung-submit")).click();
        String realContent = driver.getPageSource().toString();
        Assertions.assertTrue(realContent.contains(vorlName));
        Assertions.assertTrue(realContent.contains(kontakt));
        Assertions.assertTrue(realContent.contains(nachricht));
    }

    @Test
    public void testUrlCorrectForWrongInput() throws InterruptedException {
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");
        driver.findElement(By.id("cp")).sendKeys("9");
        String before = driver.getCurrentUrl();
        driver.findElement(By.name("vorlesung-submit")).click();
        String after = driver.getCurrentUrl();
        assertEquals(before, after);
    }

    @Test
    public void testUrlCorrectForWrongAndCorrectInput() throws InterruptedException {
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");

        String before = driver.getCurrentUrl();
        driver.findElement(By.name("vorlesung-submit")).click();
        String after = driver.getCurrentUrl();

        assertEquals(before, after);
        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("kursnr")).sendKeys("testnachricht");
        driver.findElement(By.id("titel")).sendKeys("testnachricht");
        driver.findElement(By.name("vorlesung-submit")).click();
        after = driver.getCurrentUrl();
        assertNotEquals(before, after);
    }

    @Test
    public void testDBSuccessfullyInserted() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        VorlesungErstellenTest.connection = connection;
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");
        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("kursnr")).sendKeys("testnachricht");
        driver.findElement(By.id("titel")).sendKeys("testnachricht");
        driver.findElement(By.name("vorlesung-submit")).click();

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
    }
}
