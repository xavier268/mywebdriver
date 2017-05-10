/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
