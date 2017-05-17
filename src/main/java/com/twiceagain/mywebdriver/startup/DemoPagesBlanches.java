/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.DocumentPrinter;
import com.twiceagain.mywebdriver.generators.implementations.PagesBlanches;
import com.twiceagain.mywebdriver.generators.LimiterBasic;
import org.openqa.selenium.WebDriver;

/**
 * Connector to access French  phone directory assistance for individuals.
 * @author xavier
 */
public class DemoPagesBlanches {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());

        PagesBlanches page = new PagesBlanches(wd);
        page.setLimiter(new LimiterBasic(null, 200, 6, null));
        page.init("dupont", "sceaux");

        page.processDocuments(new DocumentPrinter());

        wd.close();

    }

}
