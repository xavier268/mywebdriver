/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.limiters;

/**
 * A limiter helps decide when to stop parsing.
 *
 * @author xavier
 */
public interface Limiter {

    /**
     * Should we stop now ?
     *
     * @return
     */
    boolean shouldStop();
    boolean shouldContinue();

    /**
     * Get elapsed running time in millis.
     * @return 
     */
    long getElapsedTime();

    /**
     * Reset elapsed time.
     */
    void resetElapsedTime();
    /**
     * Reset all parameters.
     */
    void reset();

    /**
     * Increment page count
     */
    void incPage();

    /**
     * Increment document count
     */
    void incDocument();

    /**
     * Increment site count
     */
    void incSite();

}
