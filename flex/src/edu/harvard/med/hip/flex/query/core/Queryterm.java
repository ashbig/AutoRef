/*
 * Queryterm.java
 *
 * Created on October 3, 2003, 5:32 PM
 */
package edu.harvard.med.hip.flex.query.core;
import edu.harvard.med.hip.flex.database.*;
import java.util.*;
import java.sql.*;

/**
 * This class lists the terms in the query pull down menu when 
 * a user is searching on verified genes.
 * @author  dzuo
 * @version
 */
public class Queryterm {
    private String name;
    
    /** Creates new Nametype */
    public Queryterm(String name) {
        this.name = name;
    }
    
    /**
     * 
     *
     * @return A list of query terms.
     */
    public static Vector getAllQueryterms() {
        Vector result = new Vector();
        result.add(new Queryterm("Sequence ID"));       
        return result;
    }

    /**
     * Return true if the given nametype exists in the database; return 
     * false otherwise.
     *
     * @param nametype The nametype to be validated.
     * @return True if the given nametype exists in the database; return
     * false otherwise.
     */
    public static boolean exists(String queryterm) {
    /*    String sql = "select * from nametype where nametype='"+nametype+"'";
        ResultSet rs = null;
        boolean ret = false;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                ret = true;
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }     
        
        return ret; */
        return true;
    }
    
    /**
     * Add the nametype to the database.
     *
     * @param nametype The value to be added to the database.
     * @param conn The database connection.
     */
    public static boolean addQueryterm(String term, Connection conn) {
      /* String sql = "insert into nametype values ('"+nametype+"')";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException sqlE) {
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        } */   
        return true;
    }
    
    /**
     * Return the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    public static void main(String [] args) {
    /*        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            if(Nametype.addNametype("DUMMY", conn)) {
                System.out.println("Name type has been added successfully.");
            } else {
                System.out.println("Name type cannot be added to the database.");
            }
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
        }        */
    }        
}

