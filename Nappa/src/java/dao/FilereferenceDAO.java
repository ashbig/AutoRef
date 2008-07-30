/*
 * FilereferenceDAO.java
 *
 * Created on October 2, 2007, 1:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dao;

import database.DatabaseTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import transfer.FilereferenceTO;

/**
 *
 * @author dzuo
 */
public class FilereferenceDAO {
    private Connection conn = null;
    
    /** Creates a new instance of FilereferenceDAO */
    public FilereferenceDAO() {
    }
    
    public FilereferenceDAO(Connection conn) {
        this.conn = conn;
    }
    
public void setFilereferenceids(List<FilereferenceTO> files) throws DaoException   {
        int id = SequenceDAO.getNextid("filereference", "filereferenceid");
        for(FilereferenceTO f:files) {
            f.setFilereferenceid(id);
            id++;
        }
    }
    public void addFilereferences(List<FilereferenceTO> files) throws DaoException {
        setFilereferenceids(files);
        
        String sql = "insert into filereference(filereferenceid,filetype,basename,localpath)"+
                " values(?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for(FilereferenceTO f:files) {
                stmt.setInt(1, f.getFilereferenceid());
                stmt.setString(2, f.getFiletype());
                stmt.setString(3, f.getFilename());
                stmt.setString(4, f.getFilepath());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while inserting into filereference table."+ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public static FilereferenceTO getFilereference(int id) throws DaoException {
        String sql = "select filetype, basename, localpath from filereference where filereferenceid="+id;
        DatabaseTransaction t = null; 
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                String filetype = rs.getString(1);
                String name = rs.getString(2);
                String path = rs.getString(3);
                FilereferenceTO f = new FilereferenceTO(id, name, path, filetype);
                return f;
            }
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return null;
    }
}
