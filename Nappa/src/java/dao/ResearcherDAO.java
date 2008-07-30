/*
 * ResearcherDAO.java
 *
 * Created on October 4, 2007, 2:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dao;

import database.DatabaseTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import transfer.ResearcherTO;

/**
 *
 * @author dzuo
 */
public class ResearcherDAO {
    
    /** Creates a new instance of ResearcherDAO */
    public ResearcherDAO() {
    }
    
    public static ResearcherTO getResearcher(String username, String password) throws DaoException {
        String sql = "select name,email,usergroup"+
                " from researcher where id=? and password=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResearcherTO r = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                String name = rs.getString(1);
                String email = rs.getString(2);
                String usergroup = rs.getString(3);
                r = new ResearcherTO(name,email,password,usergroup,username);
            }
        } catch (Exception ex) {
            throw new DaoException("Cannot get user information\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
        
        return r;
    }
    
    public static ResearcherTO getResearcher(String id) throws DaoException {
        String sql = "select name,email,password,usergroup"+
                " from researcher where id=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResearcherTO r = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            stmt.setString(1, id);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                String name = rs.getString(1);
                String email = rs.getString(2);
                String pass = rs.getString(3);
                String usergroup = rs.getString(4);
                r = new ResearcherTO(name,email,pass,usergroup,id);
            }
        } catch (Exception ex) {
            throw new DaoException("Cannot get researcher for id: "+id+"\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
        
        return r;
    }
}
