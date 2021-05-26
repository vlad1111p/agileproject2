package com.learngrouptu.homepage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class AnnonceTest {

    /*

    public ChromeDriver init() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/annonceErstellen");
        return driver;
    }

    @Test
    public void testUrlCorrectForCorrectInput() throws InterruptedException {
        ChromeDriver driver = init();
        driver.findElement(By.id("vorlName")).sendKeys("vlad");
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("button1")).click();
        String realUrl=driver.getCurrentUrl();
        String expectedUrl="http://localhost:8080/annonce_created?vorlName=vlad&choice=Lerngruppe&kontakt=vlad1111p%40gmail.com&Nachricht=";

        assertEquals(expectedUrl, realUrl);
        driver.close();
    }
    @Test
    public void testContentCorrectForCorrectInput() throws InterruptedException {
        ChromeDriver driver = init();
        driver.findElement(By.id("vorlName")).sendKeys("vlad");
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("nachricht")).sendKeys("testnachricht");
        driver.findElement(By.id("button1")).click();
        String realContent=driver.getPageSource().toString();
        String expectedContent="vlad Lerngruppe vlad1111p@gmail.com testnachricht";
        Assertions.assertTrue(realContent.contains(expectedContent));
        driver.close();
    }
    @Test
    public void testUrlCorrectForWrongInput() throws InterruptedException {
        ChromeDriver driver = init();
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        String before=driver.getCurrentUrl();
        driver.findElement(By.id("button1")).click();
        String after=driver.getCurrentUrl();
        assertEquals(before, after);
        driver.close();
    }

    @Test
    public void testUrlCorrectForWrongAndCorrectInput() throws InterruptedException {
        ChromeDriver driver = init();
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");

        String before=driver.getCurrentUrl();
        driver.findElement(By.id("button1")).click();
        String after=driver.getCurrentUrl();

        assertEquals(before, after);
        driver.findElement(By.id("vorlName")).sendKeys("vlad");
        driver.findElement(By.id("button1")).click();
        after=driver.getCurrentUrl();
        assertNotEquals(before,after);
        driver.close();
    }

    @Test
    public void testDBSuccessfullyInserted() throws SQLException {
        ChromeDriver driver = init();

        driver.findElement(By.id("vorlName")).sendKeys("vlad");
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("Nachricht")).sendKeys("testnachricht");
        driver.findElement(By.id("button1")).click();

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU_fin.db");
        String sqlStatement = "SELECT vorl_name, choice, kontakt, nachricht FROM annonce";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        boolean present = false;
        while (rs.next()) {
                String dbVorl = (rs.getString("vorl_name"));
                String dbTyp = (rs.getString("choice"));
                String dbKontakt = (rs.getString("kontakt"));
                String dbNachricht = (rs.getString("nachricht"));
                if (dbVorl.equals("vlad") && dbTyp.equals("Lerngruppe") && dbKontakt.equals("vlad1111p@gmail.com") && dbNachricht.equals("testnachricht")) {
                    present = true;
                }
        }
        connection.close();
        driver.close();
        assertEquals(present, true);
    }

    @AfterAll
    public static void deleteTestDebris() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU_fin.db");
        String sqlStatement = "SELECT vorl_name, choice, kontakt, nachricht FROM annonce";
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM annonce WHERE kontakt='vlad1111p@gmail.com'");
        /*sqlStatement = "SELECT vorl_name, Typ, Kontakt, Nachricht FROM annonce";
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        while (rs.next()) {
            System.out.println(rs.getString("Vorlesung"));
            System.out.println(rs.getString("Typ"));
            System.out.println(rs.getString("Kontakt"));
            System.out.println(rs.getString("Nachricht"));
            System.out.println();
        }
        connection.close();

    }
*/

}