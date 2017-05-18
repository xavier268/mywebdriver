/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators.implementations;

import com.twiceagain.mywebdriver.generators.WebPageBasic;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

import org.openqa.selenium.WebDriver;

/**
 * Search selected google news abstracts - there is no notion of multi-page. The
 * search can be refined, using the provided setter.
 *
 * @author xavier
 */
@Deprecated
// Should use the generic google page, selecting new, or image, or ...
// TODO : REFACTOR completely !
public class GoogleNewsFR extends WebPageBasic {

    // Defines the various preselected topics.
    public static final String TOPIC_INTERNATIONAL = "w";
    public static final String TOPIC_NATIONAL = "n";
    public static final String TOPIC_TECGNOLOGY = "t";
    public static final String TOPIC_BUSINESS = "b";
    public static final String TOPIC_CULTURE = "e";
    public static final String TOPIC_HEALTH = "m";
    public static final String TOPIC_SPORT = "s";

    /**
     * The extended string is used to specify specific advanced search criteria.
     * Exemple :
     * "&topic=s&as_q=touscesmots&as_epq=expressionexacte&as_oq=aumoinsca&as_eq=aucunmot&as_occt=any&as_drrb=b&as_mindate=09%2F05%2F2017&as_maxdate=16%2F05%2F2017&tbs=cdr%3A1%2Ccd_min%3A09%2F05%2F2017%2Ccd_max%3A16%2F05%2F2017&as_nsrc=sourcearticle&as_nloc=lieuarticle"
     */
    protected String extendedSearch = "";

    public GoogleNewsFR(WebDriver wd) {
        super(wd);
        xpDocuments = ".//div[@class='esc-body']";
        xpNextPageClick = "";
    }

    /**
     * Set the Topic (use provided global TOPIC_xxxx constants).
     *
     * @param topic
     */
    public void setTopic(String topic) {
        if (topic != null) {
            extendedSearch += "&topic=" + topic;
        }
    }

    /**
     * Search with this EXACT EXPRESSION.
     *
     * @param exactExpression
     */
    public void setExactExpression(String exactExpression) {
        if (exactExpression != null) {
            try {
                extendedSearch += "&as_epq=" + URLEncoder.encode(exactExpression, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                LOG.severe(ex.getLocalizedMessage());
                System.exit(2);
            }
        }
    }

    /**
     * Set the minimum date.
     *
     * @param date
     */
    public void setMinDate(LocalDate date) {

        // need a format like 25/06/2017, url encoded.
        if (date == null) {
            return;
        }
        String ds = String.format("%02d%%2F%02d%%2F%02d",
                date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                System.out.println(ds);

        extendedSearch += "&as_mindate=" + ds;
    }

    /**
     * Set the maximum date.
     *
     * @param date
     */
    public void setMaxDate(LocalDate date) {

        // need a format like 25/06/2017, url encoded.
        if (date == null) {
            return;
        }
        String ds = String.format("%02d%%2F%02d%%2F%02d",
                date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        System.out.println(ds);
        extendedSearch += "&as_maxdate=" + ds;
    }

    /**
     * Search with AT LEAST ONE of these words.
     *
     * @param expression
     */
    public void setAtLeastOneOfTheseWords(String expression) {
        if (expression != null) {
            try {
                extendedSearch += "&as_oq=" + URLEncoder.encode(expression, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                LOG.severe(ex.getLocalizedMessage());
                System.exit(2);
            }
        }
    }

    /**
     * Defines the source of the news, e.g. "libération"
     *
     * @param expression
     */
    public void setSource(String expression) {
        if (expression != null) {
            try {
                extendedSearch += "&as_nsrc=" + URLEncoder.encode(expression, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                LOG.severe(ex.getLocalizedMessage());
                System.exit(2);
            }
        }
    }

    /**
     * Defines the localization of the news, e.g. "Paris or India"
     *
     * @param expression
     */
    public void setLocalization(String expression) {
        if (expression != null) {
            try {
                extendedSearch += "&as_nloc=" + URLEncoder.encode(expression, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                LOG.severe(ex.getLocalizedMessage());
                System.exit(2);
            }
        }
    }

    /**
     * Search with NONE of these words.
     *
     * @param expression
     */
    public void setNoneOfTheseWords(String expression) {
        if (expression != null) {
            try {
                extendedSearch += "&as_eq=" + URLEncoder.encode(expression, "UTF8");
            } catch (UnsupportedEncodingException ex) {
                LOG.severe(ex.getLocalizedMessage());
                System.exit(2);
            }
        }
    }

    /**
     * Launch the search on the main page. ExtendeSearchOption may have been set
     * before.
     *
     * @param searchString
     */
    @Override
    public void init(String searchString) {

        // Force the French edition here ...
        String url = "https://news.google.com/?edchanged=1&ned=fr&authuser=0";
        try {
            if (searchString != null) {
                url += "&q=" + URLEncoder.encode(searchString, "UTF8");
            }
        } catch (UnsupportedEncodingException ex) {
            LOG.severe(ex.getLocalizedMessage());
            System.exit(2);
        }

        url += extendedSearch;

        super.init(url);
    }

}
