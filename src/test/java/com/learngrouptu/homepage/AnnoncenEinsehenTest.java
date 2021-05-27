package com.learngrouptu.homepage;

import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class AnnoncenEinsehenTest
{

    public ChromeDriver init()
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux"))
        {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/annonceEinsehen");
        return driver;
    }

    @Test
    public void verifyTitle()
    {
        ChromeDriver driver = init();
        String title = driver.getTitle();
        assertTrue(title.contains("Annoncen einsehen"));
    }

    @Test
    public void verifyHeading()
    {
        ChromeDriver driver = init();
        String expectedHeading = "Users";
        //String actualHeading = driver.findElement(By.id("heading")).getText();
        String actualHeading = driver.findElement(By.xpath("/html/body/div[@id='content']/div/div/h2")).getText();
        assertEquals(expectedHeading, actualHeading);
    }

    @Test
    public void testDBinsertWorks() throws SQLException
    {
        ChromeDriver driver = init();

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        String sqlStatement = "INSERT INTO annonce (choice, kontakt, nachricht, vorl_name) VALUES ('LerngruppeTest', 'test@uni-kl.de', 'Testnachricht', 'Testvorlesung')";
        String sqlStatement2 = "SELECT choice, kontakt, nachricht, vorl_name FROM annonce WHERE choice='LerngruppeTest' AND kontakt ='test@uni-kl.de' AND nachricht = 'Testnachricht' AND vorl_name='Testvorlesung'";
        Statement insStmt = connection.createStatement();
        Statement stmt = connection.createStatement();
        insStmt.executeUpdate(sqlStatement);
        ResultSet rs = stmt.executeQuery(sqlStatement2);

        boolean present = false;

        while (rs.next())
        {
            String dbVorl = (rs.getString("vorl_name"));
            String dbTyp = (rs.getString("choice"));
            String dbKontakt = (rs.getString("kontakt"));
            String dbNachricht = (rs.getString("nachricht"));
            if (dbVorl.equals("Testvorlesung") && dbTyp.equals("LerngruppeTest") && dbKontakt.equals("test@uni-kl.de") && dbNachricht.equals("Testnachricht"))
            {
                System.out.println("Testmatch succ");
                present = true;
            }
        }
        String sqlDel = "DELETE FROM annonce WHERE choice='LerngruppeTest' AND kontakt ='test@uni-kl.de' AND nachricht = 'Testnachricht' AND vorl_name='Testvorlesung'";
        Statement delStmt = connection.createStatement();
        delStmt.executeUpdate(sqlDel);

        connection.close();
        driver.close();
        assertEquals(present, true);
    }




    //TODO Darstellungstest nach insert driver.findElem(ByClass) matched nur 1



}