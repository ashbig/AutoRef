/**
 * $Id: Sample.java,v 1.1 2003-03-27 17:45:41 Elena Exp $
 *
 * File     	: Sample.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 *

 */

package edu.harvard.med.hip.bec.sampletracking.Objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;

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
    private int           m_agartracking_id = -1;
    private int           m_oligoid = -1;
    
    private Result          m_result = null;
    
   
    
      
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
    public Sample(int id, String type, int position, 
            int containerid, int agarid, int oligoid)
            throws BecDatabaseException
    {
        if ( id == BecIDGenerator.BEC_OBJECT_ID_NOTSET) 
            m_id = BecIDGenerator.getID("sampleid");
        else
            m_id = id;
        m_type = type;
        m_position = position;
        m_containerid = containerid;
        m_agartracking_id = agarid;
        m_oligoid = oligoid;
     
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
        
        String sql = "select * from sample where sampleid = "+id;
        RowSet rs = null;
        ResultSet newRs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                
               /* type = rs.getString("SAMPLETYPE");
                containerid = rs.getInt("CONTAINERID");
                m_position = rs.getInt("CONTAINERPOSITION");
                
                Object construct = rs.getObject("CONSTRUCTID");
                if(construct != null)
                    constructid = ((BigDecimal)construct).intValue();
                else
                    constructid = -1;
                
                Object oligo = rs.getObject("OLIGOID");
                if(oligo != null)
                    oligoid = ((BigDecimal)oligo).intValue();
                else
                    oligoid = -1;
                
                status = rs.getString("STATUS_GB");
                
                String newSql = "select distinct f.cdslength as cdslength from flexsequence f, "+
                "constructdesign c, sample s where f.sequenceid = "+
                "c.sequenceid and (s.constructid = c.constructid "+
                "or s.oligoid=c.oligoid_5p) and s.sampleid = "+id;
                newRs = t.executeQuery(newSql);
                if(newRs.next())
                {
                    cdslength = newRs.getInt("CDSLENGTH");
                }
                **/
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
     * Return the oligo id.
     *
     * @return The oligo id.
     */
    public int getOligoid()    {        return m_oligoid;    }
    
       
    /**
     * Return the construct id.
     *
     * @return The construct id.
     */
    public int getAgarTrackingid()    {        return m_agartracking_id;    }
    
   
   
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
    public Container getContainer()
    throws BecDatabaseException, BecUtilException
    {
        Container container = new Container(getContainerid());
        return container;
    }
    
    /**
     * Set the construct id to the given value.
     *
     * @param id The value to be set to.
     */
    public void setAgarTrackingid(int id)    {        m_agartracking_id = id;    }
    
    /**
     * Set the oligo id to the given value.
     *
     * @param oligoid The value to be set to.
     */
    public void setOligoid(int oligoid)    {        m_oligoid = oligoid;   }
    
   
    
    /**
     * Set the cdslength to the given value.
     *
     * @param cdslength The value to be set to.
     */
   // public void setCdslength(int cdslength)    {        m_cdslength = cdslength;    }
    
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
    public boolean isControl()    {        return (m_type.equals("CONTROL"));    }
    
    /**
     * Set the sample result to the given result.
     *
     * @param result The result that the sample result will be set to.
     */
    public void setResult(Result result)    {        m_result = result;    }
    
    /**
     * Returns true if the given sample is the same; returns false otherwise.
     *
     * @param sample The given sample for comparison.
     *
     * @return True if the given sample is the same, false otherwise.
     */
    public boolean isSame(Sample sample)    {        return (m_id == sample.getId());    }
    
   
    
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
        /*
        String sql = "insert into sample\n"+
        "(sampleid, sampletype, containerid, containerm_position";
        String valuesql = "values ("+id +",'"+ type +"',"+containerid+","+m_position;
        
        if(constructid != -1)
        {
            sql = sql + ",constructid";
            valuesql = valuesql + ","+constructid;
        }
        
        if(oligoid != -1)
        {
            sql = sql + ",oligoid";
            valuesql = valuesql + ","+oligoid;
        }
        
        if(status != null)
        {
            sql = sql + ",status_gb";
            valuesql = valuesql + ",'"+status+"'";
        }
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            
            sql = sql + ")\n"+valuesql + ")";
            stmt.executeUpdate(sql);
            
            sql = "insert into containercell " +
            "(containerid, m_position, sampleid) " +
            "values(" + containerid + ","+m_position+","+id+")";
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
         *
         *
         **/
    }
    
    
    
    //******************************************************//
    //			Testing				//
    //******************************************************//
    public static void main(String args[])
    {
       
    }
}
