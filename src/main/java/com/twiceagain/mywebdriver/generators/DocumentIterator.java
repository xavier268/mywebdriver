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
 * Wrapper around a WebPage to generate Document objects, moving from page to
 * page as needed and seemless.
 *
 * @author xavier
 */
public class DocumentIterator implements Iterator<Document> {

    protected WebPage page = null;

    /**
     * Extracts documents one by one from one or more WebPage
     *
     * @param page
     */
    public DocumentIterator(WebPage page) {
        this.page = page;
    }

    @Override
    public boolean hasNext() {
        // Avoid initialization mistakes
        if (page == null) {
            return false;
        }

        // Try to use current page
        if (page.hasNext()) {
            return true;
        }
        // Try to move to next page and use it
        if (page.hasNextPage()) {
            page = page.nextPage();
            return page.hasNext();
        }
        // exhausted ...
        return false;

    }

    @Override
    public Document next() {
        if (hasNext()) {
            return page.next();
        } else {
            throw new NoSuchElementException("No more Document available and no more pages");
        }
    }

}
