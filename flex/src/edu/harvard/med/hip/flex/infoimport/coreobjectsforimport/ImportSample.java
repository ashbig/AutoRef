/*
 * ImportSample.java
 *
 * Created on June 22, 2007, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.coreobjectsforimport;

import java.util.*;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;
/**
 *
 * @author htaycher
 */
public class ImportSample
{
  
    public static final String      SAMPLE_POSITION = "POSITION";
    public static final String      SAMPLE_CONSTRUCT_TYPE = "CONSTRUCT_TYPE";

    private int         m_id = -1;
    private int         m_construct_id =-1;
    private ArrayList   m_additional_info = null;
    private String      m_type = Sample.ISOLATE;
    private int         m_containerid = -1;
    private int         m_position = -1;
    private String      m_status = Sample.GOOD;  
     private String         m_sequence_id = null;
     private String         m_construct_type = null;
     private String            m_construct_size_class = ImportConstruct.CONSTRUCT_SIZE_SMALL;
   
    
    /** Creates a new instance of ImportSample */
    public ImportSample(int position) 
    {
        m_position = position;
        m_additional_info = new ArrayList();
    }
     public ImportSample() 
    {
       m_additional_info = new ArrayList();
    }
    
    public int          getPosition(){ return m_position;}
    public String       getSequenceId(){ return m_sequence_id;}
     public void          setPosition(int v){  m_position = v;}
   
    public void         setConstructId(int v){    m_construct_id = v;}
    public void         setConstructType( String   v){      m_construct_type = v;}
     public void         setConstructSize( String v){           m_construct_size_class = v;}
    public void         setSequenceID(String v){ m_sequence_id = v;}
    public          void        addPublicInfo(PublicInfoItem v)
    {
        if (m_additional_info == null) m_additional_info = new ArrayList();
        m_additional_info.add(v);
    }
    public void         setContainerId(int v){         m_containerid = v;}
    public ArrayList    getPublicInfo() { return m_additional_info;}
    public boolean      isSameSample(ImportSample sample)
    {
         if (m_position != sample.getPosition()) return false;
         if ( !m_sequence_id.equalsIgnoreCase( sample.getSequenceId()) )return false;
         //for (int count = 0; count  < m_additional_info.size(); count++)
        // {
             // if (!m_additional_info.containsAll(sample.getPublicInfo()))
             //     return false;
        
        // }
       
         return true;
    }
    public void insert(Connection conn, int containerid, ArrayList errors) throws FlexDatabaseException
     {
           int is_addition_info = (m_additional_info.size() > 0 )? 1:0;
             if (m_id == -1)        m_id = FlexIDGenerator.getID("sampleid");
      
           String sql = "insert into sample (sampleid, sampletype, containerid, containerposition,"
          +"       constructid, status_gb, additionalinfo)"
         + "values ("+ m_id +",'"+ m_type +"',"+m_containerid+","+m_position
        + ","+m_construct_id + ",'"+m_status+"',"+is_addition_info+")";
       
        DatabaseTransaction.executeUpdate(sql,conn);
            
        sql = "insert into containercell (containerid, position, sampleid) " +
            "values(" + containerid + ","+m_position+","+m_id+")";
        DatabaseTransaction.executeUpdate(sql, conn);
         PublicInfoItem.insertPublicInfo(  conn, "SAMPLE_NAME", 
                m_additional_info, m_id, "SAMPLEID",                true, errors) ;
       
      }
       public void insertMGC(Connection conn,  int containerid, ArrayList errors) throws FlexDatabaseException
     {
             int is_addition_info = (m_additional_info.size() > 0 )? 1:0;
             if (m_id == -1)        m_id = FlexIDGenerator.getID("sampleid");
      
           String sql = "insert into sample (sampleid, sampletype, containerid, containerposition,"
          +"       constructid, status_gb, additionalinfo)"
         + "values ("+ m_id +",'"+ m_type +"',"+m_containerid+","+m_position
        + ","+m_construct_id + ",'"+m_status+"',"+is_addition_info+")";
       
            DatabaseTransaction.executeUpdate(sql,conn);

            sql = "insert into containercell (containerid, position, sampleid) " +
                "values(" + containerid + ","+m_position+","+m_id+")";
            DatabaseTransaction.executeUpdate(sql, conn);
            
           String mgc_status = ( m_sequence_id != null)? MgcSample.STATUS_AVAILABLE: MgcSample.STATUS_NO_SEQUENCE;
           String orientation = PublicInfoItem.getPublicInfoByName("ORIENTATION",m_additional_info).getValue();;
           String vector = PublicInfoItem.getPublicInfoByName("VECTOR",m_additional_info).getValue();;
           String row = PublicInfoItem.getPublicInfoByName("ROW",m_additional_info).getValue();;
           String column = PublicInfoItem.getPublicInfoByName("COLUMN",m_additional_info).getValue();;
           String mgc_id = PublicInfoItem.getPublicInfoByName("MGCID",m_additional_info).getValue();;
           String image_id = PublicInfoItem.getPublicInfoByName("IMAGEID",m_additional_info).getValue();;
           sql = "insert into mgcclone (mgccloneid, mgcid, imageid, vector, orgrow, orgcol, "
            + " sequenceid, status, orientation)  values ("+ m_id + "," + mgc_id +","+ image_id +
            ",'"+ vector + "','"+ row+"',"+ column + "," + m_sequence_id + ",'" + mgc_status + "',"+ orientation +")" ;
           DatabaseTransaction.executeUpdate(sql, conn);
           
       }
}
