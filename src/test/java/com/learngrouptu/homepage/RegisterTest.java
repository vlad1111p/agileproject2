package com.learngrouptu.homepage;


import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterTest {

    public static final String url_homepage = "http://localhost:8080/home";
    public static final String url_login = "http://localhost:8080/login";
    public static final String url_register = "http://localhost:8080/register";
    private static ChromeDriver driver;
    private static Connection connection;

    @BeforeAll
    public static void init() throws SQLException {
        ChromeOptions options = new ChromeOptions();
        ChromeDriver driver = chromeDriverSetup(options);
        RegisterTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        RegisterTest.connection = connection;
        driver.get("http://localhost:8080/perform_logout");
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        cleanDB();
        driver.close();
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
    public void testRegisterButton() {
        driver.findElement(By.name("register-button")).click();
        assertEquals(driver.getCurrentUrl(), url_register);
    }

    @Test
    public void testSuccessfullRegisterRedirectToHome()  {
        String username = getRandUsername();
        String email = getRandEmail();
        String password = getRandPassword();

        register(username, email, password);

        login(username, password);

        assertEquals(driver.getCurrentUrl(), url_homepage);
    }

    @Test
    public void unsuccesfullRegisterCorrectMessage() {
        String username = getRandUsername();
        String email = getRandEmail();
        register(username, email, "");
        assertTrue(driver.getCurrentUrl().equals(url_register));
    }

    @Test
    public void successfullRegisterDataInDB() throws SQLException {
        String username = getRandUsername();
        String email = getRandEmail();
        String password = getRandPassword();
        register(username, email, password);
        assertEquals(isEntryInDB(username, email), true);
    }

    @Test
    public void successfullRegisterAndLoginAfterwardsWithSameCredentials()  {
        String username = getRandUsername();
        String email = getRandEmail();
        String password = getRandPassword();
        register(username, email, password);

        login(username, password);

        assertEquals(driver.getCurrentUrl(), url_homepage);

        logout();
        reset();

        login(username, password);
        driver.getCurrentUrl().equals(url_homepage);
    }

    @Test
    public void successfullRegisterAndLoginAfterwardsWithWrongPassword()  {
        String username = getRandUsername();
        String email = getRandEmail();
        String password = getRandPassword();

        register(username, email, password);

        login(username, password);

        assertEquals(driver.getCurrentUrl(), url_homepage);

        logout();
        reset();

        login(username, "wrong");
        assertTrue(driver.getCurrentUrl().startsWith(url_login));
    }

    @Test
    public void sameUsernameRegisterNotWorking() {
        String username = getRandUsername();
        String email = getRandEmail();
        String password = getRandPassword();
        register(username, email, password);
        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/login.html");

        logout();
        reset();

        email = getRandEmail();
        password = getRandPassword();
        register(username, email, password);
        assertTrue(driver.getCurrentUrl().startsWith(url_register));
        assertTrue(driver.getPageSource().toLowerCase().contains("name ist schon vergeben"));

    }

    @Test
    public void sameMailRegisterNotWorking() {
        String username = getRandUsername();
        String email = getRandEmail();
        String password = getRandPassword();
        register(username, email, password);
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:8080/login"));

        logout();
        reset();

        username = getRandUsername();
        password = getRandPassword();
        register(username, email, password);
        assertTrue(driver.getCurrentUrl().startsWith(url_register));
        assertTrue(driver.getPageSource().toLowerCase().contains("e-mail adresse ist schon vergeben"));
    }

    @Test
    public void afterUnsuccessfulRegisterNoLoginPoss()  {
        String username = getRandUsername();
        String email = getRandEmail();
        register(username, email, "");
        assertTrue(driver.getCurrentUrl().startsWith(url_register));

        logout();
        reset();

        login(username, "testpasswort");
        assertTrue(driver.getCurrentUrl().startsWith(url_login));
    }

    @Test
    public void testPasswordEncoding() throws SQLException {
        String username = getRandUsername();
        String email = getRandEmail();
        String password = getRandPassword();
        register(username, email, password);

        boolean present = isEntryWithPasswordInDB(username, email, password);
        assertEquals(present, false);
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

    private String getRandEmail() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "testuser" + rand;
    }

    private String getRandPassword() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "testuser" + rand;
    }

    private String getRandUsername() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "testuser" + rand;
    }

    private boolean isEntryInDB(String username, String email) throws SQLException {
        String sqlStatement = "SELECT username, email FROM user";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        boolean present = false;
        while (rs.next()) {
            String usernameDB = (rs.getString("username"));
            String emailDB = (rs.getString("email"));
            if (usernameDB.equals(username) && emailDB.equals(email)) {
                present = true;
            }
        }
        return present;
    }

    private boolean isEntryWithPasswordInDB(String username, String email, String password) throws SQLException {
        String sqlStatement = "SELECT username, email, password FROM user";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        boolean present = false;
        while (rs.next()) {
            String usernameDB = (rs.getString("username"));
            String emailDB = (rs.getString("email"));
            String passwordDB = (rs.getString("password"));
            if (usernameDB.equals(username) && emailDB.equals(email) && passwordDB.equals(password)) {
                present = true;
            }
        }
        return present;
    }

    private void login(String username, String password) {
        driver.get("http://localhost:8080/login.html");
        driver.findElementById("loginModal").click();
        WebDriverWait wait=new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOf(driver.findElementByName("username")));
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login-button")).click();
    }

    private void register(String username, String email, String password) {
        driver.get("http://localhost:8080/login.html");
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").sendKeys(Keys.RETURN);
    }
}
