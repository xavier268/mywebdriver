/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.WebPage;
import com.twiceagain.mywebdriver.generators.WebPageBasic;
import org.openqa.selenium.WebDriver;

/**
 * Demo using google search to generate basic documents.
 *
 * @author xavier
 */
public class DemoGoogleNewsDumpArticles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());

        WebPage page = new WebPageBasic(wd)
                .setXpDocuments(".//div[@class='esc-body']")
                .init("http://news.google.com");

        page.processDocuments((lim, doc) -> {
            System.out.printf("\npage : %d- document : %d\n===============\n%s\n================\n",
                    lim.countPages(), lim.countDocuments(), doc);
            return true;
        });

        // Maximize page Height, then take screenshot
        // Will crash if in grid mode ... ??!!
        Drivers.adjustPageHeight(wd);

        page.addDebugOverlay();

        Drivers.screenshot2File(wd, "googleNewsTest.png");

        wd.close();

    }

}
