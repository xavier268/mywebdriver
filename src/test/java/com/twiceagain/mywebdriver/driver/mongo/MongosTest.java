/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.mywebdriver.driver.mongo;

import com.twiceagain.mywebdriver.driver.mongo.Mongos;
import com.mongodb.MongoTimeoutException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author xavier
 */
public class MongosTest {
    
    public MongosTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void defaultConnectionAndListDatabases() {
        Mongos.setConnection("mongodb://localhost:27017");
        System.out.printf("\nMongo databases :\n%s\n", Mongos.listDatabaseNames());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void invalidConnectionStringShouldThrow() {
        // Invalid strings will throw
        Mongos.setConnection("invalidString");        
    }
    
    
    @Test(expected = MongoTimeoutException.class)
    public void unknownPortConnectionStringShouldTimeout() {
        // Invalid port will time out with no other exception thrown
         Mongos.setConnection("mongodb://localhost:27777");          
    }
    
}
