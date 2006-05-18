//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * BioVector.java
 *
 * Created on February 24, 2003, 4:55 PM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.*;
/**
 *
 * @author  htaycher
 */
public class BioVector
{
    public final static int VECTOR_TYPE_MASTER = 1;
    public final static int VECTOR_TYPE_EXSPRESION = 2;
   // public final static int VECTOR_TYPE_FUTURE_HOLDER = 3;
    
    
    
    public final static String VECTOR_TYPE_MASTER_STR = "entry vector";
    public final static String VECTOR_TYPE_EXSPRESION_STR = "destination vector";
    
    private int m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int m_type = -1;
    private String m_name = null;
    private String m_sequence = null;
    private String m_source = null;
    private String m_filename = null;
    private String m_filepath = null;
    
    private ArrayList m_features = null;
    private ArrayList m_primers = null;
    /** Creates a new instance of BioVector */
    
    
    public BioVector(){}
    
    public BioVector(int id, String name, String seq, String source, 
            String filename ,String filepath ,
            int type,ArrayList features, ArrayList primers
            )throws BecDatabaseException
    {
        m_name = name;
        m_sequence = seq;
        m_features = features;
        m_source=source;
        m_type = type;
        m_primers =primers;
        m_filename = filename;
        m_filepath = filepath;
         if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("vectorid");
        else
            m_id = id;
    }
    
   
    public int getId()    { return m_id;}
    public String getName()    { return m_name;}
    public String getSequence()    { return m_sequence;}
    public String getSource(){ return m_source;}
    public String getFileName(){ return m_filename;}
    public String getFilePath(){ return m_filepath ;}
    
    public ArrayList getFeatures()    { return m_features;}
    public int     getType(){ return m_type;}   
    public String  getTypeAsString()    {   return getTypeAsString(m_type);    }
    public static String  getTypeAsString(int type)
    {
        switch (type)
        {
            case VECTOR_TYPE_MASTER: return VECTOR_TYPE_MASTER_STR;
            case VECTOR_TYPE_EXSPRESION: return VECTOR_TYPE_EXSPRESION_STR;
            default: return "";
        }
    }
    
    public void addFeature(BioVectorFeature v)    { if ( m_features == null) m_features = new ArrayList();  m_features.add(v);}
    public ArrayList getPrimers()    { return m_primers;}
    public void addPrimers(Oligo v)    {  m_primers.add(v);}
    
    public void setId( int v)    {  m_id = v;}
    public void setType(int v){  m_type = v;}   
    public void setName( String v)    {  m_name = v;}
    public void setSequence( String v)    {  m_sequence = v;}
    public void setSource(String s){ m_source = s;}
    public void setFileName(String s){ m_filename = s;}
    public void setFilePath(String s){ m_filepath = s;}
  
    public void setFeatures( ArrayList v)    {  m_features = v;}
    public void setPrimers( ArrayList v)    {  m_primers = v;}

    
    
    public void insert(Connection conn)throws BecDatabaseException
    {
        if ( m_name == null || m_name.length() < 1) throw new BecDatabaseException("Vector does not have name");
        
        if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("vectorid");
        BioVectorFeature ft = null;
        String sql = "insert into vector(vectorid, vectorname, source, vectortype, vectorfilename,vectorfilepath)"+
        " values ("+m_id +",'"+ m_name +"','"+m_source + "',"+m_type +",'"+m_filename +"','"+m_filepath+"')";
        
      
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            
            for (int count = 0; count < m_features.size();count++)
            {
                ft = (BioVectorFeature)m_features.get(count);
                ft.setVectorId(m_id);
                ft.insert(conn);
            }
            
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
       
       
       
    public static ArrayList getAllVectors()throws BecDatabaseException
    {
        ArrayList vect = new ArrayList();
         String sql = "select vectorid, vectorname, source,vectortype, vectorfilename, vectorfilepath  from vector ";
        RowSet rs = null;
        BioVector vector = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
                vector= new BioVector( rs.getInt("vectorid"),rs.getString("vectorname"), 
                        null, rs.getString("source") ,rs.getString("vectorfilename"),rs.getString("vectorfilepath"),
                        rs.getInt("vectortype"), null, null);
                 vector.setFeatures( getFeatures(vector.getId()));
                vect.add(vector);
            }
            return vect;
            
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
       
    }
    
    public static BioVector getVectorById(int id)throws BecDatabaseException
    {
              
        String sql = "select vectorid, vectorname, source, vectortype, "
        +" vectorfilename, vectorfilepath from vector where vectorid = "+id;
        RowSet rs = null;
        BioVector vect = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                vect= new BioVector( rs.getInt("vectorid"),rs.getString("vectorname"), 
                        null, rs.getString("source") ,rs.getString("vectorfilename"),rs.getString("vectorfilepath"),
                        rs.getInt("vectortype"), null, null);
               vect.setFeatures( getFeatures(vect.getId()));
            }
            //get features
           
            return vect;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing feature with id: "+id+"\n"+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    
    
    }
    
    /*
    public void insert(Connection conn)throws BecDatabaseException
    {
        String sql = "insert into vector(vectorid, vectorname, sequence)"+
        " values ("+m_id +","+ m_name +","+m_sequence + ")";
        
      
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            //insert features
            for (int ind = 0 ; ind < m_features.size() ;ind++)
            {
                BioVectorFeature pr = (BioVectorFeature)m_features.get(ind);
                pr.insert(conn);
            }
           
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    */
    
    public static ArrayList getVectorPrimers(int vectorid)throws BecDatabaseException
    {
        String sql = "select  cp.primerid as id,cp.tm as tm, cp.sequence as sequence, cp.type as type, cp.name as name, vp.position as position, vp.orientation as orientation,"+
        " leaderlength, leadersequence from commonprimer cp, vectorprimer vp where vp.primerid=cp.primerid and vp.vectorid = "+vectorid;
        RowSet rs = null;
        ArrayList pr = new ArrayList();
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
               Oligo ol = new Oligo();
               ol.setTm(rs.getDouble("tm") )   ;
                ol.setId(rs.getInt("id") )   ;
                ol.setType(rs.getInt("type") );//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …

                ol.setPosition(rs.getInt("position"));// for full sequencing, start of the prime regarding sequence start
                ol.setName(rs.getString("name") ) ;
                ol.setSequence(rs.getString("sequence"));
                ol.setOrientation(rs.getInt("orientation")) ;
                ol.setLeaderLength(rs.getInt("leaderlength"));
                ol.setLeaderSequence(rs.getString("leadersequence"));
               pr.add(ol); 
            }
            return pr;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing feature with id: "+vectorid+"\n"+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    private static ArrayList getFeatures(  int vectorid)throws BecDatabaseException
    {
        String sql = "select featureid, vectorid, featuretype, featurename,description from vectorfeature where vectorid = "+vectorid;
        RowSet rs = null;
        ArrayList features = new ArrayList();
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
               
                features.add(new BioVectorFeature ( rs.getInt("featureid"),
               rs.getString("featurename"),rs.getInt("featuretype"), rs.getInt("vectorid"),rs.getString("description")));
            }
            return features;
        } catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while initializing feature with id: "+vectorid+"\n"+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    
      public static void main(String [] args) {
          ArrayList r = null;
          try{
              r = BioVector.getVectorPrimers(5);
          }
          catch(Exception e){}
          System.exit(0);
              
      }
}
