/*
 * DatabaseManager.java
 *
 * Created on October 17, 2001, 2:06 PM
 */

package edu.harvard.med.hip.cloneOrder.database;

import java.sql.*;

/**
 *
 * @author  hweng
 * @version
 */
public class DBManager2 {
    private Connection connection = null;
    
    private String url = "jdbc:odbc:CloneOrder";
    private String username = "";
    private String password = "";
    
    /** Creates new DatabaseManager */
    public DBManager2() {
    }
    
    /**
     * Create new DatabaseManager by providing the url, username and password.
     *
     * @param url The url of the database instance.
     * @param username The username of the database user.
     * @param password The password of the database user.
     *
     * @return The DatabaseManager object.
     */
    public DBManager2(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    /**
     * Connect to the database.
     *
     * @return Connection object.
     */
    public Connection connect() {
        if (connection != null) {
            return connection;
        }
        
        try {           
            DriverManager.registerDriver(new sun.jdbc.odbc.JdbcOdbcDriver());
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            return connection;
        }
        catch( SQLException e ) {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * Disconnect the database. Return true if successful; false otherwise.
     *
     * @return True if disconnect successfully; false otherwise.
     */
    public boolean disconnect(Connection connection) {
        try {
            if(connection != null) {
                connection.close();
                connection = null;
            }
            
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Commits the changes by calling the JDBC commit.
     *
     * @return True if commit successful; false otherwise.
     */
    public boolean commit(Connection connection) {
        try {
            if(connection != null) {
                connection.commit();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Abort the changes by calling the JDBC rollback.
     *
     * @return True if rollback successful; false otherwise.
     */
    public boolean abort(Connection connection) {
        try {
            if(connection != null) {
                connection.rollback();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static void main(String args[]) {
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
        System.out.println("Connecting.... done");

        manager.disconnect(conn);
    }
}
