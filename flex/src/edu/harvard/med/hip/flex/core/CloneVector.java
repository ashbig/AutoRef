/*
 * CloneVector.java
 *
 * Created on June 17, 2003, 1:36 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
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
    protected List m_tags = null;
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
    
     public CloneVector(String name, String source, String type, 
     String file, String path, List features, String description,
        String restriction, String hipname, int id) 
     {
         this( name, source,  type,  file,  path,  features) ;
        this.description = description;
        this.restriction = restriction;
        this.hipname = hipname;
        this.vectorid = id;
     
     }
   
     
   public static List  getAllVectors() throws FlexDatabaseException
   {
       String sql = "select vectorname,vectorsource,vectortype,vectorfile,vectorfilepath,"
      +"description,restriction,hipname,vectorid from vector order by vectorid";
       return getVectorsBySQL(sql);
   }
   
   private static List getVectorsBySQL(String sql) throws FlexDatabaseException 
   {
     
        ResultSet rs = null;
        List vectorList = new ArrayList();
        try 
        { 
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) 
            {
                CloneVector vector = new CloneVector
                        (rs.getString("vectorname"),
                        rs.getString("vectorsource"),
                        rs.getString("vectortype"),
                        rs.getString("vectorfile"),
                        rs.getString("vectorfilepath"),
                        null,
                        rs.getString("description"),
                        rs.getString("restriction"), 
                        rs.getString("hipname"), rs.getInt("vectorid") );
                vectorList.add(vector);
            }
            return vectorList;
        } catch (Exception ex)
        {
            throw new FlexDatabaseException(ex);
        } 
        finally 
        {
            DatabaseTransaction.closeResultSet(rs);
        }
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
    public List     getTags(){ return m_tags;}
    
    
    public void setDescription(String s) {this.description = s;}
    public void setRestriction(String s) {this.restriction = s;}
    public void setHipname(String s) {this.hipname = s;}
    public void setVectorid(int id) {this.vectorid = id;}
    public void setFeatures(List v) {this.features = v;}
    public void         setName(String v) {name = v;}
    public void         setSource(String v) { source = v;}
    public void         setType(String v) { type =v;}
    public void         setFile(String v) { file =v;}
    public void         setPath(String v) { path =v;}
     
    public String toString()
    {
       return "Vector name: "+  name+" vector source: "+  source+" vector type: "+ type
               +" hip name: "+ hipname;
    }
    public static void            insertVectors(ArrayList vectors,Connection conn) throws Exception
    {
        String sql_vector = "insert into vector (vectorid, VECTORNAME, VECTORSOURCE  ,VECTORTYPE , "
        + "VECTORFILE  ,VECTORFILEPATH ,DESCRIPTION  ,RESTRICTION  ,HIPNAME  )"+
        " values( ?,?,?,?,?,?,?,?,?)";
        
         String sql_vector_feature = "insert into VECTORFEATURE  (FEATUREID ,FEATURENAME "
 + " ,FEATUREDESCRIPTION ,FEATURESTATUS,VECTORNAME) values(vectorfeatureid.nextval,?,?,?,?)";
                  String sql_vector_tag = "insert into VECTORTAG  (TAGNAME ,TAGTYPE "
 + " ,VECTORNAME) values( ?,?,?)";

         PreparedStatement stmt_vector= null;
         PreparedStatement stmt_vector_feature = null;
         PreparedStatement stmt_vector_tag = null;
        
         VectorFeature v_feature = null;
         CloneVector vector =   null;
         VectorTag v_tag = null;
         int vector_id = -1; int count=1;
        try {
            stmt_vector = conn.prepareStatement(sql_vector);
            stmt_vector_feature = conn.prepareStatement(sql_vector_feature);
            stmt_vector_tag = conn.prepareStatement(sql_vector_tag);
            for(int v_count = 0; v_count < vectors.size(); v_count ++)
            {
                count=1;
                vector = (CloneVector) vectors.get( v_count);
  //System.out.println("submitting "+vector.getName());
                vector_id = FlexIDGenerator.getID("vectorid");
                if (vector.getHipname() == null) 
                {
                    if (vector_id>99) vector.setHipname("HIPaATT"+vector_id);
                    else   vector.setHipname("HIPaATT0"+vector_id);
                }
    ////?????            stmt_vector.setInt(1, vector.getVectorid());
                stmt_vector.setInt(count++,    vector_id);
                stmt_vector.setString(count++, vector.getName());
                stmt_vector.setString(count++, vector.getSource());
                stmt_vector.setString(count++, vector.getType());
                 stmt_vector.setString(count++, vector.getFile());
                stmt_vector.setString(count++, vector.getPath());
                stmt_vector.setString(count++, vector.getDescription());
                stmt_vector.setString(count++, vector.getRestriction());
                stmt_vector.setString(count++, vector.getHipname());
                DatabaseTransaction.executeUpdate(stmt_vector);
                if ( vector.getFeatures() != null)
                {
                    for ( int f_count = 0; f_count < vector.getFeatures().size(); f_count ++)
                    {
                         v_feature = (VectorFeature)vector.getFeatures().get(f_count);
                ////?????             stmt_vector_feature.setInt(1, v_feature.getId());
                         stmt_vector_feature.setString(1, v_feature.getName());
                          stmt_vector_feature.setString(2, v_feature.getDescription());
                          stmt_vector_feature.setString(3, v_feature.getStatus()); 
                          stmt_vector_feature.setString(4, vector.getName()); 
                           DatabaseTransaction.executeUpdate(stmt_vector_feature);
                     }
                }
                if (vector.getTags() != null)
                {
                    for ( int t_count = 0; t_count < vector.getTags().size(); t_count ++)
                    {
                         v_tag = (VectorTag)vector.getTags().get(t_count);
                        stmt_vector.setString(1, v_tag.getName());
                        stmt_vector.setString(2, v_tag.getType());
                        stmt_vector.setString(3, vector.getName());

                           DatabaseTransaction.executeUpdate(stmt_vector_feature);
                     }
                }
             //  System.out.println("finished "+vector.getName());
              }
             System.out.println("finished ");
           
        } catch (Exception ex) {
            conn.rollback();
            System.out.println(ex.getMessage());
            throw new Exception("Error occured while inserting VECTOR info. \n "+ex.getMessage());
            
        }
         finally
         {
              DatabaseTransaction.closeStatement(stmt_vector);
               DatabaseTransaction.closeStatement(stmt_vector_feature );
              DatabaseTransaction.closeStatement(stmt_vector_tag );
      
         }
      
    }
      
   
      //***************************
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
