/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.implementations.AmazonFR;
import com.twiceagain.mywebdriver.generators.implementations.DocumentPrinter;
import com.twiceagain.mywebdriver.limiters.BasicLimiter;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author xavier
 */
public class DemoAmazonFR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Drivers.Config conf = Drivers.Config.defaultLocalFirefox();
        // conf.noImage = true;
        WebDriver wd = Drivers.getDriver(conf);
        AmazonFR page = new AmazonFR(wd);
        page.setLimiter(new BasicLimiter(null, 100, 4, null));
        page.init("billard américain");        
        page.processDocuments(new DocumentPrinter());
        
        page.close();
        
    }
    
}
