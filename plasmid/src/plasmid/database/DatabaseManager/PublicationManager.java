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

/**
 *
 * @author  DZuo
 */
public class PublicationManager extends TableManager {
    
    private Connection conn;
    
    /** Creates a new instance of PublicationManager */
    public PublicationManager(Connection conn) {
        this.conn = conn;
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
}
