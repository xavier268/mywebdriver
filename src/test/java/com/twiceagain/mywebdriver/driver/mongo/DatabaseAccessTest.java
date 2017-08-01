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
package com.twiceagain.mywebdriver.driver.mongo;

import java.util.Arrays;
import java.util.Date;
import org.bson.Document;
import org.junit.Test;

/**
 * Demo acessing database and playing with bson documents.
 *
 * @author xavier
 */
public class DatabaseAccessTest {

    public DatabaseAccessTest() {
    }

    
    public Document createDummyDocument() {
        Document doc = new Document("nom", "xavier")
                .append("quand", new Date())
                .append("maListe", Arrays.asList(1,2,3,4));
        //System.out.printf("\nDummy document (string) : %s\n", doc);
        //System.out.printf("\nDummy document (json) : %s\n", doc.toJson());
        return doc;
    }
    
    @Test
    public void storeDummy() {
        Document doc = createDummyDocument();
        Mongos.getClient().getDatabase("dummydb").getCollection("dummycol").insertOne(doc);
         }
    
    @Test
    public void dumpDummy() {
        System.out.printf("\nRetrieving content of dummydb.dummycol\n");
        for (Document doc : Mongos.getClient().getDatabase("dummydb").getCollection("dummycol").find()){
            System.out.printf("\n\t --> %s", doc);
        }
        System.out.println();
    }

}
