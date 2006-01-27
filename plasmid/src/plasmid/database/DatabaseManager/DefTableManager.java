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
        String sql = "select "+seqname+".nextval from dual";
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
        String sql = "select "+column+" from "+table+" order by "+column;
        
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
     
    public static List getVocabularies(String table, String columnKnown, String columnUnknown, String column) {
        List results = new ArrayList();
        String sql = "select "+columnUnknown+" from "+table+" where "+columnKnown+"=? order by "+columnUnknown;
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, column);
            rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                String rt = rs.getString(1);
                results.add(rt);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Error occured while querying "+table);
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return results;
    }
     
    public static List getVocabularies(String table, String column) {
        String sql = "select "+column+" from "+table+" order by "+column;
        
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
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return l;
    }
    
    public static int getNextid(String seqname) {
        String sql = "select "+seqname+".nextval from dual";
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
            if(Constants.DEBUG) {
                System.out.println("Error occured while querying sequence "+seqname);
                System.out.println(ex);
            }
        } finally {
            t.closeResultSet(rs);
        }
        
        return id;
    }
    
    public static String getVocabulary(String table, String columnKnown, String columnUnknown, String column) {
        String sql = "select "+columnUnknown+" from "+table+" where "+columnKnown+"=?";
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String rt = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, column);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                rt = rs.getString(1);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Error occured while querying "+table);
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return rt;
    }
       
    public String getVocabulary(String table, String columnKnown, String columnUnknown, String column, Connection conn) {
        String sql = "select "+columnUnknown+" from "+table+" where "+columnKnown+"=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String rt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, column);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                rt = rs.getString(1);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Error occured while querying "+table);
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return rt;
    }
    
    public static void main(String args[]) {
        try {
            DefTableManager manager = new DefTableManager();
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
            int id = manager.getMaxNumber("containerheader", "containerid");
            System.out.println(id);
            String sp = DefTableManager.getVocabulary("species", "genusspecies", "code", "Pseudomonas aeruginosa");
            System.out.println(sp);
            DatabaseTransaction.closeConnection(conn);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
