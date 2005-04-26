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
        
        String sql = "insert into clone"+
        " (cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename)"+
        " values(?,?,?,?,?,?,?,?,?,?,?,?)";
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
                stmt.setString(12, c.getClonemap());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONE table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneGrowths(List growths) {
        if(growths == null)
            return true;
        
        String sql = "insert into clonegrowth"+
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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEGROWTH table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneSelections(List selections) {
        if(selections == null)
            return true;
        
        String sql = "insert into cloneselection"+
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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONESELECTION table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneHosts(List hosts) {
        if(hosts == null)
            return true;
        
        String sql = "insert into host"+
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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into HOST table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneNameTypes(List types) {
        if(types == null)
            return true;
        
        String sql = "insert into clonenametype"+
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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONENAMETYPE table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneNames(List names) {
        if(names == null)
            return true;
        
        String sql = "insert into clonename"+
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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONENAME table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneInserts(List inserts) {
        if(inserts == null)
            return true;
        
        String sql = "insert into dnainsert"+
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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into DNAINSERT table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneAuthors(List authors) {
        if(authors == null)
            return true;
        
        String sql = "insert into cloneauthor"+
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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEAUTHOR table");
            return false;
        }
        return true;
    }
    
    public boolean insertClonePublications(List pubs) {
        if(pubs == null)
            return true;
        
        String sql = "insert into clonepublication"+
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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEPUBLICATION table");
            return false;
        }
        return true;
    }
    
    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection) {
        Map clones = new HashMap();
        if(cloneids == null || cloneids.size() == 0)
            return clones;
        
        String sql = "select clonename, clonetype, verified, vermethod,"+
        " domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename"+
        " from clone where cloneid=?";
        String sql2 = "select insertid, insertorder, sizeinbp, species, format, source"+
        " from dnainsert d, clone c"+
        " where c.cloneid=d.cloneid and c.cloneid=?";
        String sql3 = "select hosttype, marker from cloneselection where cloneid=?";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        int currentCloneid = 0;
        try {
            stmt = conn.prepareStatement(sql);
            if(isInsert)
                stmt2 = conn.prepareStatement(sql2);
            if(isSelection)
                stmt3 = conn.prepareStatement(sql3);
            
            for(int i=0; i<cloneids.size(); i++) {
                Integer cloneid = (Integer)cloneids.get(0);
                currentCloneid = cloneid.intValue();
                
                stmt.setInt(1, cloneid.intValue());
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    String clonename = rs.getString(1);
                    String clonetype = rs.getString(2);
                    String verified = rs.getString(3);
                    String vermethod = rs.getString(4);
                    String domain = rs.getString(5);
                    String subdomain = rs.getString(6);
                    String restriction = rs.getString(7);
                    String comments = rs.getString(8);
                    int vectorid = rs.getInt(9);
                    String vectorname = rs.getString(10);
                    String clonemap = rs.getString(11);
                    Clone c = new Clone(cloneid.intValue(),clonename,clonetype,verified,vermethod,domain,subdomain, restriction,comments,vectorid,vectorname,clonemap);
                    
                    if(isInsert) {
                        List inserts = new ArrayList();
                        stmt2.setInt(1, cloneid.intValue());
                        rs2 = DatabaseTransaction.executeQuery(stmt2);
                        while(rs2.next()) {
                            int insertid = rs.getInt(1);
                            int insertorder = rs.getInt(2);
                            int size = rs.getInt(3);
                            String species = rs.getString(4);
                            String format = rs.getString(5);
                            String source = rs.getString(6);
                            DnaInsert insert = new DnaInsert(insertid,insertorder, size, species, format, source, cloneid.intValue());
                            inserts.add(insert);
                        }
                        c.setInserts(inserts);
                    }
                    
                    if(isSelection) {
                        List selections = new ArrayList();
                        stmt3.setInt(1, cloneid.intValue());
                        rs3 = DatabaseTransaction.executeQuery(stmt3);
                        while(rs3.next()) {
                            String hosttype = rs.getString(1);
                            String marker = rs.getString(2);
                            CloneSelection selection = new CloneSelection(cloneid.intValue(),hosttype,marker);
                            selections.add(selection);
                        }
                        c.setSelections(selections);
                    }
                    
                    clones.put(cloneid, c);
                } else {
                    handleError(null, "No clone record found for cloneid: "+cloneid.toString());
                    return null;
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones by cloneid: "+currentCloneid);
            return null;
        }
        return clones;
    }
    
    public static void main(String args[]) {
        Clone clone = new Clone(1,"54934", "cDNA", "Y","Sequence Verification", "Homo sapiens", null, "No restriction", null, 1, "vectorname", null);
        List clones = new ArrayList();
        clones.add(clone);
        DatabaseTransaction dt = null;
        Connection conn = null;
        try {
            dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
            CloneManager manager = new CloneManager(conn);
            manager.insertClones(clones);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
}