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
    
    /** Creates a new instance of CloneVector */
    public CloneVector() {
    }
    
    public CloneVector(String name) {
        this.name = name;
    }
    
    public CloneVector(String name, String source, String type, String file, String path, List features) {
        this.name = name;
        this.source = source;
        this.type = type;
        this.file = file;
        this.path = path;
        this.features = features;
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
}
