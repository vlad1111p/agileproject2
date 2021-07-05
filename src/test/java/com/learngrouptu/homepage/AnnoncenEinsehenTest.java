package com.learngrouptu.homepage;


import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;



public class AnnoncenEinsehenTest
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
        driver.get("http://localhost:8080/annoncenEinsehen");
        AnnoncenEinsehenTest.driver = driver;

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        AnnoncenEinsehenTest.connection = connection;
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

    @BeforeEach
    public void reset() {
        driver.get("http://localhost:8080/annonceEinsehen");
    }



//    @Test
//    public void verifyTitle()
//    {
//        String title = driver.getTitle();
//        assertTrue(title.contains("Annoncen einsehen"));
//    }

//    @Test
//    public void verifyHeading()
//    {
//        String expectedHeading = "Annoncen";
//        String actualHeading = driver.findElement(By.xpath("/html/body/div[@id='content']/div/div/h2")).getText();
//        assertEquals(expectedHeading, actualHeading);
//    }
/*
TODO
    @Test
    public void testDBpresentationWorks() throws SQLException

    {

        Connection connection = DriverManager.getConnection("jdbc:sqlite:LearngroupTU.db");
        String sqlStatement = "INSERT INTO annonce (choice, kontakt, nachricht, vorl_name) VALUES ('LerngruppeTest', 'test@uni-kl.de', 'Testnachricht', 'Testvorlesung')";
        Statement insStmt = connection.createStatement();
        insStmt.executeUpdate(sqlStatement);

        driver.navigate().refresh();

        String realContent = driver.getPageSource();
        Assertions.assertTrue(realContent.contains("LerngruppeTest"));
        Assertions.assertTrue(realContent.contains("test@uni-kl.de"));
        Assertions.assertTrue(realContent.contains("Testnachricht"));
        Assertions.assertTrue(realContent.contains("Testvorlesung"));
        System.out.println("Sample data was successfully matched");

        String sqlDel = "DELETE FROM annonce WHERE choice='LerngruppeTest' AND kontakt ='test@uni-kl.de' AND nachricht = 'Testnachricht' AND vorl_name='Testvorlesung'";
        Statement delStmt = connection.createStatement();
        delStmt.executeUpdate(sqlDel);
    }
*/

    @Test
    public void testDBinsertWorks() throws SQLException
    {

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
                System.out.println("Sample data was successfully inserted");
                present = true;
            }
        }
        String sqlDel = "DELETE FROM annonce WHERE choice='LerngruppeTest' AND kontakt ='test@uni-kl.de' AND nachricht = 'Testnachricht' AND vorl_name='Testvorlesung'";
        Statement delStmt = connection.createStatement();
        delStmt.executeUpdate(sqlDel);

        assertEquals(present, true);
    }

    @AfterAll
    public static void deleteTestDebrisAndClose() throws SQLException {
        connection.close();
    }



}