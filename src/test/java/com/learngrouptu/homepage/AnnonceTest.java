package com.learngrouptu.homepage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

class AnnonceTest {

    public ChromeDriver init() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Linuxstuff/chromedriver");
            options.addArguments("--remote-debugging-port=9222");
        }
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/");
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
        driver.findElement(By.id("Nachricht")).sendKeys("testnachricht");
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


}