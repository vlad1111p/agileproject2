package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.*;

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

    @AfterEach
    public void logout() {
        driver.get("http://localhost:8080/perform_logout");
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        LoginTest.driver.close();
        Statement stmt = connection.createStatement();
        String sqlStatement = "DELETE FROM user WHERE email LIKE 'testuser%' and " +
                "username LIKE 'testuser%'";
        stmt.execute(sqlStatement);
        connection.close();
    }

    @Test
    public void testRedirectToLoginWithoutLogin() {
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:8080/login"));
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

    @Test
    public void testRegisterButton() {
        driver.findElement(By.name("register-button")).click();
        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/register");
    }

    @Test
    public void testSuccessfullRegisterRedirectToHome() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        String password = "testpassword" + rand;
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();

        login(username, password);

        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/home");
    }

    @Test
    public void unsuccesfullRegisterCorrectMessage() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:8080/register"));
        assertTrue(driver.getPageSource().toLowerCase().contains("passwort fehlt"));
    }

    @Test
    public void successfullRegisterDataInDB() throws SQLException {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        String password = "testpassword" + rand;
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();

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
        assertEquals(present, true);
    }

    @Test
    public void successfullRegisterAndLoginAfterwardsWithSameCredentials() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        String password = "testpassword" + rand;
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();

        login(username, password);

        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/home");

        logout();
        reset();

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login-button")).click();
        driver.getCurrentUrl().equals("http://localhost:8080/home");
    }

    @Test
    public void successfullRegisterAndLoginAfterwardsWithWrongPassword() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        String password = "testpassword" + rand;

        driver.findElement(By.name("register-button")).click();

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();

        login(username, password);

        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/home");

        logout();
        reset();

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys("wrong");
        driver.findElement(By.name("login-button")).click();
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:8080/login"));
    }

    @Test
    public void sameUsernameRegisterNotWorking() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        String password = "testpassword" + rand;
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();
        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/login.html");

        logout();
        reset();

        randNumb = Math.random();
        rand = randNumb.toString().substring(2, 6);
        email = "testuser" + rand +  "@gmail.com";
        password = "testpassword" + rand;
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:8080/register"));
        assertTrue(driver.getPageSource().toLowerCase().contains("name schon vergeben"));

    }

    @Test
    public void sameMailRegisterNotWorking() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        String password = "testpassword" + rand;
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();
        assertEquals(driver.getCurrentUrl(), "http://localhost:8080/login.html");

        logout();
        reset();

        randNumb = Math.random();
        rand = randNumb.toString().substring(2, 6);
        username = "testuser" + rand;
        password = "testpassword" + rand;
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:8080/register"));
        assertTrue(driver.getPageSource().toLowerCase().contains("e-mail schon vergeben"));
    }

    @Test
    public void afterUnsuccessfulRegisterNoLoginPoss() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:8080/register"));

        logout();
        reset();

        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys("testpasswort");
        driver.findElement(By.name("login-button")).click();
        assertTrue(driver.getCurrentUrl().startsWith("http://localhost:8080/login"));
    }

    @Test
    public void testPasswordEncoding() throws SQLException {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String username = "testuser" + rand;
        String email = "testuser" + rand +  "@gmail.com";
        String password = "testpassword" + rand;
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElementByName("password").sendKeys(password);
        driver.findElementByName("email").sendKeys(email);
        driver.findElementByName("register-submit-button").click();

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
        assertEquals(present, false);
    }

    private void login(String username, String password) {
        driver.get("http://localhost:8080/login.html");
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login-button")).click();
    }
}
