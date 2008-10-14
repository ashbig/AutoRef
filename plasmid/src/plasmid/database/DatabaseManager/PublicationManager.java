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
    
    public boolean insertPublication(Publication p) {
        if(p == null)
            return true;
        
        String sql = "insert into publication"+
        " (publicationid, title, pmid)"+
        " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
                stmt.setInt(1, p.getPublicationid());
                stmt.setString(2, p.getTitle());
                stmt.setString(3, p.getPmid());
                
                DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into PUBLICATION table");
            return false;
        }
        return true;
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
    
    public List getVectorPublicationsByVectorid(int vid) {
        if (vid < 1)
            return null;
        
        List pms = new ArrayList();
        String sql = "select a.vectorid, a.publicationid, b.title, b.pmid from vectorpublication a, publication b where a.vectorid=? order by b.pmid";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vid);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                Publication p = new Publication(rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(1));
                pms.add(p);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        }
        return pms;
    }

    public boolean updateVectorPublications(List publications) {
        if(publications == null)
            return true;
        
        String sql1 = "delete from vectorpublication where vectorid=?";
        String sql2 = "insert into vectorpublication (vectorid, publicationid) values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql1);
            DatabaseTransaction.executeUpdate(stmt);
            stmt = conn.prepareStatement(sql2);
            
            for(int i=0; i<publications.size(); i++) {
                Publication p = (Publication) publications.get(i);
                stmt.setInt(1, p.getVectorid());
                stmt.setInt(2, p.getPublicationid());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPUBLICATION table");
            return false;
        }
        return true;
    }    
    
    public List getPublicationsByPMID(String PMID) {
        if ((PMID == null) || (PMID.length() < 1)) {
            return null;
        }
        List pms = new ArrayList();
        String sql = "select publicationid, title, pmid from publication where pmid like ? order by pmid";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, PMID + "%");
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                Publication p = new Publication(rs.getInt(1), rs.getString(2), rs.getString(3));
                pms.add(p);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        }
        return pms;
    }
}
