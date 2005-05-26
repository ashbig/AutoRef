/*
 * DefTableManager.java
 *
 * Created on April 1, 2005, 4:24 PM
 */

package plasmid.database.DatabaseManager;

import plasmid.Constants;
import plasmid.database.*;

import java.sql.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class DefTableManager extends TableManager {
    
    /** Creates a new instance of TableManager */
    public DefTableManager() {
    }
    
    /**
     * Return the next ID number.
     */
    public int getMaxNumber(String table, String column, DatabaseTransaction dt) {
        String sql = "select max("+column+") from "+table;
        
        ResultSet rs = null;
        int id = -2;
        try {
            rs = dt.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying "+table);
        } finally {
            dt.closeResultSet(rs);
        }
        
        return ++id;
    }
    
    /**
     * Return the next ID number.
     */
    public int getMaxNumber(String table, String column) {
        String sql = "select max("+column+") from "+table;
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        int id = -2;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying "+table);
        } finally {
            t.closeResultSet(rs);
        }
        
        return ++id;
    }
    
    public int getNextid(String seqname, DatabaseTransaction t) {
        String sql = "select seqname.nextval from dual";
        ResultSet rs = null;
        int id = -1;
        try {
            rs = t.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying sequence "+seqname);
        } finally {
            t.closeResultSet(rs);
        }
        
        return id;
    }
    
    public List getVocabularies(String table, String column, DatabaseTransaction dt) {
        String sql = "select "+column+" from "+table;
        
        List l = new ArrayList();
        ResultSet rs = null;
        try {
            rs = dt.executeQuery(sql);
            while(rs.next()) {
                String s = rs.getString(1);
                l.add(s);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying "+table);
        } finally {
            dt.closeResultSet(rs);
        }
        
        return l;
    }
     
    public int getNextid(String seqname) {
        String sql = "select seqname.nextval from dual";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        int id = -1;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying sequence "+seqname);
        } finally {
            t.closeResultSet(rs);
        }
        
        return id;
    }
    
    public List getVocabularies(String table, String column) {
        String sql = "select "+column+" from "+table;
        
        List l = new ArrayList();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {            
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String s = rs.getString(1);
                l.add(s);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying "+table);
        } finally {
            t.closeResultSet(rs);
        }
        
        return l;
    }
    
    public static void main(String args[]) {
        try {
            DefTableManager manager = new DefTableManager();
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
            int id = manager.getMaxNumber("containerheader", "containerid");
            System.out.println(id);
            DatabaseTransaction.closeConnection(conn);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
