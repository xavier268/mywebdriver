/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators.implementations;

import com.twiceagain.mywebdriver.limiters.Limiter;
import java.util.function.BiFunction;
import org.bson.Document;

/**
 * Document processor that just dumps the content of all Documents.
 *
 * @author xavier
 */
public class DocumentPrinter implements BiFunction<Limiter, Document, Boolean> {

    public DocumentPrinter() {
    }

    /**
     * Used to process all the generated documents, one by one.
     * 
     * @param lim
     * @param doc
     * @return false to stop processing.
     */
    @Override
    public Boolean apply(Limiter lim, Document doc) {
        if (doc == null) {
            return false;
        }
        if (lim != null) {
            if (lim.shouldStop()) {
                return false;
            }
            System.out.printf("\nPage %s  Document %s"
                    + "\n=======================\n"
                    + "%s"
                    + "\n=======================\n",
                    lim.countPages(), lim.countDocuments(), doc);
            return lim.shouldContinue();
        } else {
            // lim is null ...
            System.out.printf("\nPage %s  Document %s"
                    + "\n=======================\n"
                    + "%s"
                    + "\n=======================\n",
                    "??", "??", doc);
            return true;
        }

    }

}
