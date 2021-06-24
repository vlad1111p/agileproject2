package com.learngrouptu.homepage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VorlesungUbersichtTest {
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
        VorlesungUbersichtTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        VorlesungUbersichtTest.connection = connection;
        register();
        login();
    }

    private static void login() {
        driver.get("http://localhost:8080/login.html");
        driver.findElementById("loginModal").click();
        WebDriverWait wait=new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOf(driver.findElementByName("username")));
        driver.findElement(By.name("username")).sendKeys("testuser");
        driver.findElement(By.name("password")).sendKeys("testpassword");
        driver.findElement(By.name("login-button")).click();
    }

    private static void register() {
        driver.get("http://localhost:8080/login.html");
        driver.findElement(By.name("register-button")).click();
        driver.findElement(By.name("username")).sendKeys("testuser");
        driver.findElementByName("password").sendKeys("testpassword");
        driver.findElementByName("email").sendKeys("testmail");
        driver.findElementByName("register-submit-button").sendKeys(Keys.RETURN);
    }

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080/vorlesungsubersicht");
    }

    @AfterAll
    public static void close() throws SQLException {
        driver.close();
        connection.close();
    }

    @Test
    public void verifyTitle() {
        String title = driver.getTitle();
        assertTrue(title.contains("Vorlesungubersicht"));
    }

//    ToDo
//    @Test
//    public void verifyGruppeerstellen() {
//        ChromeDriver driver = init();
//        System.out.println(driver.getPageSource());
//        JavascriptExecutor jse = (JavascriptExecutor)driver;
//        jse.executeScript("window.scrollBy(0,250)");
//        driver.findElement(By.linkText("Gruppe erstellen")).click();
//
//        assertEquals("Annonce erstellen", driver.getTitle());
//        System.out.println("title of page is: " + driver.getTitle());
//        driver.close();
//    }

    @Test
    public void verifySuchfunktionWrongInput() {
        driver.findElement(By.id("VorlName")).sendKeys("vlad1111p@sd123gmail.com");
        driver.findElement(By.id("button1")).submit();


        assertEquals("Vorlesungubersicht", driver.getTitle());
        System.out.println("title of page is: " + driver.getTitle());
    }
    //TODO Daten nach Test aus der DB löschen
    /*
    @Test
    public void verifySuchfunktionCorrectInput() {
        ChromeDriver driver = init();
        driver.findElement(By.id("VorlName")).sendKeys("hjkl;");
        driver.findElement(By.id("button1")).click();
        driver.findElement(By.partialLinkText("Gruppe suchen")).click();

        assertEquals("Annoncen einsehen", driver.getTitle());
        System.out.println("title of page is: " + driver.getTitle());

    }*/

    @Test
    public void verifGruppesucheneWrongInput() {

        driver.findElement(By.id("VorlName")).sendKeys("vlad1111p@sd123gmail.com");
        driver.findElement(By.id("button1")).submit();
        String actualHeading = driver.findElement(By.id("textforwrong")).getText();
        assertEquals("Hast du deine Vorlesung nicht gefunden? Füge die Vorlesung hinzu.", actualHeading);
        System.out.println("title of page is: " + driver.getTitle());
    }
//
    @Test
    public void testDBinsertWorks() throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        String sqlStatement = "INSERT INTO vorlesung (titel, kursnr, studiengang, cp) VALUES ('math', 'math2', 'Sozioinformatik', '9')";
        String sqlStatement2 = "SELECT titel, kursnr, studiengang, cp FROM vorlesung WHERE titel='math' AND kursnr ='math2' AND studiengang = 'Sozioinformatik' AND cp='9'";
        Statement insStmt = connection.createStatement();
        Statement stmt = connection.createStatement();
        insStmt.executeUpdate(sqlStatement);
        ResultSet rs = stmt.executeQuery(sqlStatement2);

        boolean present = false;

        while (rs.next()) {
            String dbVorl = (rs.getString("titel"));
            String dbTyp = (rs.getString("kursnr"));
            String dbKontakt = (rs.getString("studiengang"));
            String dbNachricht = (rs.getString("cp"));
            if (dbVorl.equals("math") && dbTyp.equals("math2") && dbKontakt.equals("Sozioinformatik") && dbNachricht.equals("9")) {
                System.out.println("Testmatch succ");
                present = true;
            }
        }
        String sqlDel = "DELETE FROM vorlesung WHERE titel='math' AND kursnr ='math2' AND studiengang = 'Sozioinformatikt' AND cp='9'";
        Statement delStmt = connection.createStatement();
        delStmt.executeUpdate(sqlDel);
        assertEquals(present, true);
    }

}
