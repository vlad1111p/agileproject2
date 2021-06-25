package com.learngrouptu.homepage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        ChromeDriver driver = chromeDriverSetup();
        driver.get("http://localhost:8080/annonceErstellen");
        AnnonceErstellenTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        AnnonceErstellenTest.connection = connection;
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

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080/annonceErstellen");
    }

    @Test
    public void testUrlCorrectForCorrectInput() throws InterruptedException {
        createAnnonce("vlad", "vlad1111p@gmail.com", true);
        String realUrl = driver.getCurrentUrl();
        String expectedUrl = "http://localhost:8080/addannonce";

        assertEquals(expectedUrl, realUrl);
    }

    @Test
    public void testContentCorrectForCorrectInput() throws InterruptedException {
        String vorlName = getRandVorl();
        String kontakt = getRandKontakt();
        String nachricht = getRandNachricht();

        createAnnonce(vorlName, kontakt, true, nachricht);
        String realContent = driver.getPageSource();
        System.out.println(driver.getCurrentUrl());
        assertTrue(realContent.contains(vorlName));
        assertTrue(realContent.contains(kontakt));
        assertTrue(realContent.contains(nachricht));

    }

    @Test
    public void testUrlCorrectForWrongInput() throws InterruptedException {
        String before = driver.getCurrentUrl();
        createAnnonce("", "vlad1111p@gmail.com", true);
        String after = driver.getCurrentUrl();
        assertEquals(before, after);
    }

    @Test
    public void testUrlCorrectForWrongAndCorrectInput() throws InterruptedException {
        String before = driver.getCurrentUrl();
        createAnnonce("", "vlad1111p@gmail.com", true);
        String after = driver.getCurrentUrl();

        assertEquals(before, after);

        createAnnonce("vlad", "", true, "testnachricht");
        after = driver.getCurrentUrl();
        assertNotEquals(before, after);
    }

    @Test
    public void testDBSuccessfullyInserted() throws SQLException {
        createAnnonce("vlad", "vlad1111p@gmail.com", true, "testnachricht");
        boolean present = isEntryInDB("vlad", "vlad1111p@gmail.com", "testnachricht");
        assertEquals(present, true);
    }

    @Test
    public void testCorrectDateInDatabase() throws SQLException {
        String vorlName = getRandVorl();
        String kontakt = getRandKontakt();
        String nachricht = getRandNachricht();
        createAnnonce(vorlName, kontakt, true, nachricht);
        boolean present = isEntryInDBWithCorrectDate(vorlName, kontakt, nachricht);
        assertEquals(present, true);
    }

    private void createAnnonce(String vorlesung, String kontakt, boolean choice) throws InterruptedException { // choice -> true = Lerngruppe | false = Uebungsgruppe
        driver.findElement(By.id("vorlName")).sendKeys(vorlesung);
        driver.findElement(By.id("kontakt")).sendKeys(kontakt);
        Select objSelect = new Select(driver.findElement(By.name("choice")));
        if (choice) {
            objSelect.selectByVisibleText("Lerngruppe");
        } else {
            objSelect.selectByVisibleText("Übungsgruppe");
        }
        driver.findElement(By.name("submit-button")).sendKeys(Keys.RETURN);
    }

    private void createAnnonce(String vorlName, String kontakt, boolean choice, String nachricht) {
        driver.findElement(By.id("vorlName")).sendKeys(vorlName);
        driver.findElement(By.id("kontakt")).sendKeys(kontakt);
        Select objSelect = new Select(driver.findElement(By.name("choice")));
        if (choice) {
            objSelect.selectByVisibleText("Lerngruppe");
        } else {
            objSelect.selectByVisibleText("Übungsgruppe");
        }
        driver.findElement(By.name("nachricht")).sendKeys(nachricht);
        driver.findElement(By.name("submit-button")).sendKeys(Keys.RETURN);
    }

    private String getRandKontakt() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "vlad" + rand;
    }

    private String getRandNachricht() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "testnachricht" + rand;
    }

    private String getRandVorl() {
        Double randNumb = Math.random();
        String rand = randNumb.toString().substring(2, 6);
        return "vlad" + rand;
    }

    private boolean isEntryInDB(String vorlName, String kontakt, String nachricht) throws SQLException {
        String sqlStatement = "SELECT vorl_name, choice, kontakt, nachricht FROM annonce";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        boolean present = false;
        while (rs.next()) {
            String dbVorl = (rs.getString("vorl_name"));
            String dbTyp = (rs.getString("choice"));
            String dbKontakt = (rs.getString("kontakt"));
            String dbNachricht = (rs.getString("nachricht"));
            if (dbVorl.equals(vorlName) && dbTyp.equals("Lerngruppe") && dbKontakt.equals(kontakt) && dbNachricht.equals(nachricht)) {
                present = true;
            }
        }
        return present;
    }

    private boolean isEntryInDBWithCorrectDate(String vorlName, String kontakt, String nachricht) throws SQLException {
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
        return present;
    }


}