package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    private static void login() {
        driver.get("http://localhost:8080/login.html");
        driver.findElementById("loginModal").sendKeys(Keys.RETURN);
        WebDriverWait wait=new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOf(driver.findElementByName("username")));
        driver.findElement(By.name("username")).sendKeys("testuser");
        driver.findElement(By.name("password")).sendKeys("testpassword");
        driver.findElement(By.name("login-button")).sendKeys(Keys.RETURN);
    }

    private static void register() {
        driver.get("http://localhost:8080/login.html");
        driver.findElement(By.name("register-button")).sendKeys(Keys.RETURN);
        driver.findElement(By.name("username")).sendKeys("testuser");
        driver.findElementByName("password").sendKeys("testpassword");
        driver.findElementByName("email").sendKeys("testmail");
        driver.findElementByName("register-submit-button").sendKeys(Keys.RETURN);
    }

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080/vorlesungErstellen");
    }

    @AfterAll
    public static void close() throws SQLException {
        driver.close();
        connection.close();
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
        driver.findElement(By.name("vorlesung-submit")).sendKeys(Keys.RETURN);
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
        driver.findElement(By.name("vorlesung-submit")).sendKeys(Keys.RETURN);
        String after = driver.getCurrentUrl();
        assertEquals(before, after);
    }

    @Test
    public void testUrlCorrectForWrongAndCorrectInput() throws InterruptedException {
        driver.findElement(By.id("studiengang")).sendKeys("Sozioinformatik");

        String before = driver.getCurrentUrl();
        driver.findElement(By.name("vorlesung-submit")).sendKeys(Keys.RETURN);
        String after = driver.getCurrentUrl();

        assertEquals(before, after);
        driver.findElement(By.id("cp")).sendKeys("9");
        driver.findElement(By.id("kursnr")).sendKeys("testnachricht");
        driver.findElement(By.id("titel")).sendKeys("testnachricht");
        driver.findElement(By.name("vorlesung-submit")).sendKeys(Keys.RETURN);
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
        driver.findElement(By.name("vorlesung-submit")).sendKeys(Keys.RETURN);

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
