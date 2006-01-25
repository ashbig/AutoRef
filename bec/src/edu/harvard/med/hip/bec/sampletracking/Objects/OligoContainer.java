/**
 * $Id: OligoContainer.java,v 1.7 2006-01-25 16:41:45 Elena Exp $
 *
 * File     	: Container.java

 */
package edu.harvard.med.hip.bec.sampletracking.objects;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.bec.ui_objects.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.user.*;
import  edu.harvard.med.hip.bec.coreobjects.endreads.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;
import  edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.bec.*;

/**
 * Generic representation of Oligo of containers.
 */
public class OligoContainer
{
  
    //for oligo containers
 //   public static final int STATUS_ORDER_CREATED = 0;
    public static final int STATUS_ORDER_SENT = 1;
    public static final int STATUS_RECIEVED = 2;
    public static final int STATUS_SENT_FOR_SEQUENCING = 3;
    
    
    public static final int MODE_RESTORE_SAMPLES = 1;
    public static final int MODE_NOTRESTORE_SAMPLES = 0;
    
    
    private int         m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String      m_label = null;
    private ArrayList   m_samples = new ArrayList();
    private int         m_status = -1;
    private int         m_userid = -1;
    private String      m_comment_order =  null;
    private String      m_comment_sequencing =  null;
    private java.util.Date        m_create_date = null;
    
    public static OligoContainer getById(int id) throws BecUtilException, BecDatabaseException
    {
     
        String sql = "select  OLIGOCONTAINERID ,LABEL ,USERID ,ORDERDATE, STATUS ,"
+" DESCR_OLIGO_PROCESSING  ,DESCR_SEQUENCING from oligocontainer where OLIGOCONTAINERID = "+id;
        ArrayList containers = getByRule(sql, MODE_RESTORE_SAMPLES);
        if (containers != null && containers.size() > 0)
            return (OligoContainer)containers.get(0);
        else 
            return null;
    }
    
    
      public static void getAll( ) throws BecUtilException, BecDatabaseException
    {
     
        String sql = "select  OLIGOCONTAINERID ,LABEL ,USERID ,ORDERDATE, STATUS ,"
+" DESCR_OLIGO_PROCESSING  ,DESCR_SEQUENCING from oligocontainer ";
        CachedRowSet crs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
               System.out.println("update oligocontainer set DESCR_OLIGO_PROCESSING ='"+ 
               crs.getString("DESCR_OLIGO_PROCESSING")+"',  DESCR_SEQUENCING='"+ crs.getString("DESCR_SEQUENCING")
               +"' where oligocontainerid = "+crs.getInt("OLIGOCONTAINERID") 
             );
              
             }
        
        } catch (Exception ex)
        {
            throw new BecDatabaseException("Error occured while initializing container: \n"+ex.getMessage());
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
       
    }
    
    
    public OligoContainer(String label, int status, int userid, 
            String ordcomment, String seqcomment) 
    {
         m_label = label;
        m_status = status;
        m_userid = userid;
        m_comment_order =  ordcomment;
        m_comment_sequencing =  seqcomment;
    }
      
    public OligoContainer(String label, int id, int status, int userid, 
            String ordcomment, String seqcomment) 
    {
         m_label = label;
         m_id = id;
        m_status = status;
        m_userid = userid;
        m_comment_order =  ordcomment;
        m_comment_sequencing =  seqcomment;
    }
    
    public OligoContainer() throws BecDatabaseException
    {
        m_id = BecIDGenerator.getID("containerid");
    }
    public  int         getId (){ return m_id ;}
    public  String      getLabel (){ return m_label;}
    public  ArrayList   getSamples (){ return m_samples ;}
    public  int         getStatus (){ return m_status ;}
    public  int         getUserId (){ return m_userid ;}
    public  String      getCommentOrder (){ return m_comment_order ;}
    public  String      getCommentSequencing (){ return m_comment_sequencing;}
    public  java.util.Date        getCreateDate(){ return m_create_date;}
    public String       getStatusAsString(){ return OligoContainer.getStatusAsString(m_status);}
    
    public static String       getStatusAsString(int status)
    {
        switch(status)
        {
            //case STATUS_ORDER_CREATED: return "Created";
            case STATUS_ORDER_SENT : return "Ordered";
            case STATUS_RECIEVED : return "Recieved";
            case STATUS_SENT_FOR_SEQUENCING: return "Used for sequencing";
            default: return "Not known";
        }
        
    }
    public  void         setId (int v){  m_id = v;}
    public  void         setLabel (String v){  m_label= v;}
    public  void   		setSamples (ArrayList v){  m_samples = v;}
    public  void         setStatus (int v){  m_status = v;}
    public  void         setUserId (int v){  m_userid = v;}
    public  void      setCommentOrder (String v){  m_comment_order = v;}
    public  void      setCommentSequencing (String v){  m_comment_sequencing= v;}
    public void             addSample(OligoSample sample)    {        m_samples.add(sample);    }
    public void         setCreateDate(java.util.Date v){ m_create_date =v ;}
   
    public static ArrayList findContainersInfoFromLabel(String label, int mode) throws BecUtilException,
    BecDatabaseException
    {
        String sql = "select  OLIGOCONTAINERID ,LABEL ,USERID ,ORDERDATE, STATUS ,"
+" DESCR_OLIGO_PROCESSING  ,DESCR_SEQUENCING from oligocontainer where label = '"+label+"'";
        return getByRule(sql, mode);
       
    }
    
    
    
    public static ArrayList findAllContainerLabels() throws    BecDatabaseException
    {
        //define isolatetracking id statuses for the process
        String sql =  "select  label from oligocontainer order by label";
        ArrayList container_labels = new ArrayList();
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                container_labels.add(   rs.getString("label")  );
            }
            return container_labels;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured searching for oligo containers for the process\nSQL: "+sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
            
        }
       
    }
    
   
    
    /**
     * Get the data from Sample table.
     *
     * @exception BecDatabaseException.
     */
    public void restoreSamples() throws BecDatabaseException
    {
        
        m_samples.clear();
        
        String sql = "select oligosampleid, position, cloneid,oligoid from oligosample where oligocontainerid="+m_id+" order by POSITION";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        CachedRowSet crs = t.executeQuery(sql);
        
        try
        {
            while(crs.next())
            {
                OligoSample s = new OligoSample( crs.getInt("OLIGOSAMPLEID")  , 
                m_id, crs.getInt("POSITION"),crs.getInt("OLIGOID")  ,
                Oligo.getByOligoId(crs.getInt("OLIGOID"))  ,crs.getInt("CLONEID"));
                m_samples.add(s);
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing samples\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
           
            DatabaseTransaction.closeResultSet(crs);
        }
         
    }
    
    
    /**
     * Update the location record to the database.
     *
     * @param locationid The new locationid to be updated to.
     * @param c The connection used for database update.
     * @exception The BecDatabaseException.
     */
    public void updateStatus(int status, Connection c) throws BecDatabaseException
    {
        String sql = "update oligocontainer set status="+ status +      " where oligocontainerid="+m_id;
        m_status = status;
        DatabaseTransaction.executeUpdate(sql, c);
    }
    public static void  updateStatus(int status, int containerid,Connection c) throws BecDatabaseException
    {
        String sql = "update oligocontainer set status="+ status +    " where oligocontainerid="+containerid;
        DatabaseTransaction.executeUpdate(sql, c);
    }
     public void updateOrdercomment(String comment, Connection c) throws BecDatabaseException
    {
        String sql = "update oligocontainer set DESCR_OLIGO_PROCESSING='"+ comment +      "' where oligocontainerid="+m_id;
        m_comment_order = comment;
        DatabaseTransaction.executeUpdate(sql, c);
    }
    public static void  updateOrderComents(String comment, int containerid,Connection c) throws BecDatabaseException
    {
        String sql = "update oligocontainer set DESCR_OLIGO_PROCESSING='"+ comment +    "' where oligocontainerid="+containerid;
        DatabaseTransaction.executeUpdate(sql, c);
    }
     public void updateSequencingComents(String comment, Connection c) throws BecDatabaseException
    {
        String sql = "update oligocontainer set DESCR_SEQUENCING='"+ comment +      "' where oligocontainerid="+m_id;
        m_comment_sequencing = comment;
        DatabaseTransaction.executeUpdate(sql, c);
    }
    public static void  updateSequencingComents(String comment, int containerid,Connection c) throws BecDatabaseException
    {
        String sql = "update oligocontainer set DESCR_SEQUENCING='"+ comment +    "' where oligocontainerid="+containerid;
        DatabaseTransaction.executeUpdate(sql, c);
    }
     
    //**************************************************************
    private static ArrayList   getByRule(String sql, int mode) throws  BecDatabaseException
    {
        CachedRowSet crs = null;
        ArrayList containers = new ArrayList();
        OligoContainer container = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
                container = new OligoContainer();
                container.setId( crs.getInt("OLIGOCONTAINERID"));
                container.setLabel( crs.getString("LABEL"));
                if (mode == MODE_RESTORE_SAMPLES)
                    container. restoreSamples();
                container.setStatus(  crs.getInt("STATUS"));
                container.setUserId( crs.getInt("USERID"));
                container.setCommentOrder( crs.getString("DESCR_OLIGO_PROCESSING"));
                container.setCommentSequencing(  crs.getString("DESCR_SEQUENCING"));
                container.setCreateDate( crs.getDate("ORDERDATE"));
                containers.add(container);
             }
            return containers;
        } catch (Exception ex)
        {
            throw new BecDatabaseException("Error occured while initializing container: \n"+ex.getMessage());
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
    }
    
    //**************************************************************//
    //				Test				//
    //**************************************************************//
    
    // These test cases also include tests for Sample class.
    public static void main(String args[]) throws Exception
    {
       
        try
        {
           OligoContainer.getAll();
         
          
        }
        catch(Exception e)
        {
            System.out.println( e.getMessage());
        }
        System.exit(0);
        
    }
}
