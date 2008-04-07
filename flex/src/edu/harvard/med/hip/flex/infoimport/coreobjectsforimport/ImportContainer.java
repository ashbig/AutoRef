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
    private     ArrayList       m_authors = null;
    
    
      public String toString()
    {
        StringBuffer seq = new StringBuffer();
        seq.append("ID: "+m_id +"\t");
        seq.append("Type: "+m_type+"\t");
        seq.append("Label: "+m_label+"\t");
        for (int count =0; count < m_additional_info.size(); count++)
        {
            seq.append( (PublicInfoItem) m_additional_info.get(count)+"\t");
        }
        return seq.toString();
     
    }
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
    public void                 addAuthor(ImportAuthor v){ if ( m_authors == null) m_authors=new ArrayList(); m_authors.add(v);}
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
    public          void        addPublicInfo(PublicInfoItem v){if ( ! PublicInfoItem.contains(m_additional_info, v)) m_additional_info.add(v);}
    public          void        addPublicInfoItems(ArrayList v)
    {
        for (int count = 0; count < v.size(); count++)
        {
            this.addPublicInfo( ( PublicInfoItem) v.get(count));
        }
    }

    public          void       setPublicInfo(ArrayList v){m_additional_info = v;}
    public          ArrayList        getPublicInfo(){ return m_additional_info;}
    public String               getLabel(){ return m_label;}
    public ArrayList            getAuthors(){ return m_authors;}
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
        
        if ( m_authors != null && m_authors.size() > 0)
        {
            ImportAuthor author = null;
            ArrayList author_info = null;
            for (int count = 0; count < m_authors.size(); count++)
            {
                author = (ImportAuthor) m_authors.get(count);
                author_info = author.convertToPublicInfoItem(count);
                PublicInfoItem.insertPublicInfo(  conn, "CONTAINERHEADER_NAME", 
                            author_info, m_id, "CONTAINERID",
                            true, errors) ;
            }
        }
        
        ImportSample sample = null;
        Hashtable construct_ids = new Hashtable(); int construct_id = 0;
        //foreach sample, insert record into containercell and sample table
        for (int count = 0; count < m_samples.size(); count++)
        {
           try
           {
               sample = (ImportSample) m_samples.get(count);
               if (  sample.getType().equals(Sample.ISOLATE))
               {
                   if (construct_ids.get(sample.getSequenceId()+":"+sample.getConstructType()) != null)
                   {
                       construct_id =((Integer) construct_ids.get(sample.getSequenceId()+":"+sample.getConstructType())).intValue() ;
                       sample.setConstructId( construct_id );
                   }
                   else
                   {
                        ImportConstruct.insert(conn, projectid,  workflowid,
                            sample.getConstructId(), Integer.parseInt( sample.getSequenceId()), 
                            sample.getConstructType() , sample.getConstructSize() );
                         construct_ids.put(sample.getSequenceId()+":"+sample.getConstructType(), new Integer(sample.getConstructId()));
                   }
               }
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
            
             if ( sample.getClone() != null && sample.getClone().getCloningStrategyId() == -1 )
             
             {
                 sample.getClone().assignCloningStrategyID( cloning_strategies);
                 cloning_strategy_id = sample.getClone().getCloningStrategyId( );
                 if(cloning_strategy_id == -1) throw new Exception("Cannot define cloning strategy id for clone "+sample.getClone().toString());
                 
             }
         }
     }
     
     
     public void sortSamplesByPosition()
     {
          Collections.sort(this.getSamples(), new Comparator()
          {
                public int compare(Object o1, Object o2) 
                {
                    return ((ImportSample) o1).getPosition() - ((ImportSample) o2).getPosition();
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
     }
     public  static void printContainers(HashMap containers, HashMap flex_sequences)
     {
         ImportContainer container = null;
          Iterator iter =  containers.values().iterator();
           while(iter.hasNext())
            {
                container = (ImportContainer) iter.next();
                container.print(           flex_sequences);
             }
     }
     
     
     public  void print( HashMap          i_flex_sequences)
     {
          ImportSample sample = null;
        System.out.println(this.toString());
        
        for (int count = 0; count < m_samples.size(); count++)
        {
             sample = (ImportSample) m_samples.get(count);
             System.out.println(sample.toString("\t"));
          }
       /*    Iterator iter = i_flex_sequences.keySet().iterator (  ) ; 
        HashMap  old_new_sequence_id = new HashMap();
        String flex_sequence_key = null;
         while ( iter.hasNext (  )  ) 
         {  
             flex_sequence_key = (String) iter.next();
          //   System.out.println(flex_sequence_key);
             if ( !( i_flex_sequences.get(flex_sequence_key) instanceof ImportFlexSequence)  )
                 continue;
             flex_sequence =  (ImportFlexSequence)i_flex_sequences.get(flex_sequence_key);
             flex_sequence.insert(conn, m_error_messages);
             i_flex_sequences.put( flex_sequence_key, String.valueOf(flex_sequence.getId()));
         }*/
      
     }
     
    
}
