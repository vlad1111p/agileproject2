package com.learngrouptu.homepage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PasswordTest {
    private static ChromeDriver driver;
    private static Connection connection;

    @BeforeAll
    public static void init() throws SQLException {
        ChromeDriver driver = chromeDriverSetup();
        PasswordTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        PasswordTest.connection = connection;
        register();
        login();
    }

    private static ChromeDriver chromeDriverSetup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        return driver;
    }

    private static void register() {
        driver.get("http://localhost:8080/login.html");
        driver.findElement(By.name("register-button")).sendKeys(Keys.RETURN);
        driver.findElement(By.name("username")).sendKeys("tester");
        driver.findElementByName("password").sendKeys("tester");
        driver.findElementByName("email").sendKeys("nicolae1111p@gmail.com");
        driver.findElementByName("register-submit-button").sendKeys(Keys.RETURN);
    }

    private static void login() {
        driver.get("http://localhost:8080/login.html");
        driver.findElementById("loginModal").sendKeys(Keys.RETURN);
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOf(driver.findElementByName("username")));
        driver.findElement(By.name("username")).sendKeys("tester");
        driver.findElement(By.name("password")).sendKeys("tester");
        driver.findElement(By.name("login-button")).sendKeys(Keys.RETURN);
    }

    private static void login(String password) {
        driver.get("http://localhost:8080/login.html");
        driver.findElementById("loginModal").sendKeys(Keys.RETURN);
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOf(driver.findElementByName("username")));
        driver.findElement(By.name("username")).sendKeys("tester");
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login-button")).sendKeys(Keys.RETURN);
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        PasswordTest.driver.close();
        PasswordTest.connection.close();
    }

    @Test
    public void passwordSuccessfullyChanged() {
        driver.get("http://localhost:8080/passwordChange");
        System.out.println(driver.getCurrentUrl());
        driver.findElement(By.id("altesPassword")).sendKeys("tester");
        driver.findElement(By.id("altesPassword1")).sendKeys("tester");
        driver.findElement(By.id("neuesPassword")).sendKeys("tester1");
        driver.findElement(By.id("neuesPassword1")).sendKeys("tester1");
        driver.findElementByName("profile_change-submit-button").sendKeys(Keys.RETURN);
        driver.findElementByName("logout").sendKeys(Keys.RETURN);
        login("tester1");
        String expectedUrl = "http://localhost:8080/home";
        String currentUrl = driver.getCurrentUrl();
        System.out.println(driver.getCurrentUrl());
        driver.get("http://localhost:8080/passwordChange");
        System.out.println(driver.getCurrentUrl());
        driver.findElement(By.id("altesPassword")).sendKeys("tester1");
        driver.findElement(By.id("altesPassword1")).sendKeys("tester1");
        driver.findElement(By.id("neuesPassword")).sendKeys("tester");
        driver.findElement(By.id("neuesPassword1")).sendKeys("tester");
        driver.findElementByName("profile_change-submit-button").sendKeys(Keys.RETURN);
        Assertions.assertEquals(currentUrl, expectedUrl);


    }


}
