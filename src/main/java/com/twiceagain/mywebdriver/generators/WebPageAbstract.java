/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators;

import java.util.function.BiFunction;
import org.bson.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Abstract generic WebPage implementation.
 * Implements everything that is not specific to the page structure itself.
 * @author xavier
 */
public abstract class WebPageAbstract implements WebPage {
    
    protected WebDriver wd;
    /**
     * Limiter, defaults to LimiterBasic..
     */
    protected Limiter limiter = new LimiterBasic();
    /**
     * Document parser, or null to simply extract text content.
     */
    protected BiFunction<WebDriver, WebElement, Document> documentParser;
    /**
     * Default timeout for explicit waits.
     */
    protected int maxWaitSeconds = 5; // maximum waiting time in seconds

    public WebPageAbstract(WebDriver wd) {
        this.wd = wd;
        limiter.incSite();
    }

    @Override
    public boolean processDocuments(BiFunction<Limiter, Document, Boolean> documentProcessor) {
        if (documentProcessor == null) {
            return true;
        }
        while (hasNext() && limiter.shouldContinue()) {
            if (!documentProcessor.apply(limiter, next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void close() {
        if (wd != null) {
            wd.close();
            wd = null;
        }
    }

    @Override
    public void setLimiter(Limiter limiter) {
        this.limiter = limiter;
    }

    @Override
    public void setDocumentParser(BiFunction<WebDriver, WebElement, Document> documentParser) {
        this.documentParser = documentParser;
    }

    @Override
    public void setMaxWaitSeconds(int maxWaitSeconds) {
        this.maxWaitSeconds = maxWaitSeconds;
    }
    
}
