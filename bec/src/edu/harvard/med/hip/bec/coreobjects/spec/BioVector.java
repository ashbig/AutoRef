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
    
    private int m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String m_name = null;
    private String m_sequence = null;
    private ArrayList m_features = null;
    private ArrayList m_primers = null;
    /** Creates a new instance of BioVector */
    public BioVector(int id, String name, String seq, ArrayList features, ArrayList primers)throws BecDatabaseException
    {
        m_name = name;
        m_sequence = seq;
        m_features = features;
        m_primers =primers;
         if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("containerid");
        else
            m_id = id;
    }
    
   
    public int getId()    { return m_id;}
    public String getName()    { return m_name;}
    public String getSequence()    { return m_sequence;}
    public ArrayList getFeatures()    { return m_features;}
    public void addFeature(BioVectorFeature v)    {  m_features.add(v);}
    public ArrayList getPrimers()    { return m_primers;}
    public void addPrimers(Oligo v)    {  m_primers.add(v);}
    
    public void setId( int v)    {  m_id = v;}
    public void setName( String v)    {  m_name = v;}
    public void setSequence( String v)    {  m_sequence = v;}
    public void setFeatures( ArrayList v)    {  m_features = v;}
public void setPrimers( ArrayList v)    {  m_primers = v;}

    public static ArrayList getAllVectors()throws BecDatabaseException
    {
        ArrayList vect = new ArrayList();
         String sql = "select vectorid, vectorname, sequence from vector ";
        RowSet rs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                vect.add( new BioVector( rs.getInt("vectorid"),rs.getString("vectorname"), rs.getString("sequence") , null, null));
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
              
        String sql = "select vectorid, vectorname, sequence from vector where vectorid = "+id;
        RowSet rs = null;
        BioVector vect = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                vect = new BioVector( rs.getInt("vectorid"),rs.getString("vectorname"), rs.getString("sequence") , null, null);
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
    
    
    public static ArrayList getVectorPrimers(int vectorid)throws BecDatabaseException
    {
        String sql = "select  cp.primerid as id,cp.tm as tm, cp.sequence as sequence, cp.type as type, cp.name as name, vp.position as position, vp.orientation as orientation"+
        " from commonprimer cp, vectorprimer vp where vp.primerid=cp.primerid and vp.vectorid = "+vectorid;
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

                ol.setStart(rs.getInt("position"));// for full sequencing, start of the prime regarding sequence start
                ol.setName(rs.getString("name") ) ;
                ol.setSequence(rs.getString("sequence"));
                ol.setOrientation(rs.getInt("orientation")) ;
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
        String sql = "select featureid, vectorid, featuretype, featurename from vectorfeature where vectorid = "+vectorid;
        RowSet rs = null;
        ArrayList features = new ArrayList();
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
             //   features.add(new BioVectorFeature ( rs.getInt("featureid"),
              // rs.getString("featurename"),rs.getInt("featuretype"), rs.getInt("vectorid")));
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
}
