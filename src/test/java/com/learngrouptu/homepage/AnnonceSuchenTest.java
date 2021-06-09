package com.learngrouptu.homepage;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.support.ui.Select;

import javax.xml.xpath.XPath;
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

    @Test
    public void checkIfIncompleteSearchFindsResults(){
        driver.findElement(By.id("vorlName")).sendKeys("Test");
        driver.findElement(By.id("button1")).submit();
        boolean ifCorrect = driver.getPageSource().contains("Testvorlesung") && driver.getPageSource().contains("Testkontakt")
                && driver.getPageSource().contains("Testnachricht");
        Assertions.assertTrue(ifCorrect);
    }

    /*@Test
    public void checkIfChoiceButtonWorksCorrectly(){
        Select objSelect = new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("button1")).submit();
        XPath = "//*[@class = 'tableRow']";
        boolean ifCorrect = (driver.findElementsByXPath("Übungsgruppe").stream().count()==4);
        Assertions.assertTrue(ifCorrect);
    }*/
/*
    @Test
    public void checkIfUserInputIsForwarded(){
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        driver.findElement(By.id("vorlName")).sendKeys("Testvorlesung"+ rand);
        driver.findElement(By.id("button1")).submit();
        WebElement link = driver.findElementByLinkText('Annonce erstellen');
        link.click();
        driver.findElement(By.id("kontakt")).sendKeys("Testkontakt");
        driver.findElement(By.id("button1")).submit();
        boolean ifCorrect = driver.getPageSource().contains("Testvorlesung"+rand) && driver.getPageSource().contains("Testkontakt");
        Assertions.assertTrue(ifCorrect);
    }

 */
}
