/*
 * ImportContainer.java
 *
 * Created on June 22, 2007, 3:52 PM
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
public class ImportContainer 
{
    
    public static final String          PROPERTY_LABEL = "LABEL";
    
    private      int            m_id = -1;
    private     String          m_type = null;
    private     int             m_location_id = Location.CODE_FREEZER;
    private     String          m_label = null;
    private     ArrayList       m_samples = null;
    private     ArrayList           m_additional_info = null;
    private     int[]           i_submitted_samples = null;
 
    /** Creates a new instance of ImportContainer */
    public ImportContainer(String      type, String label, int number_of_samples_per_container)   
    {   
        m_type = type;
        m_label = label;
        m_samples=new ArrayList();
        m_additional_info = new ArrayList();
        // arrays are 0 based, position - 1 based
         i_submitted_samples = new int[ number_of_samples_per_container + 1];
    }
    
    public ImportContainer( String label,  int number_of_samples_per_container)   
    {   
        m_label = label;
        m_samples=new ArrayList();
        m_additional_info = new ArrayList();
         i_submitted_samples = new int[ number_of_samples_per_container + 1];
    }
    public          void        setLocation(int v){ m_location_id = v;}
    public          ArrayList   getSamples(){ return m_samples;}
    public int                  getId(){ return m_id;}
    public          void        addSample(ImportSample v)   throws Exception
    {  
        ImportSample sample = null;
        if ( v.getPosition()  > i_submitted_samples.length  )
        {
            throw new Exception ("Wrong sample position "+ v.getPosition());
        }
        if ( i_submitted_samples[ v.getPosition()] == 1 )
        {
            sample =  getSampleByPosition( v.getPosition() ) ;
            if ( v.isSameSample(sample)) return ;
            throw new Exception ("Duplicated sample "+ m_label +" "+ v.getPosition());
           
        }
            
        i_submitted_samples[ v.getPosition()] = 1;
        m_samples.add(v);   
    }
    public          void        addPublicInfo(PublicInfoItem v){m_additional_info.add(v);}
    public          void       setPublicInfo(ArrayList v){m_additional_info = v;}
    public          ArrayList        getPublicInfo(){ return m_additional_info;}
    public String               getLabel(){ return m_label;}
    
    private ImportSample        getSampleByPosition(int position)
    {
        ImportSample sample = null;
        for (int count = 0; count <  m_samples.size(); count++)
        {
            sample = (ImportSample) m_samples.get(count);
            if ( sample.getPosition() == position)
                return sample;
        }
        return null;
    }
     public void insert(Connection conn, ArrayList errors) throws FlexDatabaseException
     {
        int threadid = FlexIDGenerator.getID("threadid");
        if (m_id == -1)
            m_id = FlexIDGenerator.getID("containerid");
        int is_addition_info = (m_additional_info.size() > 0 )? 1:0;
        String sql = 	"insert into containerheader " +
        "(containerid, containertype, locationid, label, threadid, additionalinfo) "+
        "values ("+m_id+",'"+m_type+"',"+m_location_id+",'"+m_label+"',"+threadid+","+
                is_addition_info+")";
        
        DatabaseTransaction.executeUpdate(sql,conn);
        PublicInfoItem.insertPublicInfo(  conn, "CONTAINERHEADER_NAME", 
            m_additional_info, m_id, "CONTAINERID",
            true, errors) ;
        //foreach sample, insert record into containercell and sample table
        for (int count = 0; count < m_samples.size(); count++)
        {
            ((ImportSample) m_samples.get(count)).insert(conn, m_id, errors);
        }
    }
     
     public void insertMGC(Connection conn, ArrayList errors, String file_name) throws FlexDatabaseException
     {
          
        int threadid = FlexIDGenerator.getID("threadid");
        if (m_id == -1)
            m_id = FlexIDGenerator.getID("containerid");
        String sql = 	"insert into containerheader " +
        "(containerid, containertype, locationid, label, threadid) "+
        "values ("+m_id+",'"+m_type+"',"+m_location_id+",'"+m_label+"',"+threadid+")";
        
        DatabaseTransaction.executeUpdate(sql,conn);
        
         String originalContainerName = PublicInfoItem.getPublicInfoByName("ORIGINALLABEL",m_additional_info).getValue();
         String marker =  PublicInfoItem.getPublicInfoByName("MARKER",m_additional_info).getValue();
         String sql1 = "insert into mgccontainer " +
            "(mgccontainerid, filename, oricontainer, marker ,glycerolcontainerid,CULTURECONTAINERID, dnacontainerid) "+
            "values ("+ m_id + ",'" +file_name+ "','"+ originalContainerName + "','" + marker + "', -1, -1,-1)";
            DatabaseTransaction.executeUpdate(sql1,conn);
      
        
        //foreach sample, insert record into containercell and sample table
        for (int count = 0; count < m_samples.size(); count++)
        {
            ((ImportSample) m_samples.get(count)).insertMGC(conn, m_id, errors);
        }
    }
}
