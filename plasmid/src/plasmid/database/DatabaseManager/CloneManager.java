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
        " (cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename, status)"+
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                stmt.setString(13, c.getStatus());
                
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
        " (insertid,insertorder,sizeinbp,species,format,source,cloneid,geneid,name,description,targetseqid,targetgenbank)"+
        " values(?,?,?,?,?,?,?,?,?,?,?,?)";
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
                stmt.setString(8, c.getGeneid());
                stmt.setString(9, c.getName());
                stmt.setString(10, c.getDescription());
                stmt.setString(11, c.getTargetseqid());
                stmt.setString(12, c.getTargetgenbank());
                
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
        " domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename,status"+
        " from clone where cloneid=?";
        String sql2 = "select d.insertid, d.insertorder, d.sizeinbp, d.species, d.format, d.source,d.geneid,d.name,d.description,d.targetseqid,d.targetgenbank"+
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
                String cloneid = (String)cloneids.get(i);
                currentCloneid = Integer.parseInt(cloneid);
                
                stmt.setInt(1, Integer.parseInt(cloneid));
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
                    String status = rs.getString(12);
                    Clone c = new Clone(Integer.parseInt(cloneid),clonename,clonetype,verified,vermethod,domain,subdomain, restriction,comments,vectorid,vectorname,clonemap,status);
                    
                    if(isInsert) {
                        List inserts = new ArrayList();
                        stmt2.setInt(1, Integer.parseInt(cloneid));
                        rs2 = DatabaseTransaction.executeQuery(stmt2);
                        while(rs2.next()) {
                            int insertid = rs2.getInt(1);
                            int insertorder = rs2.getInt(2);
                            int size = rs2.getInt(3);
                            String species = rs2.getString(4);
                            String format = rs2.getString(5);
                            String source = rs2.getString(6);
                            String geneid = rs2.getString(7);
                            String name = rs2.getString(8);
                            String description = rs2.getString(9);
                            String targetseqid = rs2.getString(10);
                            String targetgenbank = rs2.getString(11);
                            DnaInsert insert = new DnaInsert(insertid,insertorder, size, species, format, source, Integer.parseInt(cloneid),geneid,name,description,targetseqid,targetgenbank);
                            inserts.add(insert);
                        }
                        c.setInserts(inserts);
                        DatabaseTransaction.closeResultSet(rs2);
                    }
                    
                    if(isSelection) {
                        List selections = new ArrayList();
                        stmt3.setInt(1, Integer.parseInt(cloneid));
                        rs3 = DatabaseTransaction.executeQuery(stmt3);
                        while(rs3.next()) {
                            String hosttype = rs3.getString(1);
                            String marker = rs3.getString(2);
                            CloneSelection selection = new CloneSelection(Integer.parseInt(cloneid),hosttype,marker);
                            selections.add(selection);
                        }
                        c.setSelections(selections);
                        DatabaseTransaction.closeResultSet(rs3);
                    }
                    
                    clones.put(cloneid, c);
                } else {
                    handleError(null, "No clone record found for cloneid: "+cloneid.toString());
                    return null;
                }
                DatabaseTransaction.closeResultSet(rs);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones by cloneid: "+currentCloneid);
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
        }
        return clones;
    }
    
    public Map queryAvailableClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection) {
        Map clones = new HashMap();
        if(cloneids == null || cloneids.size() == 0)
            return clones;
        
        String sql = "select clonename, clonetype, verified, vermethod,"+
        " domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename,status"+
        " from clone where cloneid=? and status='"+Clone.AVAILABLE+"'";
        String sql2 = "select d.insertid, d.insertorder, d.sizeinbp, d.species, d.format, d.source,d.geneid,d.name,d.description,d.targetseqid,targetgenbank"+
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
                String cloneid = (String)cloneids.get(i);
                currentCloneid = Integer.parseInt(cloneid);
                
                stmt.setInt(1, Integer.parseInt(cloneid));
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
                    String status = rs.getString(12);
                    Clone c = new Clone(Integer.parseInt(cloneid),clonename,clonetype,verified,vermethod,domain,subdomain, restriction,comments,vectorid,vectorname,clonemap,status);
                    
                    if(isInsert) {
                        List inserts = new ArrayList();
                        stmt2.setInt(1, Integer.parseInt(cloneid));
                        rs2 = DatabaseTransaction.executeQuery(stmt2);
                        while(rs2.next()) {
                            int insertid = rs2.getInt(1);
                            int insertorder = rs2.getInt(2);
                            int size = rs2.getInt(3);
                            String species = rs2.getString(4);
                            String format = rs2.getString(5);
                            String source = rs2.getString(6);
                            String geneid = rs2.getString(7);
                            String name = rs2.getString(8);
                            String description = rs2.getString(9);
                            String targetseqid = rs2.getString(10);
                            String targetgenbank = rs2.getString(11);
                            DnaInsert insert = new DnaInsert(insertid,insertorder, size, species, format, source, Integer.parseInt(cloneid),geneid,name,description,targetseqid,targetgenbank);
                            inserts.add(insert);
                        }
                        c.setInserts(inserts);
                        DatabaseTransaction.closeResultSet(rs2);
                    }
                    
                    if(isSelection) {
                        List selections = new ArrayList();
                        stmt3.setInt(1, Integer.parseInt(cloneid));
                        rs3 = DatabaseTransaction.executeQuery(stmt3);
                        while(rs3.next()) {
                            String hosttype = rs3.getString(1);
                            String marker = rs3.getString(2);
                            CloneSelection selection = new CloneSelection(Integer.parseInt(cloneid),hosttype,marker);
                            selections.add(selection);
                        }
                        c.setSelections(selections);
                        DatabaseTransaction.closeResultSet(rs3);
                    }
                    
                    clones.put(cloneid, c);
                } else {
                    handleError(null, "No clone record found for cloneid: "+cloneid.toString());
                    return null;
                }
                DatabaseTransaction.closeResultSet(rs);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones by cloneid: "+currentCloneid);
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
        }
        return clones;
    }
    
    public Clone queryCloneByCloneid(int cloneid) {
        String sql = "select clonename, clonetype, verified, vermethod,"+
        " domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename, status"+
        " from clone where cloneid="+cloneid;
        String sql2 = "select d.insertid, d.insertorder, d.sizeinbp, d.species, d.format, d.source,d.geneid,d.name,d.description,d.targetseqid,d.targetgenbank"+
        " from dnainsert d, clone c"+
        " where c.cloneid=d.cloneid and c.cloneid="+cloneid;
        String sql3 = "select hosttype, marker from cloneselection where cloneid="+cloneid;
        String sql4 = "select c.cloneid,c.authorid,a.authorname,c.authortype"+
                    " from cloneauthor c, authorinfo a"+
                    " where c.authorid=a.authorid"+
                    " and c.cloneid="+cloneid;
        String sql5 = "select g.growthid,name, hosttype,antibioticselection,growthcondition,comments"+
                    " from clonegrowth c, growthcondition g"+
                    " where c.growthid=g.growthid"+
                    " and c.cloneid="+cloneid+
                    " and c.isrecommended='"+CloneGrowth.YES+"'";
        String sql6 = "select p.publicationid,p.title, p.pmid"+
                    " from publication p, clonepublication c"+
                    " where p.publicationid=c.publicationid"+
                    " and c.cloneid = "+cloneid;
        String sql7 = "select h.cloneid, h.hoststrain, h.isinuse, h.description"+
                    " from host h where h.cloneid="+cloneid;
        String sql8 = "select cloneid,nametype,namevalue,nameurl"+
                    " from clonename where cloneid="+cloneid;
        
        Clone c = null;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = t.executeQuery(sql);
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
                String status = rs.getString(12);
                c = new Clone(cloneid,clonename,clonetype,verified,vermethod,domain,subdomain, restriction,comments,vectorid,vectorname,clonemap,status);
                
                List inserts = new ArrayList();
                ResultSet rs2 = t.executeQuery(sql2);
                while(rs2.next()) {
                    int insertid = rs2.getInt(1);
                    int insertorder = rs2.getInt(2);
                    int size = rs2.getInt(3);
                    String species = rs2.getString(4);
                    String format = rs2.getString(5);
                    String source = rs2.getString(6);
                    String geneid = rs2.getString(7);
                    String name = rs2.getString(8);
                    String description = rs2.getString(9);
                    String targetseqid = rs2.getString(10);
                    String targetgenbank = rs2.getString(11);
                    DnaInsert insert = new DnaInsert(insertid,insertorder, size, species, format, source, cloneid,geneid,name,description,targetseqid,targetgenbank);
                    inserts.add(insert);
                }
                c.setInserts(inserts);
                DatabaseTransaction.closeResultSet(rs2);
                
                List selections = new ArrayList();
                ResultSet rs3 = t.executeQuery(sql3);
                while(rs3.next()) {
                    String hosttype = rs3.getString(1);
                    String marker = rs3.getString(2);
                    CloneSelection selection = new CloneSelection(cloneid,hosttype,marker);
                    selections.add(selection);
                }
                c.setSelections(selections);
                DatabaseTransaction.closeResultSet(rs3);
                
                List authors = new ArrayList();
                ResultSet rs4 = t.executeQuery(sql4);
                while(rs4.next()) {
                    int id = rs4.getInt(1);
                    int authorid = rs4.getInt(2);
                    String authorname = rs4.getString(3);
                    String authortype = rs4.getString(4);
                    CloneAuthor a = new CloneAuthor(id, authorid, authorname, authortype);
                    authors.add(a);
                }
                c.setAuthors(authors);
                DatabaseTransaction.closeResultSet(rs4);
                
                ResultSet rs5 = t.executeQuery(sql5);
                if(rs5.next()) {
                    int growthid = rs5.getInt(1);
                    String growthname = rs5.getString(2);
                    String hosttype = rs5.getString(3);
                    String antibioticselection = rs5.getString(4);
                    String condition = rs5.getString(5);
                    String com = rs5.getString(6);
                    GrowthCondition gc = new GrowthCondition(growthid,growthname,hosttype,antibioticselection,condition,com);
                    c.setRecommendedGrowthCondition(gc);
                }
                DatabaseTransaction.closeResultSet(rs5);
                
                ResultSet rs6 = t.executeQuery(sql6);
                List publications = new ArrayList();
                while(rs6.next()) {
                    int publicationid = rs6.getInt(1);
                    String title = rs6.getString(2);
                    String pmid = rs6.getString(3);
                    Publication p = new Publication(publicationid,title,pmid);
                    publications.add(p);
                }
                c.setPublications(publications);
                DatabaseTransaction.closeResultSet(rs6);
                
                ResultSet rs7 = t.executeQuery(sql7);
                List hosts = new ArrayList();
                while(rs7.next()) {
                    int id=rs7.getInt(1);
                    String strain = rs7.getString(2);
                    String isinuse = rs7.getString(3);
                    String description = rs7.getString(4);
                    CloneHost h = new CloneHost(id,strain,isinuse,description);
                    hosts.add(h);
                }
                c.setHosts(hosts);
                DatabaseTransaction.closeResultSet(rs7);
                
                ResultSet rs8 = t.executeQuery(sql8);
                List names = new ArrayList();
                while(rs8.next()) {
                    int id = rs8.getInt(1);
                    String nametype = rs8.getString(2);
                    String namevalue = rs8.getString(3);
                    String nameurl = rs8.getString(4);
                    CloneName n = new CloneName(id, nametype, namevalue, nameurl);
                    names.add(n);
                }
                c.setNames(names);
                DatabaseTransaction.closeResultSet(rs8);
            } else {
                handleError(null, "No clone record found for cloneid: "+cloneid);
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            System.out.println(ex);
            handleError(ex, "Error occured while query clones by cloneid: "+cloneid);
        }
        return c;
    }
    
    public static void main(String args[]) {
        DatabaseTransaction dt = null;
        Connection conn = null;
        try {
            dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
            CloneManager manager = new CloneManager(conn);
            Clone c = manager.queryCloneByCloneid(5096);
            List names = c.getNames();
            List authors = c.getAuthors();
            GrowthCondition growths = c.getRecommendedGrowthCondition();
            System.out.println(names);
            System.out.println(authors);
            System.out.println(growths);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
}