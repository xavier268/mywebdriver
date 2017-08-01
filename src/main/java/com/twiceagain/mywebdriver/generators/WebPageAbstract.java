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
