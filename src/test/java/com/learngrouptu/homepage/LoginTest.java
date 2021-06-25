package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    public static final String url_homepage = "http://localhost:8080/home";
    public static final String url_login = "http://localhost:8080/login";
    private static ChromeDriver driver;
    private static Connection connection;

    @BeforeAll
    public static void init() throws SQLException {
        ChromeOptions options = new ChromeOptions();
        ChromeDriver driver = chromeDriverSetup(options);
        LoginTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        LoginTest.connection = connection;

        insertTestUser();
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        LoginTest.driver.close();
        cleanDB();
        connection.close();
    }

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080");
    }

    @AfterEach
    public void logout() {
        driver.get("http://localhost:8080/perform_logout");
    }

    @Test
    public void testRedirectToLoginWithoutLogin() {
        assertTrue(driver.getCurrentUrl().startsWith(url_login));
    }

    @Test
    public void testRedirectToLoginWithoutLoginOnSupages() {
        driver.get("http://localhost:8080/annonceErstellen");
        testRedirectToLoginWithoutLogin();
    }

    @Test
    public void testCorrectUrlOnSuccessfulLogin() {
      /*  driver.findElementById("loginModal").sendKeys(Keys.RETURN);*/
        login("testuser", "testpasswort");
        driver.getCurrentUrl().equals(url_homepage);
    }

    private static ChromeDriver chromeDriverSetup(ChromeOptions options) {
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        return driver;
    }

    private static void cleanDB() throws SQLException {
        Statement stmt = connection.createStatement();
        String sqlStatement = "DELETE FROM user WHERE email LIKE 'testuser%' and " +
                "username LIKE 'testuser%'";
        stmt.execute(sqlStatement);
    }

    private static void insertTestUser() {
        register("testuser", "testemail", "testpasswort");
    }

    private void login(String username, String password) {
        driver.get("http://localhost:8080/login.html");
        driver.findElementById("loginModal").sendKeys(Keys.RETURN);
        WebDriverWait wait=new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOf(driver.findElementByName("username")));
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login-button")).sendKeys(Keys.RETURN);
    }

    private static void register(String username, String email, String password) {
        driver.get("http://localhost:8080/login.html");
        driver.findElement(By.name("register-button")).sendKeys(Keys.RETURN);
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").sendKeys(Keys.RETURN);
    }
}
