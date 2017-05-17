/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.generators;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.twiceagain.mywebdriver.driver.mongo.Mongos;
import java.util.function.BiFunction;
import org.bson.Document;

/**
 * Save documents to provided Database/Collection.
 *
 * @author xavier
 */
public class DocumentToDatabase implements BiFunction<Limiter, Document, Boolean> {

    MongoClient client = Mongos.getClient();
    MongoCollection<Document> coll;

    /**
     * Prepare to export to specified databas/collection.
     * @param database
     * @param collection 
     */
    public DocumentToDatabase(String database, String collection) {
        coll = client.getDatabase(database).getCollection(collection);
    }

    @Override
    public Boolean apply(Limiter lim, Document doc) {
        if (doc == null) {
            return true;
        }
        if (lim != null) {
            System.out.printf("\nPage %d\tdoc %d\t Time %.1f s (%.3f docs/sec)",
                    lim.countPages(), lim.countDocuments(), lim.getElapsedTime() / 1000.,
                    1000. * lim.countDocuments() / lim.getElapsedTime());
        }
        coll.insertOne(doc);
        return true;
    }

}
