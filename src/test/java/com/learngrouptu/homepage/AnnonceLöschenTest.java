package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.management.StringValueExp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnonceLöschenTest
{
    private static ChromeDriver driver;
    private static Connection connection;
    private String createAnnonceURL = "http://localhost:8080/annonceErstellen";
    private String annoncenEinsehenURL = "http://localhost:8080/annonceEinsehen";
    private String meineAnnoncenURL = "http://localhost:8080/meineAnnoncen";


    @BeforeAll
    public static void init() throws SQLException
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        AnnonceLöschenTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        AnnonceLöschenTest.connection = connection;
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
        driver.get(annoncenEinsehenURL);
    }

    @Test
    public void successfullyInsertAndDeleteAnnonce()
    {
        String vorlesung = getRandVorl();
        String kontakt = getRandKontakt();
        String typ = getRandTyp();
        String nachricht = getRandNachricht();

        driver.get(createAnnonceURL);
        createAnnonce(vorlesung, kontakt, typ, nachricht);

        driver.get(meineAnnoncenURL);
        WebElement table = driver.findElementByName("annoncentable_body");
        List<WebElement> rows = table.findElements(By.tagName("TR"));
        for (WebElement row : rows) {
            String rowVorlName = row.findElement(By.name("vorlName")).getText();
            if (rowVorlName.equals(vorlesung)) {
                row.findElement(By.tagName("BUTTON")).sendKeys(Keys.RETURN);
                break;
            }
        }

        String content = driver.getPageSource();
        assertTrue(!content.contains(vorlesung));
        assertTrue(!content.contains(kontakt));
        assertTrue(!content.contains(nachricht));

        driver.get(annoncenEinsehenURL);
        content = driver.getPageSource();
        assertTrue(!content.contains(vorlesung));
        assertTrue(!content.contains(kontakt));
        assertTrue(!content.contains(nachricht));
    }

    private String getRandKontakt() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "testkontakt" + rand;
    }

    private String getRandNachricht() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "testnachricht" + rand;
    }

    private String getRandVorl() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "testvorlesung" + rand;
    }

    private String getRandTyp() {
        Double randNumb = Math.random();
        if (randNumb < 5.0) {
            return "Übungsgruppe";
        }
        else {
            return "Lerngruppe";
        }
    }

    private void createAnnonce(String vorlName, String kontakt, String typ, String nachricht) {
        driver.findElement(By.id("vorlName")).sendKeys(vorlName);
        driver.findElement(By.id("kontakt")).sendKeys(kontakt);
        Select objSelect = new Select(driver.findElement(By.name("choice")));
        objSelect.selectByVisibleText(typ);
        driver.findElement(By.name("nachricht")).sendKeys(nachricht);
        driver.findElement(By.name("submit-button")).sendKeys(Keys.RETURN);
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        connection.close();
        driver.close();
    }

}
