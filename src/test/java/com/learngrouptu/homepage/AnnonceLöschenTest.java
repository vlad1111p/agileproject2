package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.management.StringValueExp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnonceLöschenTest
{
    private static ChromeDriver driver;
    private static Connection connection;


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

    //TODO
    /*
    @Test
    public void canClickDeleteButton() throws SQLException
    {
        String sqlStatement = "INSERT INTO annonce (choice, kontakt, nachricht, vorl_name) VALUES ('LerngruppeTest', 'test@uni-kl.de', 'Testnachricht', 'Testvorlesung')";
        Statement insStmt = connection.createStatement();
        insStmt.executeUpdate(sqlStatement);
        driver.navigate().refresh();

        driver.findElementById("delete").click();
        String delIdURL = driver.getCurrentUrl();
        System.out.println(delIdURL);
        String [] splitString = delIdURL.split("=");
        Integer testID = Integer.valueOf(splitString[1]);
        System.out.println(testID);

        String afterDel = driver.getPageSource();
       // System.out.println(afterDel);

        System.out.println(  driver.findElementByCssSelector(".tableRow:first-child"));
        System.out.println(driver.findElementByCssSelector( ".tableRow>td" ));
    }

    @Test
    public void deletionSuccessful() throws SQLException
    {

        String realContent = driver.getPageSource();

        String sqlStatement = "INSERT INTO annonce (choice, kontakt, nachricht, vorl_name) VALUES ('LerngruppeTest', 'test@uni-kl.de', 'Testnachricht', 'Testvorlesung')";
        Statement insStmt = connection.createStatement();
        insStmt.executeUpdate(sqlStatement);
        driver.navigate().refresh();

        String afterInsert = driver.getPageSource();
        driver.findElementById("delete").click();

        driver.navigate().refresh();
        String afterDeletion = driver.getPageSource();
        boolean expected = false;
        if(realContent == afterDeletion && realContent!= afterInsert){
            expected = true;
        }
        assertEquals(expected, true);

    }*/
/* TODO

    @Test
    public void deleteCorrectAnnonce() throws SQLException
    {

        WebElement elem = driver.findElementById("ID");
        System.out.println(elem.getText());

    }*/





    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        connection.close();
        driver.close();
    }

}
