/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators.implementations;

import com.twiceagain.mywebdriver.generators.WebPageBasic;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.function.BiFunction;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * A WebPage connector to search google.fr for web, news, ...
 *
 * @author xavier
 */
public class GoogleFR extends WebPageBasic {

    

    /**
     * Available search modes.
     */
    public enum Mode {
        ALL, IMAGES, VIDEOS, NEWS, SHOPPING, BOOKS;
    }
    /**
     * Selected search mode.
     */
    protected Mode mode = Mode.ALL;
    protected LocalDate minDate = null;
    protected LocalDate maxDate = null;
    private boolean noFilter=false;
    // filter prevents aggregating similar results
    // otherwise, results are bouded around a few 100s.
    // Applying request for 100 results only works for the first page.
    // Following pages will only capture 10 results.
    private final static String BASEURL = "https://www.google.fr/search?ie=utf8&num=100&";
    // private final static String BASEURL = "https://www.google.fr/search?ie=utf8&";
    private final static DateFormat DATEFORMAT = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);

    public GoogleFR(WebDriver wd) {
        super(wd);
    }

    public void setSearchMode(Mode mode) {
        this.mode = mode;
    }
    
    public void setNoFilter() {
        this.noFilter=true;
    }

    /**
     * Set the max and min dates. To set only one, set to null.
     *
     * @param min
     * @param max
     */
    public void setDateLimites(LocalDate min, LocalDate max) {
        this.minDate = min;
        this.maxDate = max;
    }

    /**
     * Initiate the serach, based on the parameters that were set. Feel free to
     * use search operators as needed. See :
     * https://moz.com/learn/seo/search-operators for details.
     *
     * @param searchstring
     */
    @Override
    public void init(String searchstring) {
        String url = BASEURL;
        if (documentParser == null) {
            documentParser = new DocumentParser();
        }
        
        if(noFilter) {
            url+="filter=0&";
        }

        // Selecting mode
        switch (mode) {
            case NEWS:
                url += "tbm=nws&";
                xpDocuments = ".//div[@class='g']";
                xpHasNextPage = ".//a[@id='pnnext']";
                xpNextPageClick = xpHasNextPage;
                xpPageLoadedMarker = xpHasNextPage;
                xpStalenessMarker = xpHasNextPage;
                break;
            case BOOKS:
                url += "tbm=bks&";
                xpDocuments = ".//div[@class='g']";
                xpHasNextPage = ".//a[@id='pnnext']";
                xpNextPageClick = xpHasNextPage;
                xpPageLoadedMarker = xpHasNextPage;
                xpStalenessMarker = xpHasNextPage;
                break;
            case SHOPPING:
                url += "tbm=shop&";
                break;
            case IMAGES:
                url += "tbm=isch&";
                break;
            case VIDEOS:
                url += "tbm=vid&";
                xpDocuments = ".//div[@class='g']";
                xpHasNextPage = ".//a[@id='pnnext']";
                xpNextPageClick = xpHasNextPage;
                xpPageLoadedMarker = xpHasNextPage;
                xpStalenessMarker = xpHasNextPage;
                break;
            case ALL:
                xpDocuments = ".//div[@class='g']";
                xpHasNextPage = ".//a[@id='pnnext']";
                xpNextPageClick = xpHasNextPage;
                xpPageLoadedMarker = xpHasNextPage;
                xpStalenessMarker = xpHasNextPage;
                break;
            default:
        }

        // Formating dates : ex tbs=cdr:1,cd_min:5/5/2017,cd_max:5/12/2017
        // if only one : tbs=cdr:1,cd_min:5/5/2017
        // format is mont/day/year
        if (maxDate != null || minDate != null) {
            url += "tbs=cdr:1";
            if (minDate != null) {
                url += String.format(",cd_min:%d/%d/%d",
                        minDate.getMonthValue(),
                        minDate.getDayOfMonth(),
                        minDate.getYear());
            }
            if (maxDate != null) {
                url += String.format(",cd_max:%d/%d/%d",
                        maxDate.getMonthValue(),
                        maxDate.getDayOfMonth(),
                        maxDate.getYear());
            }
            url += "&";
        }

        // Encode search string
        if (searchstring != null) {
            try {
                url += "q=" + URLEncoder.encode(searchstring, "UTF8");

            } catch (UnsupportedEncodingException ex) {
                LOG.severe(ex.getLocalizedMessage());
                System.exit(2);
            }
        }
        super.init(url);
    }

    /**
     * Parse a document, according to the selected search mode.
     */
    protected class DocumentParser implements BiFunction<WebDriver, WebElement, Document> {

        @Override
        public Document apply(WebDriver wd, WebElement we) {
            if (we == null) {
                return null;
            }
            Document doc = new Document("doctype", GoogleFR.class.getCanonicalName())
                    .append("mode", mode.toString());
            try {
                doc.append("title", we.findElement(By.tagName("h3")).getText());
                doc.append("url", we.findElement(By.xpath(".//h3//a")).getAttribute("href"));
            } catch (Exception ex) {
                LOG.info(ex.getLocalizedMessage());
                // ignore
            }
            try {
                doc.append("citation", we.findElement(By.tagName("cite")).getText());
            } catch (Exception ex) {
                LOG.finer(ex.getLocalizedMessage());
                // ignore
            }
            try {
                doc.append("source", we.findElement(By.xpath(".//div[@class='slp']/span[1]")).getText());
            } catch (Exception ex) {
                LOG.finer(ex.getLocalizedMessage());
                // ignore
            }
            try {
                doc.append("subtitle", we.findElement(By.xpath(".//*[@class='st']")).getText());
            } catch (Exception ex) {
                LOG.finer(ex.getLocalizedMessage());
                // ignore
            }
            try {
                String ds = null;

                if (mode == Mode.NEWS) {
                    ds = we.findElement(By.xpath(".//*[contains(@class,'f nsa')]"))
                            .getText();
                }

                if (mode == Mode.VIDEOS) {
                    ds = we.findElement(By.xpath(".//div[contains(@class,'slp f')]"))
                            .getText();
                }
                // If relative date or time, use current date.
                if (ds != null) {
                    if (ds.startsWith("l y a ", 1)) {
                        doc.append("date", new Date());
                    } else {
                        doc.append("date", DATEFORMAT.parse(ds));
                    }
                }
            } catch (Exception ex) {
                LOG.finer(ex.getLocalizedMessage());
                // ignore
            }

            doc.append("text", we.getText());
            return doc;
        }

    }

}
