/*
 * MIT License
 * 
 * Copyright (c) 2017 Xavier Gandillot
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.twiceagain.mywebdriver.generators;

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
            System.out.printf("\nPage %s  Document %s Elapsed : %s sec (%.3f docs/sec)"
                    + "\n=======================\n"
                    + "%s"
                    + "\n=======================\n",
                    lim.countPages(), lim.countDocuments(), lim.getElapsedTime() / 1000.,
                    1000. * lim.countDocuments() / lim.getElapsedTime(), doc);
            return lim.shouldContinue();
        } else {
            // lim is null ...
            System.out.printf("\nPage %s  Document %s Elapsed %s sec"
                    + "\n=======================\n"
                    + "%s"
                    + "\n=======================\n",
                    "??", "??", "??", doc);
            return true;
        }

    }

}
