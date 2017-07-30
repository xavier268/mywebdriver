/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators.implementations;

import com.twiceagain.mywebdriver.generators.WebPageBasic;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Connector for www.amazon.fr
 *
 * @author xavier
 */
public class AmazonFR extends WebPageBasic {

    public AmazonFR(WebDriver wd) {
        super(wd);
        xpDocuments = ".//div[@class='s-item-container']";
        xpHasNextPage = ".//span[@id='pagnNextString']";
        xpNextPageClick = xpHasNextPage;
        xpPageLoadedMarker = String.format("(%s)[14]", xpDocuments);
        xpStalenessMarker = xpNextPageClick;
        documentParser = new DocumentParser();
    }

    /**
     * Initialize with a search string (type in normal text - it will be
     * URLEncoded).
     *
     * @param search
     */
    @Override
    public void init(String search) {
        String url = "https://www.amazon.fr/s/?__mk_fr_FR=ÅMÅŽÕÑ&field-keywords=";
        try {
            url += URLEncoder.encode(search, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOG.severe(ex.getLocalizedMessage());
            throw new RuntimeException(ex);
        }
        super.init(url);
    }

    /**
     * TODO
     */
    
    protected class DocumentParser implements BiFunction<WebDriver, WebElement, Document> {

        @Override
        public Document apply(WebDriver wd, WebElement we) {
            Document doc = new Document("doctype", DocumentParser.class.getCanonicalName());

            try {
                doc.append("title", we.findElement(By.tagName("h2")).getText());
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
            }
            String priceString = null;
            try {
                String xpPrice = ".//*[contains(@class,'a-color-price' ) and contains(text(),'EUR')]";
                priceString = we.findElement(By.xpath(xpPrice)).getText();
                doc.append("pricestring", priceString);
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
            }

            if (priceString != null) {
                priceString = priceString.replace(",", ".").replace(" ", "");
                final Pattern pat = Pattern.compile("^EUR([.0123456789]+).*$");
                Matcher matcher = pat.matcher(priceString);
                double price = 0;
                if (matcher.matches()) {
                    price = Double.parseDouble(matcher.group(1));
                }
                doc.append("price", price);
            }

            try {
                String url = we.findElement(By.xpath(".//h2/ancestor::a")).getAttribute("href");
                // Sometimes, url is encoded in another one ...
                url = URLDecoder.decode(url, "UTF8");
                final Pattern pat = Pattern.compile("^.*/dp/([A-Z0-9]+)/.*$");
                Matcher match = pat.matcher(url);
                if (match.matches()) {
                    String asin = match.group(1);
                    doc
                            .append("asin", asin)
                            .append("url", "https://www.amazon.fr/dp/" + asin);
                }
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
            }

            return doc;
        }

    }

}
