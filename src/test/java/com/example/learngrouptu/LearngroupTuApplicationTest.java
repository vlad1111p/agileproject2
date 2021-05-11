package com.example.learngrouptu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

class LearngroupTuApplicationTest {

    @Test
    public void testUrlCorrectForCorrectInput() throws InterruptedException {



        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/");

        driver.findElement(By.id("myVorlesung")).sendKeys("vlad");
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("button1")).click();
        String at=driver.getCurrentUrl();
        String et="http://localhost:8080/annance_created?myVorlesung=vlad&choice=Lerngruppe&kontakt=vlad1111p%40gmail.com&Nachricht=";

        assertEquals(et, at);
    }
    @Test
    public void testContentCorrectForCorrectInput() throws InterruptedException {



        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/");

        driver.findElement(By.id("myVorlesung")).sendKeys("vlad");
        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");
        Select objSelect =new Select(driver.findElement(By.id("choice")));
        objSelect.selectByVisibleText("Lerngruppe");
        driver.findElement(By.id("Nachricht")).sendKeys("testnachricht");
        driver.findElement(By.id("button1")).click();
        String at=driver.getPageSource().toString();
        String et="vlad Lerngruppe vlad1111p@gmail.com testnachricht";
        //System.out.println(at);
        Assertions.assertTrue(at.contains(et));
    }
    @Test
    public void testUrlCorrectForWrongInput() throws InterruptedException {



        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/");


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



        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        driver.get("http://localhost:8080/");


        driver.findElement(By.id("kontakt")).sendKeys("vlad1111p@gmail.com");

        String before=driver.getCurrentUrl();
        driver.findElement(By.id("button1")).click();
        String after=driver.getCurrentUrl();

        assertEquals(before, after);
        driver.findElement(By.id("myVorlesung")).sendKeys("vlad");
        driver.findElement(By.id("button1")).click();
        after=driver.getCurrentUrl();
        assertNotEquals(before,after);
    }


}