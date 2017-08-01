/*
 * MIT License
 * 
 * Copyright (c) 2017 Xavier Gandillot
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.twiceagain.mywebdriver.startup;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.generators.WebPage;
import com.twiceagain.mywebdriver.generators.WebPageBasic;
import com.twiceagain.mywebdriver.generators.DocumentPrinter;
import org.openqa.selenium.WebDriver;

/**
 * Demo using google search to generate basic documents.
 *
 * @author xavier
 */
public class DemoBasicWebPageDumpArticles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        WebDriver wd = Drivers.getDriver(Drivers.Config.defaultLocalFirefox());

        WebPageBasic page = new WebPageBasic(wd);
        page.setXpDocuments(".//div[@class='esc-body']");
        page.init("http://news.google.com");

        page.processDocuments(new DocumentPrinter());

        // Maximize page Height, then take screenshot
        // Will crash if in grid mode ... ??!!
        Drivers.adjustPageHeight(wd);

        page.addDebugOverlay();

        Drivers.screenshot2File(wd, "googleNewsTest.png");

        wd.close();

    }

}
