/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.DocumentPrinter;
import com.twiceagain.mywebdriver.generators.LimiterBasic;
import com.twiceagain.mywebdriver.generators.implementations.GoogleFR;
import java.time.LocalDate;
import org.openqa.selenium.WebDriver;

/**
 * Demo Google Web search
 *
 * @author xavier
 */
   
public class DemoGoogleFRNews {

    /**
     * Create a Google connector and test it.
     *
     * @param args the command line arguments
     */ 
    public static void main(String[] args) {
        // create
        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());
        GoogleFR page = new GoogleFR(wd);

        // define options
        page.setLimiter(new LimiterBasic(null, 100, 5, null));
        page.setSearchMode(GoogleFR.Mode.NEWS);
        page.setDateLimites(LocalDate.now().minusYears(5), LocalDate.now().minusYears(4));
        
        // launch search       
        page.init("Hollande");

        // dump article on stout
        page.processDocuments(new DocumentPrinter());

        // close page & driver
        page.close();
    }

}