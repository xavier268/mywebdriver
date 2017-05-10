/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.bson.Document;

/**
 * A document generating webPage. Single pages would typically implement
 * hasNextPage() to null and throw NoSuchElementException on nextPage().
 * Multi-pages and ajax triggered pages would change/update pages on nextPage().
 *
 * @author xavier
 */
public interface WebPage extends Iterator<Document> {

    /**
     * Is a NEXT page available ? Avoid to load it at this stage.
     * @return 
     */
    public boolean hasNextPage();

    /**
     * Load the NEXT page, throw if it cannot be loaded or no next page.
     * @return
     * @throws NoSuchElementException 
     */
    public WebPage nextPage() throws NoSuchElementException;
    
    /**
     * Get the next document on the SAME page.
     * @return
     * @throws NoSuchElementException 
     */
    @Override
    public Document next() throws NoSuchElementException;
    /**
     * Is there another Document on the SAME page ?
     * @return 
     */
    @Override
    public boolean hasNext() ;
}
