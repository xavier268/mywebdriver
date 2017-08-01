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
