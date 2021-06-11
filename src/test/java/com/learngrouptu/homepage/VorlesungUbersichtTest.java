package com.learngrouptu.homepage;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VorlesungUbersichtTest {
    private static ChromeDriver driver;
    private static Connection connection;




    public ChromeDriver init() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/vorlesungsubersicht");
        return driver;
    }

    @Test
    public void verifyTitle() {
        ChromeDriver driver = init();
        String title = driver.getTitle();
        assertTrue(title.contains("Vorlesungubersicht"));
        driver.close();
    }

    @Test
    public void verifyHeading() {
        ChromeDriver driver = init();
        String expectedHeading = "Vorlesungsübersicht";
        //String actualHeading = driver.findElement(By.id("heading")).getText();
        String actualHeading = driver.findElement(By.xpath("/html/body/div[@id='content']/div/div/h2")).getText();
        assertEquals(expectedHeading, actualHeading);
        driver.close();
    }
    @Test
    public void verifyGruppeerstellen() {
        ChromeDriver driver = init();

        driver.findElement(By.linkText("Gruppe erstellen")).click();

        assertEquals("Annonce erstellen", driver.getTitle());
        System.out.println("title of page is: " + driver.getTitle());
        driver.close();
    }

    @Test
    public void verifySuchfunktionWrongInput() {
        ChromeDriver driver = init();
        driver.findElement(By.id("VorlName")).sendKeys("vlad1111p@sd123gmail.com");
        driver.findElement(By.id("button1")).click();


        assertEquals("Vorlesungubersicht", driver.getTitle());
        System.out.println("title of page is: " + driver.getTitle());
        driver.close();
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
        ChromeDriver driver = init();

        driver.findElement(By.id("VorlName")).sendKeys("vlad1111p@sd123gmail.com");
        driver.findElement(By.id("button1")).click();
        String actualHeading = driver.findElement(By.id("textforwrong")).getText();
        assertEquals("Hast du deine Vorlesung nicht gefunden? Füge die Vorlesung hinzu.", actualHeading);
        System.out.println("title of page is: " + driver.getTitle());
        driver.close();
    }
//
    @Test
    public void testDBinsertWorks() throws SQLException {
        ChromeDriver driver = init();

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

        connection.close();
        driver.close();
        assertEquals(present, true);
    }

}
