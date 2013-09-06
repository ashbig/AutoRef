/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author dongmei
 */
public class DatabaseConnection {

    static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    private static DatabaseConnection pool = null;
    private static DataSource dataSource = null;

    public synchronized static DatabaseConnection getInstance() {
        if (pool == null) {
            pool = new DatabaseConnection();
        }
        return pool;
    }

    private DatabaseConnection() {
        try {
            InitialContext ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("java:/comp/env/jdbc/plasmid");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("cannot get database connection.\n" + ex.getMessage());
            //logger.warning("cannot get database connection.\n" + ex.getMessage());
        }
    }

    public void closeConnection(Connection c) {
        try {
            c.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error closing database connection\n" + ex.getMessage());
            //logger.warning("Error closing database connection\n" + ex.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("cannot get database connection.\n" + ex.getMessage());
            //logger.warning("cannot get database connection.\n" + ex.getMessage());
            return null;
        }
    }
    
    public void commit(Connection conn) {
        try {
            conn.commit();
        } catch (Exception ex) {}
    }
    
    public void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception ex) {}
    }
    
    public int test() {        
        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection conn = instance.getConnection();
        if (conn == null) {
            System.out.println("Cannot connect to database.");
        }

        String sql = "select count(*) from gene";
        Statement stmt = null;
        ResultSet rs = null;
        int value = -1;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()) {
                value = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
            rs.close();
            stmt.close();
            } catch (Exception ex) {}
        }
        
        instance.closeConnection(conn);
        System.out.print("value="+value);
        return value;
    }
}

