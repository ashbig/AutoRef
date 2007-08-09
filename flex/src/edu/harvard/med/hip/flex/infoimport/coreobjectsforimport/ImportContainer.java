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
    
    public          void        setType(String v){ m_type = v;}
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
    public          void        addPublicInfo(PublicInfoItem v){if ( !m_additional_info.contains(v)) m_additional_info.add(v);}
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
    
     private  String getLabel( int thread_number, String project_code, String plate_type)
     {
        int count = Integer.toString(thread_number).length();
        StringBuffer temp = new StringBuffer(project_code + plate_type) ;
        for (int i = 0; i < 6 - count; i++)
        {
            temp.append("0");
        }
        
        return ( temp.toString() + thread_number );
    }
     
     
     public void insert(Connection conn, ArrayList errors, 
             int projectid, int workflowid,
             String project_code,  String plate_type) throws Exception
     {
        int threadid = FlexIDGenerator.getID("threadid");
        if (m_id == -1)
            m_id = FlexIDGenerator.getID("containerid");
        
        
        // reassigne label 
        PublicInfoItem p_info= new PublicInfoItem("USER_ID", m_label);
        this.addPublicInfo(p_info);
        m_label = getLabel( threadid,  project_code,  plate_type);
        int is_addition_info = ( PublicInfoItem.isAnyPublicInfoForSubmission(m_additional_info)) ? 1 : 0;
     
        String sql = 	"insert into containerheader " +
        "(containerid, containertype, locationid, label, threadid, additionalinfo) "+
        "values ("+m_id+",'"+m_type+"',"+m_location_id+",'"+m_label+"',"+threadid+","+
                is_addition_info+")";
      
        
        DatabaseTransaction.executeUpdate(sql,conn);
        PublicInfoItem.insertPublicInfo(  conn, "CONTAINERHEADER_NAME", 
            m_additional_info, m_id, "CONTAINERID",
            true, errors) ;
        
        ImportSample sample = null;
        //foreach sample, insert record into containercell and sample table
        for (int count = 0; count < m_samples.size(); count++)
        {
           try
           {
               sample = (ImportSample) m_samples.get(count);
                ImportConstruct.insert(conn, projectid,  workflowid,
                    sample.getConstructId(), Integer.parseInt( sample.getSequenceId()), 
                    sample.getConstructType() , sample.getConstructSize() );
                 sample.insert(conn, m_id, errors);
                 }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                throw new Exception("Cannot upload container "+ e.getMessage());
            }
           
         }
    }
     
     public void insertMGC(Connection conn, ArrayList errors, String file_name) throws Exception
     {
        PublicInfoItem pi_temp = null;  
        int threadid = FlexIDGenerator.getID("threadid");
        if (m_id == -1)
            m_id = FlexIDGenerator.getID("containerid");
        
        // replace label and add USER_ID property 
        
        String originalContainerName = m_label;
        PublicInfoItem p_info= new PublicInfoItem("USER_ID", m_label);
        this.addPublicInfo(p_info);
        m_label = MgcContainer.getLabel(FlexIDGenerator.getID("MGCCONTAINERLABEL"));
             
        
        int is_addition_info = ( PublicInfoItem.isAnyPublicInfoForSubmission(m_additional_info)) ? 1 : 0;
     
        String sql = 	"insert into containerheader " +
        "(containerid, containertype, locationid, label, threadid, additionalinfo) "+
        "values ("+m_id+",'"+m_type+"',"+m_location_id+",'"+m_label+"',"+threadid+","+is_addition_info +")";
        
        DatabaseTransaction.executeUpdate(sql,conn);
        
        
          pi_temp =  PublicInfoItem.getPublicInfoByName("MARKER",m_additional_info);
           String marker = ( pi_temp == null) ? "" : pi_temp.getValue();
         
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
     
     
     public void checkCloningStrategyIDAsigment() throws Exception
     {
         ImportSample sample = null;
         int cloning_strategy_id = -1;
         Hashtable cloning_strategies = new Hashtable();
         
         for (int count = 0; count < m_samples.size(); count++)
         {
             sample = (ImportSample) m_samples.get(count);
             if ( sample.getCloningStrategyId() == -1)
             {
                 cloning_strategy_id = sample.getCloningStrategyID( cloning_strategies);
                 if(cloning_strategy_id == -1) throw new Exception("Cannot define cloning strategy id for clone "+sample.toString());
                 sample.setCloningStrategyId(cloning_strategy_id);
             }
         }
     }
     
     private int          getCloningStrategyID(Hashtable cloning_strategies)
            throws Exception
     {
         int[] result = {-1,-1,-1};
         String vector_name = null;String linker_5p_name = null;String linker_3p_name = null;
         PublicInfoItem temp = null;
         
         temp = PublicInfoItem.getPublicInfoByName(ImportSample.SAMPLE_VECTOR,m_additional_info);
         if ( temp != null) vector_name = temp.getValue();
         temp = PublicInfoItem.getPublicInfoByName(ImportSample.SAMPLE_FIVE_PRIME_LINKER,m_additional_info);
         if ( temp != null)linker_5p_name = temp.getValue();
         temp = PublicInfoItem.getPublicInfoByName(ImportSample.SAMPLE_THREE_PRIME_LINKER,m_additional_info);
         if ( temp != null) linker_3p_name = temp.getValue();
        
        if (vector_name == null || linker_5p_name == null || linker_3p_name == null)
            return -1;
         
         String strategy_key =  vector_name+"_"+linker_5p_name +"_"+ linker_3p_name;
         // try to get cloning strategies for this parameters
         if ( cloning_strategies.get(strategy_key) != null)
         {
             return ((Integer)cloning_strategies.get(strategy_key)).intValue();
         }
         else
         {
             int clstr_id = CloningStrategy.findStrategyByVectorAndLinker( vector_name,  linker_5p_name, linker_3p_name);
             if (clstr_id == -1) return -1;
             
             cloning_strategies.put(strategy_key, new Integer(clstr_id));
             return clstr_id;
         }
       
     }
}
