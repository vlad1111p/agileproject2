package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileTest {
    private static ChromeDriver driver;
    private static Connection connection;

    @BeforeAll
    public static void init() throws SQLException {
        ChromeDriver driver = chromeDriverSetup();
        ProfileTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        ProfileTest.connection = connection;
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

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        ProfileTest.driver.close();
        ProfileTest.connection.close();
    }

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080/profilEinstellen");
    }

    private String getRandStudiengang() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "Teststudiengang" + rand;
    }

    private String getRandBio() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "Testbio" + rand;
    }

    private String getRandAbschluss() {
        Integer randNumb = (int)Math.random() * 3;
        if(randNumb==0) {
            return "Bachelor";
        }
        else if(randNumb==1) {
            return "Master";
        }
        else {
            return "Diplom";
        }
    }


    @Test
    public void insertAndCheck() {

        String testStudiengang = getRandStudiengang();
        String testBio = getRandBio();
        String testAbschluss = getRandAbschluss();

        driver.findElementByName("profileChangeButton").sendKeys(Keys.RETURN);

        driver.findElementById("studiengang").clear();
        driver.findElementById("studiengang").sendKeys(testStudiengang);
        Select objSelect = new Select(driver.findElement(By.id("abschluss")));
        objSelect.selectByVisibleText(testAbschluss);
        driver.findElementById("bio").clear();
        driver.findElementById("bio").sendKeys(testBio);
        driver.findElementByName("profile_change-submit-button").sendKeys(Keys.RETURN);

        Assertions.assertEquals(testStudiengang, driver.findElementByName("studiengang").getText());
        Assertions.assertEquals(testAbschluss, driver.findElementByName("abschluss").getText());
        Assertions.assertEquals(testBio, driver.findElementByName("bio").getText());

    }
}
