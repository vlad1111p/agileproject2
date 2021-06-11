package com.learngrouptu.homepage;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.support.ui.Select;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AnnonceSuchenTest {
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
        AnnonceSuchenTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        AnnonceSuchenTest.connection = connection;
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
        driver.get("http://localhost:8080/annonceEinsehen");
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        AnnonceSuchenTest.driver.close();
        Statement stmt = connection.createStatement();
        String sqlStatement = "DELETE FROM annonce WHERE vorl_name = 'Testvorlesung' AND kontakt = 'Testkontakt' " +
                                "AND nachricht = 'Testnachricht'";
        stmt.execute(sqlStatement);
        connection.close();
    }

    @Test
    public void checkIfSearchFindsResults() {
        driver.get("http://localhost:8080/annonceErstellen");
        driver.findElement(By.id("vorlName")).sendKeys("Testvorlesung");
        driver.findElement(By.id("kontakt")).sendKeys("Testkontakt");
        Select objSelect = new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("Nachricht")).sendKeys("Testnachricht");
        driver.findElement(By.id("button1")).click();
        Assertions.assertTrue(driver.getCurrentUrl().equals("http://localhost:8080/addannonce"));
        driver.findElement(By.id("vorlName")).sendKeys("Testvorlesung");
        driver.findElement(By.id("button1")).submit();
        boolean ifCorrect = driver.getPageSource().contains("Testvorlesung") && driver.getPageSource().contains("Testkontakt")
                    && driver.getPageSource().contains("Testnachricht");
        Assertions.assertTrue(ifCorrect);
    }
}
