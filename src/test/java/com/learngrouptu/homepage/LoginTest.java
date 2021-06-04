package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
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
        LoginTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        LoginTest.connection = connection;
    }

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080");
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        LoginTest.driver.close();
        connection.close();
    }

    @Test
    public void testRedirectToLoginWithoutLogin() {
        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/login");
    }

    @Test
    public void testNoAccessMessageWithoutLogin() {
        assertTrue(driver.getPageSource().toLowerCase().contains("kein zugriff"));
    }

    @Test
    public void testRedirectToLoginWithoutLoginOnSupages() {
        driver.get("http://localhost:8080/annonceErstellen");
        testRedirectToLoginWithoutLogin();
        testNoAccessMessageWithoutLogin();
    }

    @Test
    public void testCorrectUrlOnSuccessfulLogin() {
        driver.findElement(By.name("username")).sendKeys("testuser");
        driver.findElement(By.name("password")).sendKeys("testpasswort");
        driver.findElement(By.name("login-button")).click();
        driver.getCurrentUrl().equals("http://localhost:8080/home");
    }
}
