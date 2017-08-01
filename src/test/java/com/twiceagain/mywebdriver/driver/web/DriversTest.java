/*
 * MIT License
 * 
 * Copyright (c) 2017 Xavier Gandillot
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.twiceagain.mywebdriver.driver.web;

import com.twiceagain.mywebdriver.driver.web.Drivers.Config;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public static void setUpClass() throws IOException {
        Path path;
        // Cleanup previous tests generated files.
        Files.deleteIfExists(Paths.get("gridNoImage_FullPageGoogle.png"));
        Files.deleteIfExists(Paths.get("gridNoImage_LogoGoogle.png"));
        Files.deleteIfExists(Paths.get("grid_FullPageGoogle.png"));
        Files.deleteIfExists(Paths.get("grid_LogoGoogle.png"));
        Files.deleteIfExists(Paths.get("local_FullPageGoogle.png"));
        Files.deleteIfExists(Paths.get("local_LogoGoogle.png"));
        
        
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
