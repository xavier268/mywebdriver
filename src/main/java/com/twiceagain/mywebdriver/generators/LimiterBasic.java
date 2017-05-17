/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators;

/**
 * Basic, multpurpose, limiter. Extend as needed.
 *
 * @author xavier
 */
public class LimiterBasic implements Limiter {

    protected final Long maxTime;
    protected long start = System.currentTimeMillis();

    /**
     * Various counters.
     */
    protected final Integer maxDocument, maxSite, maxPage;
    protected Integer curDocument = 0, curSite = 0, curPage = 0;

    /**
     * No limit set, continue forever ...
     */
    public LimiterBasic() {
        maxTime = null;
        maxDocument = maxSite = maxPage = null;
    }

    /**
     * Create time limited limiter.
     *
     * @param maxTime - max elapsed time in millis
     */
    public LimiterBasic(Long maxTime) {
        this.maxTime = maxTime;
        maxDocument = maxSite = maxPage = null;
    }

    /**
     * Fully defined limiter.
     *
     * @param maxTime max elapsed time, null to ignore
     * @param maxDocument max document count, null to ignore
     * @param maxPage max page count, null to ignore
     * @param maxSite max site count, null to ignore
     */
    public LimiterBasic(Long maxTime, Integer maxDocument, Integer maxPage, Integer maxSite) {
        this.maxTime = maxTime;
        this.maxDocument = maxDocument;
        this.maxSite = maxSite;
        this.maxPage = maxPage;
    }

    @Override
    public boolean shouldStop() {
        return !shouldContinue();
    }

    @Override
    public long getElapsedTime() {
        return System.currentTimeMillis() - start;
    }

    @Override
    public void resetElapsedTime() {
        start = System.currentTimeMillis();
    }

    @Override
    public void reset() {
        resetElapsedTime();
        curPage = curSite = curDocument = 0;
    }

    @Override
    public void incPage() {
        curPage += 1;
    }

    @Override
    public void incDocument() {
        curDocument += 1;
    }

    @Override
    public void incSite() {
        curSite += 1;
    }

    @Override
    public boolean shouldContinue() {

        return (maxTime == null || getElapsedTime() < maxTime)
                && (maxDocument == null || curDocument < maxDocument)
                && (maxPage == null || curPage < maxPage)
                && (maxSite == null || curSite < maxSite);

    }

    @Override
    public int countDocuments() {
        return curDocument;
    }

    @Override
    public int countPages() {
        return curPage;
    }

    @Override
    public int countSites() {
        return curSite;
    }

}
