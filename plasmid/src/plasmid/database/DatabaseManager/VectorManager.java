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

/**
 *
 * @author  DZuo
 */
public class VectorManager extends TableManager {
    private Connection conn;
    
    /** Creates a new instance of VectorManager */
    public VectorManager(Connection conn) {
        this.conn = conn;
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
        " values(?,?,?,?,?,?,?,?,?,?,?)";
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
                
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPROPERTY table");
            return false;
        }
        return true;
    }
    
    public boolean insertVectorFeatures(List features) {
        if(features == null)
            return true;
        
        String sql = "insert into featuremap"+
                    " (vectorid, maptype)"+
                    " values(?,?)";
        String sql2 = "insert into vectorfeature"+
                    " (name, description, startpos, endpos, vectorid, maptype)"+
                    " values(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            
            for(int i=0; i<features.size(); i++) {
                VectorFeature v = (VectorFeature)features.get(i);
                stmt.setInt(1, v.getVectorid());
                stmt.setString(2, v.getMaptype());
                
                stmt2.setString(1, v.getName());
                stmt2.setString(2, v.getDescription());
                stmt2.setInt(3, v.getStart());
                stmt2.setInt(4,  v.getStop());
                stmt2.setInt(5, v.getVectorid());
                stmt2.setString(6, v.getMaptype());
                
                DatabaseTransaction.executeUpdate(stmt);
                DatabaseTransaction.executeUpdate(stmt2);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORMAP and VECTORFEATURE tables");
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
                stmt.setInt(3, vp.getParentvectorid());
                stmt.setString(4, vp.getComments());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
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
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPUBLICATIOIN table");
            return false;
        }
        return true;
    }       
}