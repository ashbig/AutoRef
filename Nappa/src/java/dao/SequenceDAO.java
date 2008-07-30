/*
 * SequenceDAO.java
 *
 * Created on September 27, 2007, 10:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dao;

import database.DatabaseTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dzuo
 */
public class SequenceDAO {
    
    /** Creates a new instance of SequenceDAO */
    public SequenceDAO() {
    }

    /**
     * Return the next ID number.
     */
    public int getMaxNumber(String table, String column, DatabaseTransaction dt) throws DaoException {
        String sql = "select max("+column+") from "+table;
        
        ResultSet rs = null;
        int id = -2;
        try {
            rs = dt.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            throw new DaoException("Cannot get max number."+ex.getMessage());
        } finally {
            dt.closeResultSet(rs);
        }
        
        return ++id;
    }
    
    /**
     * Return the next ID number.
     */
    public static int getMaxNumber(String table, String column) throws DaoException {
        String sql = "select max("+column+") from "+table;
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        int id = 0;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            throw new DaoException("Cannot get max number."+ex.getMessage());
        } finally {
            t.closeResultSet(rs);
        }
        
        return id;
    }
    
    public static int getNextid(String table, String column) throws DaoException {
        String sql = "select max("+column+") from "+table;
        
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
            throw new DaoException("Cannot get max number."+ex.getMessage());
        } finally {
            t.closeResultSet(rs);
        }
        
        return ++id;
    }
    
    public int getNextid(String seqname, DatabaseTransaction t) throws DaoException {
        String sql = "select "+seqname+".nextval from dual";
        ResultSet rs = null;
        int id = -1;
        try {
            rs = t.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            throw new DaoException("Cannot get sequence."+ex.getMessage());
        } finally {
            t.closeResultSet(rs);
        }
        
        return id;
    }
    
    public List getVocabularies(String table, String column, DatabaseTransaction dt) throws DaoException {
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
            throw new DaoException("Error occured while querying table "+table+ex.getMessage());
        } finally {
            dt.closeResultSet(rs);
        }
        
        return l;
    }
     
    public static List getVocabularies(String table, String columnKnown, String columnUnknown, String column) throws DaoException {
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
            throw new DaoException("Error occured while querying table "+table+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return results;
    }
     
    public static List getVocabularies(String table, String column) throws DaoException {
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
            throw new DaoException("Error occured while querying table "+table+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return l;
    }
    
    public static int getNextid(String seqname) throws DaoException {
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
            throw new DaoException("Error occured while querying sequence "+seqname+ex.getMessage());
        } finally {
            t.closeResultSet(rs);
        }
        
        return id;
    }
    
    public static String getVocabulary(String table, String columnKnown, String columnUnknown, String column) throws DaoException {
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
            throw new DaoException("Error occured while querying table "+table+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return rt;
    }
       
    public String getVocabulary(String table, String columnKnown, String columnUnknown, String column, Connection conn) throws DaoException {
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
            throw new DaoException("Error occured while querying table "+table+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return rt;
    }
   
}
