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

import com.twiceagain.mywebdriver.generators.WebPage;
import com.twiceagain.mywebdriver.generators.WebPageBasic;
import java.util.function.BiFunction;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Retrieves mails from a yopmail.com mailbox. We inherit from webpagebasic to
 * firts get all the messages urls.
 *
 * @author xavier
 */
public class Yopmail extends WebPageBasic implements WebPage {

    /**
     * The inbox iframe.
     */
    protected String ifInbox = "ifinbox";
    /**
     * Alternative webdriver to get the content of the message. You should take
     * care of closing it yourself when processing is finished.
     */
    private WebDriver wd2;

    public Yopmail(WebDriver wd) {
        super(wd);
        xpDocuments = ".//a[@class='lm']";
        xpNextPageClick = ".//a[@class='igif next']";
        xpHasNextPage = xpNextPageClick;
        xpPageLoadedMarker = "(.//a[@class='lm'])[14]";
        xpStalenessMarker = xpPageLoadedMarker;
        documentParser = new DocumentParser();
    }

    public Yopmail(WebDriver wd, WebDriver wd2) {
        super(wd);
        this.wd2 = wd2;
        xpDocuments = ".//a[@class='lm']";
        xpNextPageClick = ".//a[@class='igif next']";
        xpHasNextPage = xpNextPageClick;
        xpPageLoadedMarker = "(.//a[@class='lm'])[14]";
        xpStalenessMarker = xpPageLoadedMarker;
        documentParser = new DocumentParser();
    }

    /**
     * Initialize with the provided username.
     *
     * @param user
     */
    @Override
    public void init(String user) {
        wd.get("http://www.yopmail.com?" + user);

        // Wait for message frame to appear, and switch to it
        try {
            (new WebDriverWait(wd, maxWaitSeconds)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(ifInbox));
        } catch (Exception ex) {
            LOG.warning(ex.getLocalizedMessage());
        }
        // wd now points to the inbox frame.
        // Wait for at least 1 message to appear
        try {
            (new WebDriverWait(wd, maxWaitSeconds)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpDocuments)));
        } catch (Exception ex) {
            LOG.warning(ex.getLocalizedMessage());
        }
        super.preloadElements();

    }
    /**
     * A revoir, notamment sur blocage captcha avec wd2 ?!
     * @deprecated
     */
    @Deprecated
    protected class DocumentParser implements BiFunction<WebDriver, WebElement, Document> {

        public DocumentParser() {
        }

        @Override
        public Document apply(WebDriver wd, WebElement we) {
            Document doc = new Document("doctype", DocumentParser.class.getCanonicalName());

            doc.append("url", we.getAttribute("href"));
            try {
                doc.append("object", we.findElement(By.xpath(".//span[@class='lms']")).getText());
            } catch (Exception ex) {
                LOG.fine(ex.getLocalizedMessage());
            }
            try {
                doc.append("senderName", we.findElement(By.xpath(".//span[@class='lmf']")).getText());
            } catch (Exception ex) {
                LOG.fine(ex.getLocalizedMessage());
            }
            doc.append("summary", we.getText());

            if (wd2 == null) {
                System.out.printf("\nNo further processing - no further window provided\n");
                return doc;
            }

            // Get a stalenessMarker
            WebElement sm = null;
            try {
                sm = (new WebDriverWait(wd2, maxWaitSeconds)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[@class='noprint']")));
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
            }
            // Now, we continue with the second window.
            wd2.get(doc.getString("url"));

            // Wait for the page to stabilize
            try {
                if (sm != null) {
                    (new WebDriverWait(wd2, maxWaitSeconds)).until(ExpectedConditions.stalenessOf(sm));
                }
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
            }

            (new WebDriverWait(wd2, maxWaitSeconds)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//div[@class='noprint']")));

            try {
                doc.append("fullMessageText", wd2.findElement(By.xpath(".")).getText());
            } catch (Exception ex) {
                LOG.warning(ex.getLocalizedMessage());
            }

            return doc;

        }

    }

    /**
     * Close (potentially both) drivers.
     */
    @Override
    public void close() {
        if (wd2 != null) {
            wd2.close();
        }
        super.close();
    }

}
