/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.WebPage;
import com.twiceagain.mywebdriver.generators.implementations.DocumentPrinter;
import com.twiceagain.mywebdriver.generators.implementations.PagesBlanches;
import com.twiceagain.mywebdriver.limiters.BasicLimiter;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author xavier
 */
public class DemoPagesBlanches {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());

        PagesBlanches page = new PagesBlanches(wd);
        page.setLimiter(new BasicLimiter(null, 1000, 6, null));
        page.init("dupont", "paris");

        page.processDocuments(new DocumentPrinter());

        wd.close();

    }

}
