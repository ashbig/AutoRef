/*
 * ConnectionPool.java
 *
 * Created on April 23, 2002, 1:54 PM
 */

package edu.harvard.med.hip.cloneOrder.database;

import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;

/**
 *
 * @author  hweng
 */

// apply Singelton pattern

public class ConnectionPool {
    
    private DataSource ds;
    private static ConnectionPool myself;
    
    /** Creates a new instance of ConnectionPool */
    public ConnectionPool(DataSource ds) {
        this.ds = ds;
    }
    
    public static void init(DataSource ds){
        myself = new ConnectionPool(ds);
    }
    
    public static ConnectionPool getInstance(){
        if(myself == null){
            throw new IllegalStateException("Pool not initialized.");
        }
        return myself;
    }
    
    public Connection getConnection(){
        Connection con = null;
        try{
            con = ds.getConnection();
        }catch(Exception e){
            System.out.println(e);
        }            
        return con;
    }
    
      
}
