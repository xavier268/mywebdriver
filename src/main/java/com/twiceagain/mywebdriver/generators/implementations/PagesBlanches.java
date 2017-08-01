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
package com.twiceagain.mywebdriver.generators.implementations;

import com.twiceagain.mywebdriver.generators.WebPageBasic;
import java.util.function.BiFunction;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Connector for PagesBlanches.
 *
 * @author xavier
 */
public class PagesBlanches extends WebPageBasic {

    public PagesBlanches(WebDriver wd) {
        super(wd);
        xpDocuments = ".//article";
        xpHasNextPage = ".//a[@id='pagination-next']";
        xpNextPageClick = xpHasNextPage;
        xpPageLoadedMarker = String.format("(%s)[18]", xpDocuments);
        xpStalenessMarker = xpDocuments;
        documentParser = new DocumentParser();
    }

    public PagesBlanches init(String nom, String ville) {
        String url = String.format("https://www.pagesjaunes.fr/pagesblanches/recherche"
                + "?quoiqui=%s&ou=%s&proximite=0",
                nom, ville);
        init(url);
        return this;
    }

    protected class DocumentParser implements BiFunction<WebDriver, WebElement, Document> {

        @Override
        public Document apply(WebDriver wd, WebElement el) {

            if (wd == null || el == null) {
                return null;
            }

            Document doc = new Document("doctype", DocumentParser.class.getCanonicalName());

            try {
                doc.append(
                        "adresse",
                        el.findElement(By.xpath(".//div[@class='adresse-container']")).getText()
                );
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore ...
            }

            try {
                doc.append(
                        "fullname",
                        el.findElement(By.xpath("(.//h2[@class='company-name']/a)[2]")).getText()
                );
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore ...
            }

            try {
                doc.append(
                        "telephone",
                        el.findElement(By.xpath(".//div[@class='tel-zone']/strong"))
                                .getAttribute("title"));
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore ...
            }

            return doc;
        }

    }

}
