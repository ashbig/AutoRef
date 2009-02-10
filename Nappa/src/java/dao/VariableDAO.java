/*
 * To change this template, choose Tools | Templates
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
 * @author DZuo
 */
public class VariableDAO {
    public static List<String> getVariableTypes() {
        String sql = "select vartype from vartype";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List<String> types = new ArrayList<String>();
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String type = rs.getString(1);
                types.add(type);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return types;
    }
    
    public static boolean isTypeExistIgnoreCase(String type) throws DaoException {
        String sql = "select count(*) from vartype where upper(vartype)=upper(?)";
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, type);
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                count = rs.getInt(1);
            }
            
            if(count>0)
                return true;
            else
                return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DaoException("Cannot insert variable type into database: "+type);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static void addVartype(String type) throws DaoException {
        String sql = "insert into vartype values(?)";
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, type);
            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
            throw new DaoException("Cannot insert variable type into database: "+type);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
