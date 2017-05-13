/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.demos;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author xavier
 */
public class DemoGoogleNewsVisualizeElements {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        WebDriver wd = Drivers.getDriver();
        wd.get("https://news.google.com");
        //Thread.sleep(2000);
        List<WebElement> lwe = wd.findElements(By.xpath(".//div[@class='esc-body']"));
        Drivers.flashElements(wd, lwe ,"article");    
        Thread.sleep(5000);// wait 5 secs
        wd.close();
    }
    
}
