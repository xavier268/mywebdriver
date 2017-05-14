/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Short demo, used to test connectivity with local firefox.
 *
 * @author xavier
 */
public class DemoNoGrid {

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());
        wd.get("https://www.google.com");
        System.out.printf("\nSaved file to : %s\n", Drivers.screenshot2File(wd, "local_FullPageGoogle.png"));

        WebElement we = wd.findElement(By.id("hplogo"));
        System.out.printf("\nSaved file to : %s\n", Drivers.screenshot2File(wd, we, "local_LogoGoogle.png"));

        wd.quit();
    }

}
