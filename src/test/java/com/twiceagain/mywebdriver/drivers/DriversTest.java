/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.drivers;

import com.twiceagain.mywebdriver.drivers.Drivers.Config;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Basic tests.
 *
 * @author xavier
 */
public class DriversTest {

    public DriversTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void loadQuickJava() throws IOException {

        Drivers.Config.loadFirefoxQuickJavaExtension();
        Drivers.Config.loadFirefoxQuickJavaExtension();
        Drivers.Config.loadFirefoxQuickJavaExtension();

    }

    /**
     * Make sure Firefox can run locally and Geckoriver is loaded and configured
     * in the given path.
     */
    @Test
    public void testLocalDefaultFirefox() {
        
        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());
        System.out.printf("\nOpen page (no grid) : %s\n", openPage(wd));
        wd.quit();
    }

    /**
     * Make sure grid is running on the correct url ?
     */
    @Test
    public void testGridDefaultFirefox() {     
        
        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultGridFirefox());
        System.out.printf("\nOpen page (grid) : %s\n", openPage(wd));
        wd.quit();
    }
    
    @Test
    public void testScreenshotLocal() {
        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());
        System.out.printf("\nOpen page (local) : %s\n", openPage(wd));
        System.out.printf("\nSaved file to : %s\n", Drivers.screenshot2File(wd, "local_FullPageGoogle.png"));
        
        WebElement we = wd.findElement(By.id("hplogo"));               
        System.out.printf("\nSaved file to : %s\n", Drivers.screenshot2File(wd, we, "local_LogoGoogle.png"));
        
        wd.quit();
    }
    
    @Test
    public void testScreenshotGrid() {
        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultGridFirefox());
        System.out.printf("\nOpen page (grid) : %s\n", openPage(wd));
        System.out.printf("\nSaved file to : %s\n", Drivers.screenshot2File(wd, "grid_FullPageGoogle.png"));
        
        WebElement we = wd.findElement(By.id("hplogo"));               
        System.out.printf("\nSaved file to : %s\n", Drivers.screenshot2File(wd, we, "grid_LogoGoogle.png"));
        
        wd.quit();
    }
    @Test
    public void testScreenshotGridNoImage() {
        Config conf = Config.defaultGridFirefox();
        conf.noImage = true;
        WebDriver wd = Drivers.getDriver(conf);
        System.out.printf("\nOpen page (grid-noImage) : %s\n", openPage(wd));
        System.out.printf("\nSaved file to : %s\n", Drivers.screenshot2File(wd, "gridNoImage_FullPageGoogle.png"));
        
        WebElement we = wd.findElement(By.id("hplogo"));               
        System.out.printf("\nSaved file to : %s\n", Drivers.screenshot2File(wd, we, "gridNoImage_LogoGoogle.png"));
        
        wd.quit();
    }
    
   

    // ======================================================//
    protected String openPage(WebDriver wd) {
        wd.get("http://www.google.com");
        return wd.getTitle();
    }

}
