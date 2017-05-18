/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators.implementations;

import com.twiceagain.mywebdriver.generators.WebPageBasic;
import java.time.LocalDate;
import org.openqa.selenium.WebDriver;

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

    private final static String BASEURL = "https://www.google.fr/search?";

    public GoogleFR(WebDriver wd) {
        super(wd);
    }

    public void setSearchMode(Mode mode) {
        this.mode = mode;
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

        // Selecting mode
        switch (mode) {
            case NEWS:
                url += "tbm=nws&";
                break;
            case BOOKS:
                url += "tbm=bks&";
                break;
            case SHOPPING:
                url += "tbm=shop&";
                break;
            case IMAGES:
                url += "tbm=isch&";
                break;
            case VIDEOS:
                url += "tbm=vid&";
                break;
            case ALL:
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
    }

}
