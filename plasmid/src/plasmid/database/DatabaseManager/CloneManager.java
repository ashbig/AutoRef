/*
 * CloneManager.java
 *
 * Created on April 14, 2005, 11:19 AM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class CloneManager extends TableManager {
    
    /** Creates a new instance of CloneManager */
    public CloneManager(Connection conn) {
        super(conn);
    }
    
    public boolean insertClones(List clones) {
        if(clones == null)
            return true;
        
        String sql = "insert into clone"+
        " (cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename, status, specialtreatment, source, description)"+
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                stmt.setString(14, c.getSpecialtreatment());
                stmt.setString(15, c.getSource());
                stmt.setString(16, c.getDescription());
                
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
                //System.out.println(c.getCloneid()+"\t"+c.getGrowthid());
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
                //System.out.println(c.getType()+"\t"+c.getValue());
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
        " (insertid,insertorder,sizeinbp,species,format,source,geneid,name,description,targetseqid,targetgenbank,hasdiscrepancy,hasmutation,refseqid,region)"+
        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql1 = "insert into cloneinsert (cloneid, insertid) values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            
            for(int i=0; i<inserts.size(); i++) {
                DnaInsert c = (DnaInsert)inserts.get(i);
                stmt.setInt(1, c.getInsertid());
                stmt.setInt(2, c.getOrder());
                
                int size = c.getSize();
                if(size>0)
                    stmt.setInt(3, size);
                else
                    stmt.setString(3, null);
                
                stmt.setString(4, c.getSpecies());
                stmt.setString(5, c.getFormat());
                stmt.setString(6, c.getSource());
                stmt.setString(7, c.getGeneid());
                stmt.setString(8, c.getName());
                stmt.setString(9, c.getDescription());
                stmt.setString(10, c.getTargetseqid());
                stmt.setString(11, c.getTargetgenbank());
                stmt.setString(12, c.getHasdiscrepancy());
                stmt.setString(13, c.getHasmutation());
                stmt.setString(15, c.getRegion());
                
                int refseqid = c.getRefseqid();
                if(refseqid > 0) {
                    stmt.setInt(14, refseqid);
                } else {
                    stmt.setString(14, null);
                }
                //System.out.println(c.getSpecies());
                DatabaseTransaction.executeUpdate(stmt);
                
                stmt1.setInt(1, c.getCloneid());
                stmt1.setInt(2, c.getInsertid());
                DatabaseTransaction.executeUpdate(stmt1);
            }
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt1);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into DNAINSERT table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneInsertsWithoutInsertInfo(List inserts) {
        if(inserts == null)
            return true;
        
        String sql1 = "insert into cloneinsert (cloneid, insertid) values(?,?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            
            for(int i=0; i<inserts.size(); i++) {
                DnaInsert c = (DnaInsert)inserts.get(i);                
                stmt1.setInt(1, c.getCloneid());
                stmt1.setInt(2, c.getInsertid());
                DatabaseTransaction.executeUpdate(stmt1);
            }
            DatabaseTransaction.closeStatement(stmt1);
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
                //System.out.println(c.getCloneid()+"\t"+c.getPublicationid());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEPUBLICATION table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneProperties(List properties) {
        if(properties == null)
            return true;
        
        String sql = "insert into cloneproperty"+
        " (cloneid, propertytype,propertyvalue,extrainfo)"+
        " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<properties.size(); i++) {
                CloneProperty c = (CloneProperty)properties.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getType());
                stmt.setString(3, c.getValue());
                stmt.setString(4, c.getExtrainfo());
                //System.out.println(c.getCloneid());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEPROPERTY table");
            return false;
        }
        return true;
    }
    
    public boolean insertCloneCollections(List collections) {
        if(collections == null)
            return true;
        
        String sql = "insert into clonecollection"+
        " (cloneid, name)"+
        " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<collections.size(); i++) {
                CloneCollection c = (CloneCollection)collections.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getName());
                //System.out.println(c.getName());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONECOLLECTION table");
            return false;
        }
        return true;
    }
    
    public boolean insertInsertProperties(List properties) {
        if(properties == null)
            return true;
        
        String sql = "insert into insertproperty"+
        " (insertid, propertytype,propertyvalue,extrainfo)"+
        " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<properties.size(); i++) {
                InsertProperty c = (InsertProperty)properties.get(i);
                stmt.setInt(1, c.getInsertid());
                stmt.setString(2, c.getType());
                stmt.setString(3, c.getValue());
                stmt.setString(4, c.getExtrainfo());
                
                //System.out.println(c.getType()+"\t"+c.getValue());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into INSERTPROPERTY table");
            return false;
        }
        return true;
    }
    
    /**
     * Query the database to get a list of Clone objects for given list of cloneid.
     * @param cloneids A list of cloneid as String.
     * @param isInsert Insert will be queried if it is true.
     * @param isSelection Selection will be queried if it is true.
     * @param isWorkingStorage Working storage location will be queried if it is true.
     * @return A map with cloneid as the key and Clone object as the value. Return null if
     *  error occured.
     */
    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth, List restrictions, List clonetypes, String species, String status) {
        String sq = "select clonename, clonetype, verified, vermethod,"+
        " domain, subdomain, restriction, comments, vectorid, vectorname,"+
        " clonemapfilename,status,specialtreatment,source,description,cloneid"+
        " from clone where cloneid =?";
       
        if(clonetypes != null) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sq = sq+" and clonetype in ("+s+")";
        }
        
        if(restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sq = sq+" and restriction in ("+s+")";
        }
        
        if(species != null) {
            sq = sq+" and domain='"+species+"'";
        }
        
        if(status != null)
            sq += " and status='"+status+"'";
        
        return performQueryClones(cloneids, isInsert, isSelection, isWorkingStorage, isGrowth, sq);
    }
        
    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, List restrictions, List clonetypes, String species, String status) {
        return queryClonesByCloneid(cloneids, isInsert, isSelection, isWorkingStorage, true, restrictions, clonetypes, species, status);
    }
    
    /**
     * Query the database to get a list of Clone objects for given list of cloneid.
     * @param cloneids A list of cloneid as String.
     * @param isInsert Insert will be queried if it is true.
     * @param isSelection Selection will be queried if it is true.
     * @param isWorkingStorage Working storage location will be queried if it is true.
     * @return A map with cloneid as the key and Clone object as the value. Return null if
     *  error occured.
     */
    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage) {
        return queryClonesByCloneid(cloneids, isInsert, isSelection, isWorkingStorage, null, null, null, null);
    }
    
    public Map queryAvailableClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage) {
        return queryClonesByCloneid(cloneids, isInsert, isSelection, isWorkingStorage, null, null, null, Clone.AVAILABLE);
    }
    
    public Map queryAvailableClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, List restrictions, List clonetypes, String species) {
        return queryClonesByCloneid(cloneids, isInsert, isSelection, isWorkingStorage, restrictions, clonetypes, species, Clone.AVAILABLE);
    }
    
    public Clone queryCloneByCloneid(int cloneid) {
        String sql = "select clonename, clonetype, verified, vermethod,"+
        " domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename, status,specialtreatment,source,description"+
        " from clone where cloneid="+cloneid;
        String sql2 = "select d.insertid, d.insertorder, d.sizeinbp, d.species, d.format, d.source,d.geneid,d.name,d.description,d.targetseqid,d.targetgenbank,d.hasdiscrepancy,d.hasmutation,d.refseqid,d.region"+
        " from dnainsert d, cloneinsert cd, clone c"+
        " where c.cloneid=cd.cloneid and cd.insertid=d.insertid and c.cloneid="+cloneid;
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
        String sql9 = "select vectorid,name,description,form,type,sizeinbp,mapfilename,sequencefilename,comments"+
        " from vector where vectorid=?";
        String sql10 = "select propertytype from vectorproperty where vectorid=?";
        String sql11 = "select propertytype,propertyvalue,extrainfo from cloneproperty where cloneid="+cloneid;
        String sql12 = "select propertytype,propertyvalue,extrainfo from insertproperty where insertid=?";
        String sql13 = "select seqorder,seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.insertid=? order by seqorder";
        
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
                String specialtreatment = rs.getString(13);
                String src = rs.getString(14);
                String d = rs.getString(15);
                c = new Clone(cloneid,clonename,clonetype,verified,vermethod,domain,subdomain, restriction,comments,vectorid,vectorname,clonemap,status,specialtreatment,src,d);
                
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
                    String hasdiscrepancy = rs2.getString(12);
                    String hasmutation = rs2.getString(13);
                    int refseqid = rs2.getInt(14);
                    String region = rs2.getString(15);
                    DnaInsert insert = new DnaInsert(insertid,insertorder, size, species, format, source, cloneid,geneid,name,description,targetseqid,targetgenbank,hasdiscrepancy,hasmutation,region,refseqid);
                    
                    PreparedStatement stmt1 = conn.prepareStatement(sql12);
                    stmt1.setInt(1, insertid);
                    ResultSet rs12 = DatabaseTransaction.executeQuery(stmt1);
                    List properties = new ArrayList();
                    while(rs12.next()) {
                        String type = rs12.getString(1);
                        String value = rs12.getString(2);
                        String extrainfo = rs12.getString(3);
                        InsertProperty p = new InsertProperty(insertid, type, value, extrainfo);
                        properties.add(p);
                    }
                    DatabaseTransaction.closeResultSet(rs12);
                    DatabaseTransaction.closeStatement(stmt1);
                    
                    PreparedStatement stmt2 = conn.prepareStatement(sql13);
                    stmt2.setInt(1, insertid);
                    ResultSet rs13 = DatabaseTransaction.executeQuery(stmt2);
                    String seq = "";
                    while(rs13.next()) {
                        int seqorder = rs13.getInt(1);
                        String seqtext = rs13.getString(2);
                        seq += seqtext;
                    }
                    DatabaseTransaction.closeResultSet(rs13);
                    DatabaseTransaction.closeStatement(stmt2);
                    
                    insert.setProperties(properties);
                    insert.setSequence(seq);
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
                
                PreparedStatement stmt = conn.prepareStatement(sql9);
                stmt.setInt(1, c.getVectorid());
                ResultSet rs9 = DatabaseTransaction.executeQuery(stmt);
                if(rs9.next()) {
                    int vid = rs9.getInt(1);
                    String vname = rs9.getString(2);
                    String desc = rs9.getString(3);
                    String form = rs9.getString(4);
                    String type = rs9.getString(5);
                    int size = rs9.getInt(6);
                    String mapfile = rs9.getString(7);
                    String seqfile = rs9.getString(8);
                    String vcomments = rs9.getString(9);
                    CloneVector v = new CloneVector(vid, vname,desc,form,type,size,mapfile,seqfile,vcomments);
                    
                    PreparedStatement stmt1 = conn.prepareStatement(sql10);
                    stmt1.setInt(1, vid);
                    ResultSet rs10 = DatabaseTransaction.executeQuery(stmt1);
                    List properties = new ArrayList();
                    while(rs10.next()) {
                        String p = rs10.getString(1);
                        properties.add(p);
                    }
                    v.setProperty(properties);
                    c.setVector(v);
                    
                    DatabaseTransaction.closeResultSet(rs10);
                    DatabaseTransaction.closeStatement(stmt1);
                }
                DatabaseTransaction.closeResultSet(rs9);
                DatabaseTransaction.closeStatement(stmt);
                
                ResultSet rs11 = t.executeQuery(sql11);
                List cloneproperties = new ArrayList();
                while(rs11.next()) {
                    String type = rs11.getString(1);
                    String value = rs11.getString(2);
                    String extrainfo = rs11.getString(3);
                    CloneProperty n = new CloneProperty(cloneid, type, value, extrainfo);
                    cloneproperties.add(n);
                }
                c.setProperties(cloneproperties);
                DatabaseTransaction.closeResultSet(rs11);
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
    
    private Map performQueryClones(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth, String sql) {
        Map clones = new TreeMap();
        if(cloneids == null || cloneids.size() == 0)
            return clones;
        
        String sql2 = getInsertSql();
        String sql3 = getSelectionSql();
        String sql4 = getStorageSql();
        String sql5 = getGrowthSql();
        
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        
        ResultSet rs = null;
        
        int currentCloneid = 0;
        try {
            stmt = conn.prepareStatement(sql);
            if(isInsert)
                stmt2 = conn.prepareStatement(sql2);
            if(isSelection)
                stmt3 = conn.prepareStatement(sql3);
            if(isWorkingStorage)
                stmt4 = conn.prepareStatement(sql4);
            if(isGrowth)
                stmt5 = conn.prepareStatement(sql5);
            
            for(int i=0; i<cloneids.size(); i++) {
                String cid = (String)cloneids.get(i);
                currentCloneid = Integer.parseInt(cid);
                stmt.setInt(1, currentCloneid);
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
                    String specialtreatment = rs.getString(13);
                    String src = rs.getString(14);
                    String des = rs.getString(15);
                    int cloneid = rs.getInt(16);
                    CloneInfo c = new CloneInfo(cloneid,clonename,clonetype,verified,vermethod,domain,subdomain, restriction,comments,vectorid,vectorname,clonemap,status,specialtreatment,src,des);
                    
                    if(isInsert) {
                        stmt2.setInt(1, cloneid);
                        List inserts = getInserts(stmt2, cloneid);
                        c.setInserts(inserts);
                    }
                    
                    if(isSelection) {
                        stmt3.setInt(1, cloneid);
                        List selections = getSelections(stmt3, cloneid);
                        c.setSelections(selections);
                    }
                    
                    if(isWorkingStorage) {
                        stmt4.setInt(1, cloneid);
                        stmt4.setString(2, Sample.WORKING_GLYCEROL);
                        setStorage(stmt4, c);
                    }
                    
                    if(isGrowth) {
                        stmt5.setInt(1, cloneid);
                        GrowthCondition g = getRecommendedGrowth(stmt5, cloneid);
                        c.setRecommendedGrowthCondition(g);
                    }
                    
                    clones.put(cid, c);
                } else {
                    clones.put(cid, null);
                }
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones");
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeStatement(stmt4);
            DatabaseTransaction.closeStatement(stmt5);
        }
        return clones;
    }
        
    private Map performQueryClones(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, String sql) {
        return performQueryClones(cloneids, isInsert, isSelection, isWorkingStorage, false, sql);
    }

    public List performQueryClones(List clones, boolean isInsert, boolean isSelection, boolean isWorkingStorage) {
        List infos = new ArrayList();
        if(clones == null || clones.size() == 0)
            return infos;
        
        String sql2 = getInsertSql();
        String sql3 = getSelectionSql();
        String sql4 = getStorageSql();
        
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        
        int currentCloneid = 0;
        try {
            if(isInsert)
                stmt2 = conn.prepareStatement(sql2);
            if(isSelection)
                stmt3 = conn.prepareStatement(sql3);
            if(isWorkingStorage)
                stmt4 = conn.prepareStatement(sql4);
            
            for(int i=0; i<clones.size(); i++) {
                Clone clone = (Clone)clones.get(i);
                CloneInfo c = new CloneInfo(null, clone);
                int cloneid = clone.getCloneid();
                currentCloneid = cloneid;
                
                if(isInsert) {
                    stmt2.setInt(1,cloneid);
                    List inserts = getInserts(stmt2, cloneid);
                    c.setInserts(inserts);
                }
                
                if(isSelection) {
                    stmt3.setInt(1, cloneid);
                    List selections = getSelections(stmt3, cloneid);
                    c.setSelections(selections);
                }
                
                if(isWorkingStorage) {
                    stmt4.setInt(1, cloneid);
                    stmt4.setString(2, Sample.WORKING_GLYCEROL);
                    setStorage(stmt4, c);
                }
                infos.add(c);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones by cloneid: "+currentCloneid);
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt4);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
        }
        return infos;
    }
    
    private String getInsertSql() {
        return "select d.insertid, d.insertorder, d.sizeinbp, d.species, d.format, d.source,d.geneid,d.name,d.description,d.targetseqid,d.targetgenbank,d.hasdiscrepancy,d.hasmutation,d.refseqid,d.region"+
        " from dnainsert d, cloneinsert cd, clone c"+
        " where d.insertid=cd.insertid and c.cloneid=cd.cloneid and c.cloneid=?";
    }
    
    private String getSelectionSql() {
        return "select hosttype, marker from cloneselection where cloneid=?";
    }
    
    private String getStorageSql() {
        return "select containerlabel, position, positionx, positiony from sample where cloneid=? and sampletype=? order by sampleid desc";
    }
    
    private String getGrowthSql() {
        String s = "select g.growthid, g.name, g.hosttype, g.antibioticselection, g.growthcondition, g.comments"+
        " from growthcondition g, clonegrowth c where g.growthid=c.growthid"+
        " and c.isrecommended='"+CloneGrowth.YES+"' and c.cloneid=?";
        return s;
    }
    
    private List getInserts(PreparedStatement stmt, int cloneid) throws Exception {
        List inserts = new ArrayList();
        ResultSet rs2 = DatabaseTransaction.executeQuery(stmt);
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
            String hasdiscrepancy = rs2.getString(12);
            String hasmutation = rs2.getString(13);
            int refseqid = rs2.getInt(14);
            String region = rs2.getString(15);
            DnaInsert insert = new DnaInsert(insertid,insertorder, size, species, format, source, cloneid,geneid,name,description,targetseqid,targetgenbank,hasdiscrepancy,hasmutation,region,refseqid);
            inserts.add(insert);
        }
        DatabaseTransaction.closeResultSet(rs2);
        
        return inserts;
    }
    
    private List getSelections(PreparedStatement stmt, int cloneid) throws Exception {
        List selections = new ArrayList();
        ResultSet rs3 = DatabaseTransaction.executeQuery(stmt);
        while(rs3.next()) {
            String hosttype = rs3.getString(1);
            String marker = rs3.getString(2);
            CloneSelection selection = new CloneSelection(cloneid,hosttype,marker);
            selections.add(selection);
        }
        DatabaseTransaction.closeResultSet(rs3);
        
        return selections;
    }
    
    private void setStorage(PreparedStatement stmt, CloneInfo c) throws Exception {
        ResultSet rs4 = DatabaseTransaction.executeQuery(stmt);
        if(rs4.next()) {
            String label = rs4.getString(1);
            int position = rs4.getInt(2);
            String x = rs4.getString(3);
            String y = rs4.getString(4);
            c.setPlate(label);
            c.setPosition(position);
            c.setWell(x, y);
        }
        DatabaseTransaction.closeResultSet(rs4);
    }
     
    private GrowthCondition getRecommendedGrowth(PreparedStatement stmt, int cloneid) throws Exception {
        GrowthCondition g = null;
        ResultSet rs = DatabaseTransaction.executeQuery(stmt);
        while(rs.next()) {
            int growthid = rs.getInt(1);
            String name = rs.getString(2);
            String hosttype = rs.getString(3);
            String selection = rs.getString(4);
            String growthcondition = rs.getString(5);
            String comments = rs.getString(6);
            g = new GrowthCondition(growthid, name, hosttype, selection, growthcondition,comments);
        }
        DatabaseTransaction.closeResultSet(rs);
        
        return g;
    }
     
    public List findRestrictedClones(List cloneidStrings, List restrictions) {
        String sql = "select clonename, restriction from clone where cloneid=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List restrictedClones = new ArrayList();
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<cloneidStrings.size(); i++) {
                String cloneid = (String)cloneidStrings.get(i);
                stmt.setInt(1, Integer.parseInt(cloneid));
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    String clonename = rs.getString(1);
                    String restriction = rs.getString(2);
                    
                    boolean match = false;
                    for(int k=0; k<restrictions.size(); k++) {
                        String res = (String)restrictions.get(k);
                        if(restriction.equals(res)) {
                            match = true;
                            break;
                        }
                    }
                    
                    if(!match) {
                        restrictedClones.add(clonename);
                    }
                } else {
                    handleError(null, "Cannot find clone for cloneid: "+cloneid);
                    return null;
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return restrictedClones;
    }
    
    public List findShippingRestrictedClones(List cloneidStrings) {
        String sql = "select clonename, domain from clone where cloneid=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List restrictedClones = new ArrayList();
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<cloneidStrings.size(); i++) {
                String cloneid = (String)cloneidStrings.get(i);
                stmt.setInt(1, Integer.parseInt(cloneid));
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    String clonename = rs.getString(1);
                    String species = rs.getString(2);
                    if(species.equals(DnaInsert.YP) || species.equals(DnaInsert.VC) || species.equals(DnaInsert.FT)) {
                        restrictedClones.add(clonename);
                    }
                } else {
                    handleError(null, "Cannot find clone for cloneid: "+cloneid);
                    return null;
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return restrictedClones;
    }
    
    public List queryCloneidsByCloneType(List clonetypes) {
        String sql = "select cloneid from clone";
        
        if(clonetypes != null && clonetypes.size()>0) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sql = sql+" where clonetype in ("+s+")";
        }
        
        PreparedStatement stmt = null;
        
        List cloneids = new ArrayList();
        try {
            stmt = conn.prepareStatement(sql);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                cloneids.add((new Integer(cloneid)).toString());
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones.");
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        return cloneids;
    }
    
    public static int queryCloneCounts(Map foundList, List nofound, String sql) {
        if(foundList == null || foundList.size() == 0)
            return 0;
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String currentCloneid = "";
        int totalCount = 0;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            Set terms = foundList.keySet();
            Iterator iter = terms.iterator();
            while(iter.hasNext()) {
                String term = (String)iter.next();
                List cloneids = (List)foundList.get(term);
                int count = 0;
                for(int i=0; i<cloneids.size(); i++) {
                    String cloneid = (String)cloneids.get(i);
                    currentCloneid = cloneid;
                    stmt.setInt(1, Integer.parseInt(cloneid));
                    rs = DatabaseTransaction.executeQuery(stmt);
                    if(rs.next()) {
                        int n = rs.getInt(1);
                        count += n;
                    }
                }
                if(count>0) {
                    totalCount += count;
                } else {
                    nofound.add(term);
                }
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Error occured while query clone counts by cloneid: "+currentCloneid);
                System.out.println(ex);
            }
            return 0;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        return totalCount;
    }
    
    public Map queryCloneSamples(List clones) {
        if(clones == null)
            return null;
        
        String sql = "select sampletype, position, containerlabel, result, sampleid,"+
        " cloneid, containerid, positionx, positiony"+
        " from sample where cloneid in (select cloneid from clone where clonename=?)";
    
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        Map m = new HashMap();
        String currentClone = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            
            for(int i=0; i<clones.size(); i++) {
                String clone = (String)clones.get(i);
                currentClone = clone;
                stmt.setString(1, clone);
                rs = DatabaseTransaction.executeQuery(stmt);
                List samples = new ArrayList();
                while(rs.next()) {
                    String type = rs.getString(1);
                    int position = rs.getInt(2);
                    String label = rs.getString(3);
                    String result = rs.getString(4);
                    int sampleid = rs.getInt(5);
                    int cloneid = rs.getInt(6);
                    int containerid = rs.getInt(7);
                    String x = rs.getString(8);
                    String y = rs.getString(9);
                    Sample s = new Sample(sampleid,type,null,cloneid,position,x,y,containerid, label);
                    s.setResult(result);
                    samples.add(s);
                }
                m.put(clone, samples);
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Error occured while query sample by clone "+currentClone);
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return m;
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