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
