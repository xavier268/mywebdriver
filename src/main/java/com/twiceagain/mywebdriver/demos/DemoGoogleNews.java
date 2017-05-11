/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.demos;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.DocumentIterator;
import com.twiceagain.mywebdriver.generators.WebPage;
import com.twiceagain.mywebdriver.generators.WebPageXPathImplementation;
import org.openqa.selenium.WebDriver;

/**
 * Demo using google search to genarete basic documents.
 * @author xavier
 */
public class DemoGoogleNews {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        WebDriver wd = Drivers.getDriver();
        WebPage page = new WebPageXPathImplementation(wd, "http://news.google.com")
                .setXpDocuments(".//div[@class='esc-body']");
        DocumentIterator di = new DocumentIterator(page);while(di.hasNext()) {
            System.out.printf("\n%s\n", di.next().toString());
        }
        wd.close();
        
    }
    
}
