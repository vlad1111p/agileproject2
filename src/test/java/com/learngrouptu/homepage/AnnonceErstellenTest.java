package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class AnnonceErstellenTest {

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
        driver.get("http://localhost:8080/annonceErstellen");
        AnnonceErstellenTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        AnnonceErstellenTest.connection = connection;
    }

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080/annonceErstellen");
    }

    @Test
    public void testUrlCorrectForCorrectInput() throws InterruptedException {
        driver.findElement(By.id("vorlName")).sendKeys("vlad");
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("button1")).click();
        String realUrl=driver.getCurrentUrl();
        String expectedUrl="http://localhost:8080/addannonce";

        assertEquals(expectedUrl, realUrl);
    }
    @Test
    public void testContentCorrectForCorrectInput() throws InterruptedException {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String vorlName = "vlad" + rand;
        String kontakt = "vlad" + rand +  "@gmail.com";
        String nachricht = "testnachricht" + rand;

        driver.findElement(By.id("vorlName")).sendKeys(vorlName);
        driver.findElement(By.id("kontakt")).sendKeys(kontakt);
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("Nachricht")).sendKeys(nachricht);
        driver.findElement(By.id("button1")).click();
        String realContent=driver.getPageSource().toString();
        Assertions.assertTrue(realContent.contains(vorlName));
        Assertions.assertTrue(realContent.contains(kontakt));
        Assertions.assertTrue(realContent.contains(nachricht));

    }
    @Test
    public void testUrlCorrectForWrongInput() throws InterruptedException {
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        String before=driver.getCurrentUrl();
        driver.findElement(By.id("button1")).click();
        String after=driver.getCurrentUrl();
        assertEquals(before, after);
    }

    @Test
    public void testUrlCorrectForWrongAndCorrectInput() throws InterruptedException {
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");

        String before=driver.getCurrentUrl();
        driver.findElement(By.id("button1")).click();
        String after=driver.getCurrentUrl();

        assertEquals(before, after);
        driver.findElement(By.id("vorlName")).sendKeys("vlad");
        driver.findElement(By.id("Nachricht")).sendKeys("testnachricht");
        driver.findElement(By.id("button1")).click();
        after=driver.getCurrentUrl();
        assertNotEquals(before,after);
    }

    @Test
    public void testDBSuccessfullyInserted() throws SQLException {
        driver.findElement(By.id("vorlName")).sendKeys("vlad");
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("Nachricht")).sendKeys("testnachricht");
        driver.findElement(By.id("button1")).click();

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
        assertEquals(present, true);
    }

    @Test
    public void testCorrectDateInDatabase() throws SQLException {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        String vorlName = rand;
        String kontakt = rand;
        String nachricht = "testnachricht" + rand;
        driver.findElement(By.id("vorlName")).sendKeys(vorlName);
        driver.findElement(By.id("kontakt")).sendKeys(kontakt);
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("Nachricht")).sendKeys(nachricht);
        driver.findElement(By.id("button1")).click();

        String sqlStatement = "SELECT vorl_name, choice, kontakt, nachricht, datum FROM annonce";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        boolean present = false;
        while (rs.next()) {
            String dbVorl = (rs.getString("vorl_name"));
            String dbTyp = (rs.getString("choice"));
            String dbKontakt = (rs.getString("kontakt"));
            String dbNachricht = (rs.getString("nachricht"));
            String dbDatum = (rs.getDate("datum")).toString();
            String currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime()).toString();
            if (dbVorl.equals(vorlName) && dbTyp.equals("Lerngruppe") && dbKontakt.equals(kontakt)
                    && dbNachricht.equals(nachricht) && dbDatum.equals(currentDate)) {
                present = true;
            }
        }
        assertEquals(present, true);
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        AnnonceErstellenTest.driver.close();
        Statement stmt = connection.createStatement();
        String sqlStatement = "DELETE FROM annonce WHERE nachricht LIKE 'testnachricht%' OR kontakt = 'vlad1111p@gmail.com'";
        stmt.execute(sqlStatement);
        /*sqlStatement = "SELECT vorl_name, choice, kontakt, nachricht FROM annonce";
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        while (rs.next()) {
            System.out.println(rs.getString("vorl_name"));
            System.out.println(rs.getString("choice"));
            System.out.println(rs.getString("kontakt"));
            System.out.println(rs.getString("nachricht"));
            System.out.println();
        }*/
        connection.close();
    }


}