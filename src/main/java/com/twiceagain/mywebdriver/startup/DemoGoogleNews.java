/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.DocumentPrinter;
import com.twiceagain.mywebdriver.generators.LimiterBasic;
import com.twiceagain.mywebdriver.generators.implementations.GoogleNewsFR;
import java.time.LocalDate;
import org.openqa.selenium.WebDriver;

/**
 * Demo Google News connector.
 *
 * @author xavier
 */
   @Deprecated
public class DemoGoogleNews {

    /**
     * Create a GoogleNews connector and test it.
     *
     * @param args the command line arguments
     */
 
    public static void main(String[] args) {
        // create
        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());
        GoogleNewsFR page = new GoogleNewsFR(wd);

        // define options
        page.setLimiter(new LimiterBasic(null, 100, 5, null));

        page.setNoneOfTheseWords("Hollande");
        page.setMaxDate(LocalDate.now().minusDays(10));

        // launch search       
        page.init("Président de la république");

        // dump article on stout
        page.processDocuments(new DocumentPrinter());

        // close page & driver
        page.close();
    }

}
