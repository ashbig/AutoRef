/*
 * DatabaseManager.java
 *
 * Created on October 17, 2001, 2:06 PM
 */

package edu.harvard.med.hip.metagene.database;

import java.sql.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class DatabaseManager {
    private Connection connection = null;
    
//    private String url = "jdbc:oracle:thin:@localhost:1532:WALL";
//    private String username = "flex_owner";
//    private String password = "flex";
    private String url = "jdbc:odbc:medgene";
    private String username = "";
    private String password = "";
    
    /** Creates new DatabaseManager */
    public DatabaseManager() {
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
    public DatabaseManager(String url, String username, String password) {
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
//            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
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
    public boolean disconnect() {
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
    public boolean commit() {
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
    public boolean abort() {
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
        DatabaseManager manager = new DatabaseManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return;
        }
        
        Statement stmt = null;
        String sql = "select * from ID_Type";
        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        
            while(rs.next()) {
                System.out.println(rs.getString(2));
            }

            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            manager.disconnect();
        }
    }
}
