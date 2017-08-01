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
import com.twiceagain.mywebdriver.generators.implementations.AmazonFR;
import com.twiceagain.mywebdriver.generators.DocumentPrinter;
import com.twiceagain.mywebdriver.generators.LimiterBasic;
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
        page.setLimiter(new LimiterBasic(null, 100, 4, null));
        page.init("billard américain");        
        page.processDocuments(new DocumentPrinter());
        
        page.close();
        
    }
    
}
