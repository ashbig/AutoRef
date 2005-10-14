/*
 * CloneManager.java
 *
 * Created on April 14, 2005, 11:19 AM
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
public class CloneManager extends TableManager {
    private Connection conn;
    
    /** Creates a new instance of CloneManager */
    public CloneManager(Connection conn) {
        this.conn = conn;
    }
    
    public boolean insertClones(List clones) {
        if(clones == null)
            return true;
        
        String sql = "inset into clone"+
                    " (cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname)"+
                    " values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<clones.size(); i++) {
                Clone c = (Clone)clones.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getName());
                stmt.setString(3, c.getType());
                stmt.setString(4, c.getVerified());
                stmt.setString(5, c.getVermethod());
                stmt.setString(6, c.getDomain());
                stmt.setString(7, c.getSubdomain());
                stmt.setString(8, c.getRestriction());
                stmt.setString(9, c.getComments());
                stmt.setInt(10, c.getVectorid());
                stmt.setString(11, c.getVectorname());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONE table");
            return false;
        }
        return true;
    }   
        
    public boolean insertCloneGrowths(List growths) {
        if(growths == null)
            return true;
        
        String sql = "inset into clonegrowth"+
                    " (cloneid,growthid,isrecommended)"+
                    " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<growths.size(); i++) {
                CloneGrowth c = (CloneGrowth)growths.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setInt(2, c.getGrowthid());
                stmt.setString(3, c.getIsrecommended());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEGROWTH table");
            return false;
        }
        return true;
    }   
        
    public boolean insertCloneSelections(List selections) {
        if(selections == null)
            return true;
        
        String sql = "inset into cloneselection"+
                    " (cloneid,hosttype,marker)"+
                    " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<selections.size(); i++) {
                CloneSelection c = (CloneSelection)selections.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getHosttype());
                stmt.setString(3, c.getMarker());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONESELECTION table");
            return false;
        }
        return true;
    }   
            
    public boolean insertCloneHosts(List hosts) {
        if(hosts == null)
            return true;
        
        String sql = "inset into host"+
                    " (cloneid,hoststrain,isinuse,description)"+
                    " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<hosts.size(); i++) {
                CloneHost c = (CloneHost)hosts.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getHoststrain());
                stmt.setString(3, c.getIsinuse());
                stmt.setString(4, c.getDescription());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into HOST table");
            return false;
        }
        return true;
    }  
                
    public boolean insertCloneNameTypes(List types) {
        if(types == null)
            return true;
        
        String sql = "inset into clonenametype"+
                    " (clonetype, nametype, use)"+
                    " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<types.size(); i++) {
                CloneNameType c = (CloneNameType)types.get(i);
                stmt.setString(1, c.getClonetype());
                stmt.setString(2, c.getNametype());
                stmt.setString(3, c.getUse());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONENAMETYPE table");
            return false;
        }
        return true;
    }   
                    
    public boolean insertCloneNames(List names) {
        if(names == null)
            return true;
        
        String sql = "inset into clonename"+
                    " (cloneid, nametype, namevalue, nameurl)"+
                    " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<names.size(); i++) {
                CloneName c = (CloneName)names.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getType());
                stmt.setString(3, c.getValue());
                stmt.setString(4, c.getUrl());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONENAME table");
            return false;
        }
        return true;
    }
                        
    public boolean insertCloneInserts(List inserts) {
        if(inserts == null)
            return true;
        
        String sql = "inset into dnainsert"+
                    " (insertid,insertorder,sizeinbp,species,format,source,cloneid)"+
                    " values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<inserts.size(); i++) {
                DnaInsert c = (DnaInsert)inserts.get(i);
                stmt.setInt(1, c.getInsertid());
                stmt.setInt(2, c.getOrder());
                stmt.setInt(3, c.getSize());
                stmt.setString(4, c.getSpecies());
                stmt.setString(5, c.getFormat());
                stmt.setString(6, c.getSource());
                stmt.setInt(7, c.getCloneid());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into DNAINSERT table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneAuthors(List authors) {
        if(authors == null)
            return true;
        
        String sql = "inset into cloneauthor"+
                    " (cloneid, authorid, authortype)"+
                    " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<authors.size(); i++) {
                CloneAuthor c = (CloneAuthor)authors.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setInt(2, c.getAuthorid());
                stmt.setString(3, c.getAuthortype());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEAUTHOR table");
            return false;
        }
        return true;
    }  
        
    public boolean insertClonePublications(List pubs) {
        if(pubs == null)
            return true;
        
        String sql = "inset into clonepublication"+
                    " (cloneid, publicationid)"+
                    " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<pubs.size(); i++) {
                ClonePublication c = (ClonePublication)pubs.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setInt(2, c.getPublicationid());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEPUBLICATION table");
            return false;
        }
        return true;
    }  
}
