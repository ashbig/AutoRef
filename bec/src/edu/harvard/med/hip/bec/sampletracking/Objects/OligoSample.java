/**
 * $Id: OligoSample.java,v 1.1 2003-12-08 19:16:15 Elena Exp $
 *
 * File     	: Sample.java
 * Date     	: 04162001
 * Author	: Dongmei Zuo
 *

 */

package edu.harvard.med.hip.bec.sampletracking.objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;

import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.*;

/**
 * Generic representation of Oligo samples.
 */
public class OligoSample
{
    
    public static final boolean MODE_RESTORE_OLIGO = true;
    private int           m_id = -1;
    private int           m_containerid = -1;
    private int           m_position = -1;
    private int           m_oligo_id = -1;
    private Oligo         m_oligo = null;
    private int           m_clone_id = -1;
    
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
    public OligoSample(int id  , int  containerid, int position,int  oligo_id  ,
                    Oligo         oligo  ,     int           clone_id )
            throws BecDatabaseException
    {
        if ( id == BecIDGenerator.BEC_OBJECT_ID_NOTSET) 
            m_id = BecIDGenerator.getID("sampleid");
        else
            m_id = id;
        m_containerid = containerid;
        m_position = position;
        m_oligo_id = oligo_id;
        m_oligo = oligo;
        m_clone_id = clone_id;
    }
    
   
          
    /**
     * Constructor.
     *
     * @param id The primary key of the sample table.
     *
     * @return A Sample object.
     * @exception BecUtilException, BecDatabaseException.
     */
    public OligoSample(int id, boolean mode) throws BecUtilException, BecDatabaseException
    {
        m_id = id;
        
        String sql = "select OLIGOSAMPLEID ,OLIGOID  ,CLONEID ,POSITION  ,OLIGOCONTAINERID from OLIGOSAMPLE where OLIGOSAMPLEID = "+id;
        RowSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 m_id = id;
                m_containerid = rs.getInt("OLIGOCONTAINERID");
                m_position = rs.getInt("POSITION");
                m_oligo_id = rs.getInt("OLIGOID");
                if ( mode )
                {
                    m_oligo = Oligo.getByOligoId(m_oligo_id);;
                }
                m_clone_id = rs.getInt("CLONEID");
               
            }
        } catch (Exception e)
        {
            throw new BecUtilException("Error occured while initializing oligo sample with id: "+id+"\n"+e.getMessage());
        } 
        finally
        {
           
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
   
     public  int           getId (){ return m_id  ;}
    public  int           getContainerId (){ return m_containerid  ;}
    public  int           getPosition (){ return m_position  ;}
    public  int           getOligoId (){ return m_oligo_id  ;}
    public  Oligo         getOligo (){ return m_oligo  ;}
    public  int           getCLoneId (){ return m_clone_id  ;} 
    
    public void             setId ( int v){  m_id  = v;}
    public void             setContainerId ( int v){  m_containerid  = v;}
    public void             setPosition ( int v){  m_position  = v;}
    public void             setOligoId ( int v){  m_oligo_id  = v;}
    public void           setOligo ( Oligo v){  m_oligo  = v;}
    public void             setCloneId ( int v){  m_clone_id  = v;}    
    //******************************************************//
    //			Testing				//
    //******************************************************//
    public static void main(String args[])
    {
       try
       {
           
       }
       catch(Exception e){}
    }
}
