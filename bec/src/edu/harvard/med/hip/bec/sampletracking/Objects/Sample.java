/**
 * $Id: Sample.java,v 1.4 2003-04-25 20:19:47 Elena Exp $
 *
 * File     	: Sample.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 *

 */

package edu.harvard.med.hip.bec.sampletracking.objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;

import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.*;

/**
 * Generic representation of all kinds of samples.
 */
public class Sample
{
    
    public final static String OLIGO_5P = "OLIGO_5P";
    public final static String OLIGO_3C = "OLIGO_3C";
    public final static String OLIGO_3F = "OLIGO_3F";
    
    public final static String ISOLATE = "ISOLATE";
    public final static String DNA = "DNA";
   
    public final static String EMPTY = "EMPTY";
    public final static String GEL = "GEL";
  
    public final static String CONTROL_POSITIVE = "CONTROL_POSITIVE";
    public final static String CONTROL_NEGATIVE = "CONTROL_NEGATIVE";
    
    
    private int           m_id = -1;
    private String        m_type = null;
    private int           m_containerid = -1;
    private int           m_position = -1;
    private int           m_isolatetracking_id = -1;
    private IsolateTrackingEngine           m_isolatetracking = null;
   
    /**
     * Constructor.
     *
     * @param id The primary key of the sample table.
     * @param type The type of the sample.
     * @param m_position The m_position of the sample on the container.
     * @param containerid The container id that the sample is on.
     * @param constructid The construct id that the sample is connected to.
     * @param oligoid The oligo id of the sample (if the sample is oligo sample).
     * @param status The status of the sample.
     *
     * @return A Sample object.
     */
    public Sample(int id, String type, int position,      int containerid)
            throws BecDatabaseException
    {
        if ( id == BecIDGenerator.BEC_OBJECT_ID_NOTSET) 
            m_id = BecIDGenerator.getID("sampleid");
        else
            m_id = id;
        m_type = type;
        m_position = position;
        m_containerid = containerid;
       
    }
    
   
          
    /**
     * Constructor.
     *
     * @param id The primary key of the sample table.
     *
     * @return A Sample object.
     * @exception BecUtilException, BecDatabaseException.
     */
    public Sample(int id) throws BecUtilException, BecDatabaseException
    {
        m_id = id;
        
        String sql = "select containerid, position,sampletype from sample where sampleid = "+id;
        RowSet rs = null;
        ResultSet newRs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
                m_type = rs.getString("SAMPLETYPE");
                m_containerid = rs.getInt("CONTAINERID");
                m_position = rs.getInt("POSITION");
            }
        } catch (NullPointerException e)
        {
            throw new BecUtilException("Error occured while initializing sample with id: "+id+"\n"+e.getMessage());
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing sample with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(newRs);
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Return the sample id.
     *
     * @return The sample id.
     */
    public int getId()    {        return m_id;    }
    
    /**
     * Return the m_position of this sample.
     *
     * @return The m_position of this sample.
     */
    public int getPosition()    {        return m_position;    }
    
    /**
     * Return the sample type.
     *
     * @return The sample type.
     */
    public String getType()    {        return m_type;    }
    
    
      
    
       
    /**
     * Return the construct id.
     *
     * @return The construct id.
     */
    public int getIsolateTrackingid()    {        return m_isolatetracking_id;    }
    public IsolateTrackingEngine        getIsolateTrackingEngine(){ return m_isolatetracking;}
 
    public void setIsolateTrackingid(int v)    {         m_isolatetracking_id = v;    }
    public void setIsolaterTracking(IsolateTrackingEngine id)    {        m_isolatetracking = id;    }
   
   
   
    /**
     * Gets the sequence this sample is from.
     *
     * @return the BecSequence this sample is from.
     */
    
    /*
    public BecSequence getBecSequence() throws BecDatabaseException
    {
        String sql =
        "select DISTINCT fs.sequenceid " +
        "from  flexsequence fs, constructdesign cd, sample s, oligo o " +
        "where "+
        "s.sampleid=" + m_id + " AND cd.sequenceid=fs.sequenceid AND " +
        "(s.constructid = cd.constructid " +
        "OR (s.oligoid=o.oligoid AND "+
        "(o.oligoid=cd.oligoid_5p OR o.oligoid=cd.oligoid_3p)))";
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
        BecSequence seq = null;
        try
        {
            while(rs.next())
            {
                seq =  new BecSequence(rs.getInt("SEQUENCEID"));
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return seq;
    }
    
    
    /**
     * Return the container id.
     *
     * @return The container id.
     */
    public int getContainerid()    {        return m_containerid;    }
    
    /**
     * Get the container this sample is in
     *
     * @return <Container> this sample is in
     */
    public Container getContainer()    throws BecDatabaseException, BecUtilException
    {
        Container container = new Container(getContainerid());
        return container;
    }
    
   
    /**
     * Return true if the sample is empty; false otherwise.
     *
     * @return True if the sample is empty; false otherwise.
     */
    public boolean isEmpty()    {        return (m_type.equals("EMPTY"));    }
    
    /**
     * Return  true if the sample is control sample; false otherwise.
     *
     * @return True if the sample is control sample; false otherwise.
     */
    public boolean isControl()    {        return (m_type.startsWith("CONTROL"));    }
    public boolean isClone()  
    {      
        boolean res = true;
        if (m_type.startsWith("CONTROL")) res = false;
        if (m_type.equals("EMPTY")) res = false;
        return res;
    }
   
   
   
    
    //function returns all history tree for the sample with/without results (optional)
    public ArrayList getSampleHistory(Connection conn) throws BecDatabaseException
    {
        return null;
    }
    //function returns all history tree for the sample with/without results (optional)
    public static ArrayList getSampleHistory(Connection conn, int sampleid) throws BecDatabaseException
    {
        return null;
    }
    /**
     * Insert the sample record into database.
     *
     * @param t DatabaseTransaction object.
     * @exception BecDatabaseException.
     */
    public void insert(Connection conn) throws BecDatabaseException
    {
        
        String sql = "insert into sample " +
        " (SAMPLEID  ,CONTAINERID  ,POSITION ,SAMPLETYPE)"
        +"values ("+m_id +","+ m_containerid+"," +m_position +",'"+m_type +"')";
        
       
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            if (m_isolatetracking != null)
                m_isolatetracking.insert(conn);
           
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
        
    }
    
    
   
    //******************************************************//
    //			Testing				//
    //******************************************************//
    public static void main(String args[])
    {
       
    }
}
