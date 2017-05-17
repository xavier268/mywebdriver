/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.driver.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Factory class to provide access to a SYNC MongoClient.
 *
 * @author xavier
 */
public class Mongos {

    private static final Logger LOG = Logger.getLogger(Mongos.class.getName());

    /**
     * Protected constructor prevents initialisation.
     */
    protected Mongos() {
    }

    /**
     * Internal, singleton instance for client.
     */
    protected static MongoClient client = null;
    /**
     * Mongo connection string. 
     */
    protected static String connection = "mongodb://localhost:27017";

    /**
     * Get a (singleton) instance of the Mongo client.
     * @return 
     */
    public static MongoClient getClient() {
        if (client == null) {
            client = new MongoClient(new MongoClientURI(connection));
        }
        return client;
    }

    /**
     * Set the connection string to use, and reset the client. Will throw on
     * invalid string, or timeout on no connection.
     *
     * @param aConnection
     */
    public static void setConnection(String aConnection) {
        if (aConnection != null) {
            if (!connection.equals(aConnection)) {
                // Close client, to force new client with new connection details.
                close();
                connection = aConnection;
                // Reopen client and access for early failure detection
                getClient().listDatabases().first();
            }

        }
    }

    /**
     * Get the list of databases names.
     *
     * @return
     */
    public static List<String> listDatabaseNames() {
        List<String> res = new ArrayList<>();

        for (String name : getClient().listDatabaseNames()) {
            res.add(name);
        }
        return res;
    }

    /**
     * Closes existing client, and frees all resources. Not necessary, since
     * client is a singleton, unless there was a change in the connection
     * string.
     */
    public static void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

}
