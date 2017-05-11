/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators;

import org.bson.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Parses a WebElement into a Document.
 * @author xavier
 */
public interface DocumentParser {
    
    Document toDocument(WebDriver wd, WebElement we);
    
}
