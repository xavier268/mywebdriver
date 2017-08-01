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

import java.util.Iterator;
import java.util.function.BiFunction;
import org.bson.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * A WebPage provide a way to iterate over documents.
 *
 * @author xavier
 */
public interface WebPage extends Iterator<Document> {
    
    /**
     * Process the documents, applying the provided functionnal inteface.
     * @param documentProcessor - consumes a Document, returning a Boolean (true = success, continue, false = stop, do not proceed)
     * @return true on success.
     */
    public boolean processDocuments(BiFunction<Limiter, Document, Boolean> documentProcessor);
       /**
     * Tries to move to next page. If no next page, return false.
     *
     * @return true if next page sucessfully loaded.
     */
    public boolean goToNextPage();
    /**
     * Draw an overlay for debugging purposes on the (current) webPage.
     * @return 
     */
    public WebPageBasic addDebugOverlay();
    
    /**
     * Closes the underlying driver.
     */
    public void close();

    
    
    /**
     * Load the first page.
     * @param url 
     */
    void init(String url);

    @Override
    Document next();
    
    @Override
    boolean hasNext();

    void setDocumentParser(BiFunction<WebDriver, WebElement, Document> documentParser);

    void setLimiter(Limiter limiter);

    void setMaxWaitSeconds(int maxWaitSeconds);

    
    
    

    
    
}
