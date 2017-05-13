/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators;

import com.twiceagain.mywebdriver.driver.web.Drivers;
import com.twiceagain.mywebdriver.limiters.Limiter;
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
 * Implementation of a crawler on multipgae website to generate selected
 * preconfigured Documents using xpath based setting. *TODO** MISSING LIMITER
 * IMPLEMENTATION !
 *
 * @author xavier
 */
public class WebPageXPathImplementation implements WebPage {

    private static final Logger LOG = Logger.getLogger(WebPageXPathImplementation.class.getName());

    protected WebDriver wd;
    /**
     * Presence of this element means page is loaded.
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
     * Limiter, or null if no limit.
     */
    protected Limiter limiter;
    /**
     * Document parser, or null to simply extract text content.
     */
    protected DocumentParser documentParser;
    /**
     * Default timeout for explicit waits.
     */
    protected int maxWaitSeconds = 5; // maximum waiting time in seconds

    //  === internal cache for performance reasons ====
    /**
     * Cache fully loaded page ?.
     */
    private boolean isLoaded = false;
    /**
     * Cache hasNextPage ?.
     */
    private boolean hasNextPage = false;
    /**
     * Internal cache for loaded elements.
     */
    private List<WebElement> loadedElements;

    /**
     * We assume page has alredy been loaded elsewhere.
     *
     * @param wd
     */
    public WebPageXPathImplementation(WebDriver wd) {
        this.wd = wd;
    }

    /**
     * Load the specified url.
     *
     * @param wd
     * @param url
     */
    public WebPageXPathImplementation(WebDriver wd, String url) {
        this.wd = wd;
        // load url
        wd.get(url);
    }

    /**
     * Add an overlay for visual debugging.
     * Ignore xpPath issues.
     * @return
     */
    @SuppressWarnings("empty-statement")
    public WebPageXPathImplementation addDebugOverlay() {
        try {
            if (xpDocuments != null) {
                Drivers.flashElements(wd, wd.findElements(By.xpath(xpDocuments)), "xpDocuments", "rgba(64,0,0,0.2)", "red");
            }
        } catch (Exception ex) {
        };

        try {
            if (xpHasNextPage != null) {
                Drivers.flashElement(wd, wd.findElement(By.xpath(xpHasNextPage)), "xpHasNextPage", "rgba(0,64,0,0.2)", "red");
            }
        } catch (Exception ex) {
        };
        try {
            if (xpNextPageClick != null) {
                Drivers.flashElement(wd, wd.findElement(By.xpath(xpNextPageClick)), "xpNextPageClick", "rgba(0,64,0,0.4)", "yellow");
            }
        } catch (Exception ex) {
        };
        try {
            if (xpPageLoadedMarker != null) {
                Drivers.flashElement(wd, wd.findElement(By.xpath(xpPageLoadedMarker)), "xpPageLoadedMarker", "rgba(20,64,10,0.2)", "green");
            }
        } catch (Exception ex) {
        };
        try {
            if (xpStalenessMarker != null) {
                Drivers.flashElement(wd, wd.findElement(By.xpath(xpStalenessMarker)), "xpStalenessMarker", "rgba(20,64,10,0.2)", "green");
            }
        } catch (Exception ex) {
        };

        return this;
    }

    public WebPageXPathImplementation setXpPageLoadedMarker(String xpPageLoadedMarker) {
        this.xpPageLoadedMarker = xpPageLoadedMarker;
        return this;
    }

    public WebPageXPathImplementation setXpHasNextPage(String xpHasNextPage) {
        this.xpHasNextPage = xpHasNextPage;
        return this;
    }

    public WebPageXPathImplementation setXpNextPageClick(String xpNextPageClick) {
        this.xpNextPageClick = xpNextPageClick;
        return this;
    }

    public WebPageXPathImplementation setXpStalenessMarker(String xpStalenessMarker) {
        this.xpStalenessMarker = xpStalenessMarker;
        return this;
    }

    public WebPageXPathImplementation setXpDocuments(String xpDocuments) {
        this.xpDocuments = xpDocuments;
        return this;
    }

    public WebPageXPathImplementation setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    public WebPageXPathImplementation setDocumentParser(DocumentParser documentParser) {
        this.documentParser = documentParser;
        return this;
    }

    public WebPageXPathImplementation setMaxWaitSeconds(int maxWaitSeconds) {
        this.maxWaitSeconds = maxWaitSeconds;
        return this;
    }

    /**
     * Waits until page is fully loaded.
     *
     * @return false on timeout.
     */
    protected boolean isPageLoaded() {
        if (isLoaded || xpPageLoadedMarker == null) {
            isLoaded = true;
            return true;
        }
        try {
            new WebDriverWait(wd, maxWaitSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpPageLoadedMarker)));
            isLoaded = true;

        } catch (Exception ex) {
            isLoaded = false;
        }
        return isLoaded;
    }

    @Override
    public boolean hasNextPage() {
        if (hasNextPage) {
            return true;
        }
        if (xpHasNextPage == null) {
            return false;
        }
        try {
            new WebDriverWait(wd, maxWaitSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpHasNextPage)));
            hasNextPage = true;
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    @Override
    public WebPage nextPage() throws NoSuchElementException {
        // clear cache
        hasNextPage = false;
        isLoaded = false;
        loadedElements = null;

        if (xpStalenessMarker == null) {
            // just try to change page
            wd.findElement(By.xpath(xpNextPageClick)).click();
        } else {
            // check for staleness of specified marker when changing page.
            WebElement smarker = wd.findElement(By.xpath(xpStalenessMarker));
            wd.findElement(By.xpath(xpNextPageClick)).click();
            new WebDriverWait(wd, maxWaitSeconds).until(ExpectedConditions.stalenessOf(smarker));
        }

        return this;
    }

    @Override
    public Document next() throws NoSuchElementException {
        if (loadedElements == null) {
            LoadDocuments();
        }
        try {
            if (documentParser == null) {
                return new Document("doctype", "text")
                        .append("content", loadedElements.remove(0).getText());
            } else {
                return documentParser.toDocument(wd, loadedElements.remove(0));
            }
        } catch (Exception ex) {
            LOG.info(ex.getMessage());
            throw new NoSuchElementException("Trying to generate a non existing Document");
        }
    }

    @Override
    public boolean hasNext() {
        if (loadedElements == null) {
            LoadDocuments();
        }
        return !(loadedElements == null || loadedElements.isEmpty());
    }

    /**
     * Load and cache webElements for documents.
     */
    protected void LoadDocuments() {
        // make sure page loded or timeout elapsed ...
        isPageLoaded();
        loadedElements = wd.findElements(By.xpath(xpDocuments));

    }

    protected boolean shouldStop() {
        return (limiter != null && limiter.shouldStop());
    }

}
