/*
 * CloneVector.java
 *
 * Created on June 17, 2003, 1:36 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class CloneVector {
    protected String name;
    protected String source;
    protected String type;
    protected String file;
    protected String path;
    protected List features;
    protected String description;
    protected String restriction;
    protected String hipname;
    protected int vectorid;
    
    /** Creates a new instance of CloneVector */
    public CloneVector() {
    }
    
    public CloneVector(String name) {
        this.name = name;
    }
    
    public CloneVector(CloneVector c) {
        if(c != null) {
            this.name = c.getName();
            this.source = c.getSource();
            this.type = c.getType();
            this.file = c.getFile();
            this.path = c.getPath();
            this.description = c.getDescription();
            this.restriction = c.getRestriction();
            this.hipname = c.getHipname();
            this.vectorid = c.getVectorid();
            
            features = new ArrayList();
            List f = c.getFeatures();
            if(f != null) {
                for(int i=0; i<f.size(); i++) {
                    VectorFeature vf = (VectorFeature)f.get(i);
                    features.add(new VectorFeature(vf));
                }
            }
        }
    }
    
    public CloneVector(String name, String source, String type, String file, String path, List features) {
        this.name = name;
        this.source = source;
        this.type = type;
        this.file = file;
        this.path = path;
        this.features = features;
    }
    
    public static List findDestVectors(String sourceVector) throws FlexDatabaseException {
        String sql = "select vectorname,vectorsource,vectortype,vectorfile,"+
        " vectorfilepath, description, restriction, hipname,vectorid"+
        " from vector where vectorname in ("+
        " select destvectorname from vectormapping"+
        " where srcvectorname=?)";
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List vectorList = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sourceVector);
            rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                String vectorname = rs.getString(1);
                String vectorsource = rs.getString(2);
                String vectortype = rs.getString(3);
                String vectorfile = rs.getString(4);
                String vectorfilepath = rs.getString(5);
                String description = rs.getString(6);
                String restriction = rs.getString(7);
                String hipname = rs.getString(8);
                int vectorid = rs.getInt(9);
                CloneVector vector = new CloneVector(vectorname, vectorsource,vectortype,vectorfile,vectorfilepath,null);
                vector.setDescription(description);
                vector.setRestriction(restriction);
                vector.setHipname(hipname);
                vector.setVectorid(vectorid);
                vectorList.add(vector);
            }
            return vectorList;
        } catch (Exception ex) {
            throw new FlexDatabaseException(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public void restore(String vname) throws Exception {
        name = vname;
        String sql = "select * from vector where vectorname='"+vname+"'";
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                source = rs.getString(2);
                type = rs.getString(3);
                file = rs.getString(4);
                path = rs.getString(5);
                description = rs.getString(6);
                restriction = rs.getString(7);
                hipname = rs.getString(8);
                vectorid = rs.getInt(9);
                
                features = new ArrayList();
                sql = "select * from vectorfeature where vectorname='"+name+"'";
                rs = t.executeQuery(sql);
                while(rs.next()) {
                    int id = rs.getInt(1);
                    String vectorname = rs.getString(2);
                    String desc = rs.getString(3);
                    String status = rs.getString(4);
                    VectorFeature f = new VectorFeature(id, vectorname, desc, status);
                    features.add(f);
                }
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public String getName() {return name;}
    public String getSource() {return source;}
    public String getType() {return type;}
    public String getFile() {return file;}
    public String getPath() {return path;}
    public List getFeatures() {return features;}
    public String getPathFile() {return path+file;}
    public String getDescription() {return description;}
    public String getHipname() {return hipname;}
    public String getRestriction() {return restriction;}
    public int getVectorid() {return vectorid;}
    public String getNameAndId() {return name+"!"+vectorid;}
    
    public void setDescription(String s) {this.description = s;}
    public void setRestriction(String s) {this.restriction = s;}
    public void setHipname(String s) {this.hipname = s;}
    public void setVectorid(int id) {this.vectorid = id;}
    
    public static void main(String args[]) {
        String vectorname = "pDNR-Dual";
        List vectors = null;
        
        try {
            vectors = CloneVector.findDestVectors(vectorname);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        if(vectors == null || vectors.size() == 0) {
            System.out.println("No vectors found.");
        }
        
        for(int i=0; i<vectors.size(); i++) {
            CloneVector vector = (CloneVector)vectors.get(i);
            System.out.println(vector.getName());
        }
    }
}
