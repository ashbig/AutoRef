/*
 * VectorManager.java
 *
 * Created on April 1, 2005, 4:02 PM
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
public class VectorManager extends TableManager {
    
    /** Creates a new instance of VectorManager */
    public VectorManager(Connection conn) {
       super(conn);
    }
    
    public boolean insertVector(CloneVector vector) {
        String sql = "insert into vector"+
        " (vectorid, name, description, form, type, sizeinbp,"+
        " mapfilename, sequencefilename, comments)"+
        " values(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vector.getVectorid());
            stmt.setString(2, vector.getName());
            stmt.setString(3, vector.getDescription());
            stmt.setString(4, vector.getForm());
            stmt.setString(5, vector.getType());
            stmt.setInt(6,  vector.getSize());
            stmt.setString(7, vector.getMapfilename());
            stmt.setString(8, vector.getSeqfilename());
            stmt.setString(9, vector.getComments());
            
            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTOR table");
            return false;
        }
        return true;
    }
    
    public boolean insertVectors(List vectors) {
        if(vectors == null)
            return true;
        
        String sql = "insert into vector"+
        " (vectorid, name, description, form, type, sizeinbp,"+
        " mapfilename, sequencefilename, comments)"+
        " values(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<vectors.size(); i++) {
                CloneVector vector = (CloneVector)vectors.get(i);
                stmt.setInt(1, vector.getVectorid());
                stmt.setString(2, vector.getName());
                stmt.setString(3, vector.getDescription());
                stmt.setString(4, vector.getForm());
                stmt.setString(5, vector.getType());
                stmt.setInt(6,  vector.getSize());
                stmt.setString(7, vector.getMapfilename());
                stmt.setString(8, vector.getSeqfilename());
                stmt.setString(9, vector.getComments());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTOR table");
            return false;
        }
        return true;
    }
    
    public boolean insertSynonyms(List synonyms) {
        if(synonyms == null)
            return true;
        
        String sql = "insert into vectorsynonym"+
        " (vectorid, vsynonym)"+
        " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<synonyms.size(); i++) {
                VectorSynonym vs = (VectorSynonym)synonyms.get(i);
                stmt.setInt(1, vs.getVectorid());
                stmt.setString(2, vs.getSynonym());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORSYNONYM table");
            return false;
        }
        return true;
    }
    
    public boolean insertProperties(List properties) {
        if(properties == null)
            return true;
        
        String sql = "insert into vectorproperty"+
        " (vectorid, propertytype)"+
        " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<properties.size(); i++) {
                VectorProperty vp = (VectorProperty)properties.get(i);
                stmt.setInt(1, vp.getVectorid());
                stmt.setString(2, vp.getPropertyType());
                
                if(Constants.DEBUG) {
                    System.out.println("inserting propertytype: "+vp.getPropertyType());
                }
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPROPERTY table");
            return false;
        }
        return true;
    }
    
    public boolean insertVectorFeatures(List features) {
        if(features == null)
            return true;
        
        String sql2 = "insert into vectorfeature"+
        " (name, description, startpos, endpos, vectorid, maptype, featureid)"+
        " values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            
            for(int i=0; i<features.size(); i++) {
                VectorFeature v = (VectorFeature)features.get(i);
                stmt2.setString(1, v.getName());
                stmt2.setString(2, v.getDescription());
                stmt2.setInt(3, v.getStart());
                stmt2.setInt(4,  v.getStop());
                stmt2.setInt(5, v.getVectorid());
                stmt2.setString(6, v.getMaptype());
                stmt2.setInt(7, v.getFeatureid());
                
                DatabaseTransaction.executeUpdate(stmt2);
            }
            DatabaseTransaction.closeStatement(stmt2);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORFEATURE table");
            return false;
        }
        return true;
    }
    
    public boolean insertParents(List parents) {
        if(parents == null)
            return true;
        
        String sql = "insert into vectorparent"+
        " (vectorid, parentvectorname, parentvectorid, comments)"+
        " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<parents.size(); i++) {
                VectorParent vp = (VectorParent)parents.get(i);
                stmt.setInt(1, vp.getVectorid());
                stmt.setString(2, vp.getParentvectorname());
                
                int parentvectorid = vp.getParentvectorid();
                if(parentvectorid != 0)
                    stmt.setInt(3, vp.getParentvectorid());
                else
                    stmt.setString(3, null);
                stmt.setString(4, vp.getComments());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPARENT table");
            return false;
        }
        return true;
    }
    
    public boolean insertVectorAuthors(List authors) {
        if(authors == null)
            return true;
        
        String sql = "insert into vectorauthor"+
        " (vectorid, authorid, authortype, creationdate)"+
        " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<authors.size(); i++) {
                VectorAuthor vp = (VectorAuthor)authors.get(i);
                stmt.setInt(1, vp.getVectorid());
                stmt.setInt(2, vp.getAuthorid());
                stmt.setString(3, vp.getType());
                stmt.setString(4, vp.getDate());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORAUTHOR table");
            return false;
        }
        return true;
    }
    
    public boolean insertVectorPublications(List publications) {
        if(publications == null)
            return true;
        
        String sql = "insert into vectorpublication"+
        " (vectorid, publicationid)"+
        " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<publications.size(); i++) {
                VectorPublication vp = (VectorPublication)publications.get(i);
                stmt.setInt(1, vp.getVectorid());
                stmt.setInt(2, vp.getPublicationid());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPUBLICATIOIN table");
            return false;
        }
        return true;
    }
    
    public CloneVector queryCloneVector(int vectorid) {
        CloneVector v = null;
        
        String sql="select name,description,form,type,sizeinbp,mapfilename,sequencefilename,comments"+
        " from vector where vectorid="+vectorid;
        String sql2="select featureid,maptype,name,description,startpos,endpos"+
        " from vectorfeature where vectorid="+vectorid+" order by maptype";
        String sql3 = "select propertytype from vectorproperty where vectorid="+vectorid;
        String sql4 = "select vsynonym from vectorsynonym where vectorid="+vectorid;
        String sql5 = "select v.authorid,v.authortype,v.creationdate,a.authorname"+
        " from vectorauthor v, authorinfo a"+
        " where v.authorid=a.authorid and v.vectorid="+vectorid;
        String sql6 = "select p.publicationid,p.title,p.pmid"+
        " from publication p, vectorpublication v"+
        " where p.publicationid=v.publicationid and v.vectorid="+vectorid;
        String sql7 = "select parentvectorname,parentvectorid,comments"+
        " from vectorparent where vectorid="+vectorid;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            
            ResultSet rs = t.executeQuery(sql);
            if(rs.next()) {
                String name=rs.getString(1);
                String description = rs.getString(2);
                String form = rs.getString(3);
                String type = rs.getString(4);
                int size = rs.getInt(5);
                String mapfilename = rs.getString(6);
                String seqfilename = rs.getString(7);
                String comments = rs.getString(8);
                v = new CloneVector(vectorid, name, description, form, type, size, mapfilename, seqfilename, comments);
                
                ResultSet rs2 = t.executeQuery(sql2);
                List features = new ArrayList();
                while(rs2.next()) {
                    int featureid = rs2.getInt(1);
                    String maptype = rs2.getString(2);
                    String n = rs2.getString(3);
                    String d = rs2.getString(4);
                    int start = rs2.getInt(5);
                    int end = rs2.getInt(6);
                    VectorFeature feature = new VectorFeature(featureid,n,d,start,end,vectorid,maptype);
                    features.add(feature);
                }
                v.setVectorfeatures(features);
                DatabaseTransaction.closeResultSet(rs2);
                
                ResultSet rs3 = t.executeQuery(sql3);
                List properties = new ArrayList();
                while(rs3.next()) {
                    String p = rs3.getString(1);
                    properties.add(p);
                }
                v.setProperty(properties);
                DatabaseTransaction.closeResultSet(rs3);
                
                ResultSet rs4 = t.executeQuery(sql4);
                List names = new ArrayList();
                while(rs4.next()) {
                    String synonym = rs4.getString(1);
                    names.add(synonym);
                }
                v.setSynonyms(names);
                DatabaseTransaction.closeResultSet(rs4);
                
                ResultSet rs5 = t.executeQuery(sql5);
                List authors = new ArrayList();
                while(rs5.next()) {
                    int authorid = rs5.getInt(1);
                    String tp = rs5.getString(2);
                    String date = rs5.getString(3);
                    String n = rs5.getString(4);
                    VectorAuthor author = new VectorAuthor(vectorid,authorid,tp,date,n);
                    authors.add(author);
                }
                v.setAuthors(authors);
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
                v.setPublications(publications);
                DatabaseTransaction.closeResultSet(rs6);
                
                ResultSet rs7 = t.executeQuery(sql7);
                List parents = new ArrayList();
                while(rs7.next()) {
                    String parentname = rs7.getString(1);
                    int parentid = rs7.getInt(2);
                    String c = rs7.getString(3);
                    VectorParent p = new VectorParent(vectorid,parentname,c,parentid);
                    parents.add(p);
                }
                v.setVectorparents(parents);
                DatabaseTransaction.closeResultSet(rs7);
            } else {
                handleError(null, "Cannot find vector record with vectorid: "+vectorid);
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            handleError(ex,  "Cannot find vector record with vectorid: "+vectorid);
        }
        
        return v;
    }
    
    public static void main(String args[]) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager manager = new VectorManager(conn);
            CloneVector v = manager.queryCloneVector(3);
            System.out.println(v.getName());
            System.out.println(v.getDescription());
            System.out.println(v.getComments());
            System.out.println(v.getForm());
            System.out.println(v.getType());
            System.out.println(v.getSize());
            System.out.println(v.getMapfilename());
            System.out.println(v.getSeqfilename());
            
            System.out.println(v.getPropertyString());
            
            System.out.println(v.getSynonymString());
            
            List features = v.getVectorfeatures();
            for(int i=0; i<features.size(); i++) {
                VectorFeature f = (VectorFeature)features.get(i);
                System.out.println(f.getDescription());
                System.out.println(f.getFeatureid());
                System.out.println(f.getMaptype());
                System.out.println(f.getName());
                System.out.println(f.getStart());
                System.out.println(f.getStop());
            }
            
            List authors = v.getAuthors();
            for(int i=0; i<authors.size(); i++) {
                VectorAuthor a = (VectorAuthor)authors.get(i);
                System.out.println(a.getAuthorid());
                System.out.println(a.getDate());
                System.out.println(a.getName());
                System.out.println(a.getType());
            }
            
            List publications = v.getPublications();
            for(int i=0; i<publications.size(); i++) {
                Publication p = (Publication)publications.get(i);
                System.out.println(p.getPmid());
                System.out.println(p.getPublicationid());
                System.out.println(p.getTitle());
            }
            
            List parents = v.getVectorparents();
            for(int i=0; i<parents.size(); i++) {
                VectorParent p = (VectorParent)parents.get(i);
                System.out.println(p.getParentvectorid());
                System.out.println(p.getParentvectorname());
                System.out.println(p.getComments());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
    
    public int getVectorid(String name) {
        String sql = "select vectorid from vector where name=?";
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