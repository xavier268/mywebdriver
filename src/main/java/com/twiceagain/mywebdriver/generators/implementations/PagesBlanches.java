/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators.implementations;

import com.twiceagain.mywebdriver.generators.WebPageBasic;
import java.util.function.BiFunction;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Connector for PagesBlanches.
 *
 * @author xavier
 */
public class PagesBlanches extends WebPageBasic {

    public PagesBlanches(WebDriver wd) {
        super(wd);
        xpDocuments = ".//article";
        xpHasNextPage = ".//a[@id='pagination-next']";
        xpNextPageClick = xpHasNextPage;
        xpPageLoadedMarker = String.format("(%s)[18]", xpDocuments);
        xpStalenessMarker = xpDocuments;
        documentParser = new DocumentParser();
    }

    public PagesBlanches init(String nom, String ville) {
        String url = String.format("https://www.pagesjaunes.fr/pagesblanches/recherche"
                + "?quoiqui=%s&ou=%s&proximite=0",
                nom, ville);
        init(url);
        return this;
    }

    protected class DocumentParser implements BiFunction<WebDriver, WebElement, Document> {

        @Override
        public Document apply(WebDriver wd, WebElement el) {

            if (wd == null || el == null) {
                return null;
            }

            Document doc = new Document("doctype", DocumentParser.class.getCanonicalName());

            try {
                doc.append(
                        "adresse",
                        el.findElement(By.xpath(".//div[@class='adresse-container']")).getText()
                );
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore ...
            }

            try {
                doc.append(
                        "fullname",
                        el.findElement(By.xpath("(.//h2[@class='company-name']/a)[2]")).getText()
                );
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore ...
            }

            try {
                doc.append(
                        "telephone",
                        el.findElement(By.xpath(".//div[@class='tel-zone']/strong"))
                                .getAttribute("title"));
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore ...
            }

            return doc;
        }

    }

}
