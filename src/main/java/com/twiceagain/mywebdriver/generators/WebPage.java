/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators;

import com.twiceagain.mywebdriver.limiters.Limiter;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bson.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * A WebPage should provide a way to iterate over documents.
 *
 * @author xavier
 */
public interface WebPage extends Iterator<Document> {
    
    /**
     * Process the documents, applying the provided functionnal inteface.
     * @param documentProcessor - consumes a Document, returning a Boolean (true = success)
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
     * Preload elements from current page, or any other initialisation required
     * for each new page.
     *
     * @return - false if exception occured, tru if ok (even with no document
     * loaded)
     */
    WebPageBasic init();

    @Override
    Document next();
    
    @Override
    boolean hasNext();

    WebPageBasic setDocumentParser(BiFunction<WebDriver, WebElement, Document> documentParser);

    WebPageBasic setLimiter(Limiter limiter);

    WebPageBasic setMaxWaitSeconds(int maxWaitSeconds);

    WebPageBasic setXpDocuments(String xpDocuments);

    WebPageBasic setXpHasNextPage(String xpHasNextPage);

    WebPageBasic setXpNextPageClick(String xpNextPageClick);

    WebPageBasic setXpPageLoadedMarker(String xpPageLoadedMarker);

    WebPageBasic setXpStalenessMarker(String xpStalenessMarker);
    
    

    
    
}
