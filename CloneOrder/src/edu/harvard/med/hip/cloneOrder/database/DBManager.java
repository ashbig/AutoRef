/*
 * DBManager.java
 *
 * Created on May 8, 2002, 11:43 AM
 */

package edu.harvard.med.hip.cloneOrder.database;

import java.sql.*;
/**
 *
 * @author  hweng
 */
public class DBManager {
    
    private ConnectionPool pool;
    private Connection connection;
    
    /** Creates a new instance of DBManager */
    public DBManager() {
        pool = ConnectionPool.getInstance();
    }
 
    /**
     * Connect to the database.
     *
     * @return Connection object.
     */
    public Connection connect() {
        connection = pool.getConnection();       
        if (connection == null) {
            //System.out.println("Cannot connect to the database.");
            return null;
        }
        return connection;
    }
        
   
    /**
     * Disconnect the database. Return true if successful; false otherwise.
     *
     * @return True if disconnect successfully; false otherwise.
     */
    public boolean disconnect(Connection con) {
        try {
            if(con != null) {
                con.close();
                con = null;
            }            
            return true;        
        } catch (SQLException e) {
            System.out.println("cannot disconnect database!");
            return false;
        }
    }
    
    /**
     * Commits the changes by calling the JDBC commit.
     *
     * @return True if commit successful; false otherwise.
     */
    public boolean commit(Connection con) {
        try {
            if(con != null) {
                con.commit();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("cannot commit!");
            return false;
        }
    }
    
    /**
     * Abort the changes by calling the JDBC rollback.
     *
     * @return True if rollback successful; false otherwise.
     */
    public boolean abort(Connection con) {
        try {
            if(con != null) {
                con.rollback();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("cannot rollback!");
            return false;
        }
    }
    

}