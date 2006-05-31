/*
 * PublicationManager.java
 *
 * Created on April 14, 2005, 10:48 AM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class PublicationManager extends TableManager {
    
    /** Creates a new instance of PublicationManager */
    public PublicationManager(Connection conn) {
       super(conn);
    }
    
    public boolean insertPublications(List publications) {
        if(publications == null)
            return true;
        
        String sql = "insert into publication"+
        " (publicationid, title, pmid)"+
        " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<publications.size(); i++) {
                Publication p = (Publication)publications.get(i);
                stmt.setInt(1, p.getPublicationid());
                stmt.setString(2, p.getTitle());
                stmt.setString(3, p.getPmid());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into PUBLICATION table");
            return false;
        }
        return true;
    }   
        
    public int getPublicationid(String name) {
        String sql = "select PUBLICATIONID from publication where PMID=?";
        int id = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                id = rs.getInt(1);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        }
        
        return id;
    }
}
