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
package com.twiceagain.mywebdriver.generators;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base structure for a multipage document processing object. You typically
 * create the WebPageBasic with an existing WebDriver, set the xpath parameters
 * and the other parameters, and finish with init(url). Subclasses should
 * redefine their own init(...) function.
 *
 *
 * @author xavier
 */
public class WebPageBasic extends WebPageAbstract implements WebPage {

    protected static final Logger LOG = Logger.getLogger(WebPageBasic.class.getName());

    /**
     * Presence of this element means page is loaded. Null means no check.
     */
    protected String xpPageLoadedMarker;
    /**
     * Presence of this element means that there is a next page. Null means not
     * next page, never ...
     */
    protected String xpHasNextPage;
    /**
     * We should click here to get the next page. Null means no next page, never
     * ....
     */
    protected String xpNextPageClick;
    /**
     * If specified, we should expect the designated elemnet to become stale
     * when changing pages. Null means ignore staleness test.
     */
    protected String xpStalenessMarker;
    /**
     * All WebElements returned by this xpath will be used to parse documents.
     */
    protected String xpDocuments = ".";
    /**
     * Limiter, defaults to LimiterBasic..
     */
    /**
     * Document parser, or null to simply extract text content.
     */
    /**
     * Default timeout for explicit waits.
     */
    // maximum waiting time in seconds


    //  === internal cache for performance reasons ====
    /**
     * Internal cache for loaded elements.
     */
    protected List<WebElement> loadedElements = null;

    public WebPageBasic(WebDriver wd) {
        super(wd);
    }

    /**
     * Preload elements from current page, or any other initialisation required
     * for each new page.
     *
     * @return - this page.
     */
    protected WebPageBasic preloadElements() {

        limiter.incPage();
        //System.out.printf("\nDEBUG : preloading elements, page = \n");

        // wait for pageLoaded criteria
        if (xpPageLoadedMarker != null) {
            try {
                (new WebDriverWait(wd, maxWaitSeconds))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpPageLoadedMarker)));
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore ..
            }
        }

        // We try to load documents
        loadedElements = null;
        try {
            loadedElements = wd.findElements(By.xpath(xpDocuments));
        } catch (Exception ex) {
            loadedElements = null;
            LOG.info(ex.getLocalizedMessage());

        }
        return this;

    }

    /**
     * Tries to move to next page. If no next page, return false.
     *
     * @return true if next page sucessfully loaded.
     */
    @Override
    public boolean goToNextPage() {
        if (limiter.shouldStop()) {
            return false;
        }

        // When pages never have next pages.
        if (xpNextPageClick == null) {
            loadedElements = null;
            return false;
        }

        // check for hasNextPage, if it is defined
        if (xpHasNextPage != null) {
            try {
                wd.findElement(By.xpath(xpHasNextPage));
            } catch (Exception ex) {
                loadedElements = null;
                LOG.info(ex.getLocalizedMessage());
                return false;
            }
        }

        // Identify staleness marker
        WebElement stale = null;
        if (xpStalenessMarker != null) {
            try {
                stale = wd.findElements(By.xpath(xpStalenessMarker)).get(0);
            } catch (Exception ex) {
                stale = null;
                LOG.info(ex.getLocalizedMessage());
                // ignore ...
            }
        }

        // try to click for goToNextPage
        if (xpNextPageClick != null) {
            try {
                wd.findElement(By.xpath(xpNextPageClick)).click();
            } catch (Exception ex) {
                loadedElements = null;
                LOG.info(ex.getLocalizedMessage());
                return false;
            }
        }

        // Wait for stalenessmarker to become stale.
        if (stale != null) {
            try {
                (new WebDriverWait(wd, maxWaitSeconds))
                        .until(ExpectedConditions.stalenessOf(stale));
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore ...
            }
        }

        preloadElements();
        return loadedElements != null;
    }

    @Override
    public Document next() {

        if (limiter.shouldStop()) {
            throw new NoSuchElementException();
        }

        if (hasNext()) {
            WebElement e = loadedElements.remove(0);
            limiter.incDocument();
            if (documentParser == null) {
                return new Document("doctype", "text")
                        .append("content", e.getText());
            } else {
                return documentParser.apply(wd, e);
            }
        } else {
            throw new NoSuchElementException();
        }

    }

    @Override
    public boolean hasNext() {

        // Someting on this page ?
        if (loadedElements != null && !loadedElements.isEmpty()) {
            return true;
        }

        // Load next page and iterate ...
        if (goToNextPage()) {
            return hasNext();
        } else {
            return false;
        }

    }


    /**
     * Add an overlay for visual debugging. Ignore xpPath issues.
     *
     * @return
     */
    @Override
    public WebPageBasic addDebugOverlay() {
        try {
            if (xpDocuments != null) {
                Drivers.highlightElements(wd, wd.findElements(By.xpath(xpDocuments)), "xpDocuments", "rgba(64,0,0,0.2)", "red");
            }
        } catch (Exception ex) {
            LOG.info(ex.getLocalizedMessage());

        }

        try {
            if (xpHasNextPage != null) {
                Drivers.highlightElement(wd, wd.findElement(By.xpath(xpHasNextPage)), "xpHasNextPage", "rgba(0,64,0,0.2)", "red");
            }
        } catch (Exception ex) {
            LOG.info(ex.getLocalizedMessage());
        }
        try {
            if (xpNextPageClick != null) {
                Drivers.highlightElement(wd, wd.findElement(By.xpath(xpNextPageClick)), "xpNextPageClick", "rgba(0,64,0,0.4)", "yellow");
            }
        } catch (Exception ex) {
            LOG.info(ex.getLocalizedMessage());
        }
        try {
            if (xpPageLoadedMarker != null) {
                Drivers.highlightElement(wd, wd.findElement(By.xpath(xpPageLoadedMarker)), "xpPageLoadedMarker", "rgba(20,64,10,0.2)", "green");
            }
        } catch (Exception ex) {
            LOG.info(ex.getLocalizedMessage());
        }
        try {
            if (xpStalenessMarker != null) {
                Drivers.highlightElement(wd, wd.findElement(By.xpath(xpStalenessMarker)), "xpStalenessMarker", "rgba(20,64,10,0.2)", "green");
            }
        } catch (Exception ex) {
            LOG.info(ex.getLocalizedMessage());
        }

        return this;
    }


    public void setXpPageLoadedMarker(String xpPageLoadedMarker) {
        this.xpPageLoadedMarker = xpPageLoadedMarker;
    }

    public void setXpHasNextPage(String xpHasNextPage) {
        this.xpHasNextPage = xpHasNextPage;
    }

    public void setXpNextPageClick(String xpNextPageClick) {
        this.xpNextPageClick = xpNextPageClick;
    }

    public void setXpStalenessMarker(String xpStalenessMarker) {
        this.xpStalenessMarker = xpStalenessMarker;
    }

    public void setXpDocuments(String xpDocuments) {
        this.xpDocuments = xpDocuments;
    }


    @Override
    public void init(String url) {
        wd.get(url);
        preloadElements();
    }

}
