/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators.implementations;

import com.twiceagain.mywebdriver.generators.WebPageBasic;
import org.openqa.selenium.WebDriver;

/**
 * Connector for PagesBlanches.
 * @author xavier
 */
public class PagesBlanches extends WebPageBasic {
    
    public PagesBlanches(WebDriver wd) {
        super(wd);
        xpDocuments = ".//article";
        xpHasNextPage = ".//a[@id='pagination-next']";
        xpNextPageClick = xpHasNextPage;
        xpPageLoadedMarker = xpDocuments+"[18]";
        xpStalenessMarker = xpDocuments;
    }
    
    public PagesBlanches init(String nom, String ville) {
        String url = String.format("https://www.pagesjaunes.fr/pagesblanches/recherche"
                + "?quoiqui=%s&ou=%s&proximite=0",
                nom, ville);
        init(url);
        return this;
    }
    
}
