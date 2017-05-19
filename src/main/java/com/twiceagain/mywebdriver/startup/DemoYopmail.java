/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.DocumentPrinter;
import com.twiceagain.mywebdriver.generators.implementations.Yopmail;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author xavier
 */
public class DemoYopmail {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());
        WebDriver wd2 = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());
        
        Yopmail page = new Yopmail(wd, wd2) ;        
        
        page.init("maxim");
        page.processDocuments(new DocumentPrinter());
        
         page.close();
        
    }
    
}
