/*
 * ImportClone.java
 *
 * Created on October 8, 2007, 1:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.coreobjectsforimport;

import java.util.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
/**
 *
 * @author htaycher
 */
public class ImportClone {
    
    
      // for clones tables only
    public static final String      CLONE_STATUS ="CLONE_STATUS";
    public static final String      CLONE_TYPE ="CLONE_TYPE";
    public static final String      CLONING_STRATEGYID="CLONING_STRATEGYID";
     public static final String     FIVE_PRIME_LINKER="FIVE_PRIME_LINKER";
    public static final String      THREE_PRIME_LINKER="THREE_PRIME_LINKER";
    public static final String      VECTOR="VECTOR";
    public static final String      CLONE_SEQUENCE_TEXT="CLONE_SEQUENCE_TEXT";
   
    
public static final String      CLONE_STATUS_SEQUENCE_VERIFIED = "SEQUENCE VERIFIED";
public static final String      CLONE_STATUS_UNSEQUENCED = "UNSEQUENCED";
public static final String      CLONE_STATUS_IN_PROCESS = "IN PROCESS";
public static final String      CLONE_STATUS_SUCESSFUL = "SUCESSFUL";
public static final String      CLONE_STATUS_FAIL = "FAIL";
public static final String      CLONE_STATUS_FAILED_BY_SEQUENCE_VALIDATION ="FAILED BY SEQUENCE VALIDATION";


       
    private int         m_id = -1;
    private String         m_user_id = null;
  
    private ArrayList   m_additional_info = null;
    private ArrayList   m_authors = null;
    private ArrayList   m_publications = null;
   
    
    private String         m_sequence_text = null;
    private String             i_clone_type = CloneSample.MASTER;
    private String              i_clone_status = CLONE_STATUS_IN_PROCESS;
   
    private int             i_cloning_strategy_id = -1;
    private String          i_vector_name = null;
    private String          i_5_linker_name = null;
    private String          i_3_linker_name = null;
    
  
    /** Creates a new instance of ImportClone */
    public ImportClone() {m_additional_info = new ArrayList();
    }
    
    public int          getId() throws Exception{  if (m_id == -1)        m_id = FlexIDGenerator.getID("sampleid");return m_id;}
   public String           getUserId(){ return m_user_id;}
    public String       getSequenceText(){ return m_sequence_text;}
    
    public int          getCloningStrategyId(   ){  return    i_cloning_strategy_id;}
    public String       getType(   ){  return    i_clone_type ;}
    public String       getStatus(   ){  return     i_clone_status ;}
    public ArrayList    getPublicInfo() { return m_additional_info;}
    public ArrayList    getAuthors(){ return m_authors;}
    public ArrayList    getPublications(){ return m_publications;}
    public String       getVectorName(){ return   i_vector_name ;}
    public String       get5LinkerName(){  return     i_5_linker_name ;}
    public String       get3LinkerName(){ return      i_3_linker_name ;}
     
    
    public void         setSequenceText(String v){ m_sequence_text = v;}
    public          void        addPublicInfo(PublicInfoItem v)
    {
         if ( ! PublicInfoItem.contains( m_additional_info, v))
                m_additional_info.add(v);
    }
    public void          setCloningStrategyId(int v   ){      i_cloning_strategy_id = v;}
     public void          setVectorName(String v ){    i_vector_name =v ;}
    public void          set5LinkerName(String v ){       i_5_linker_name =v;}
    public void          set3LinkerName(String v ){       i_3_linker_name =v;}
  
    public void         setType( String v  ){      i_clone_type  = v;}
    public void         setStatus( String v  ){       i_clone_status  = v;}
    public void         addAuthor(ImportAuthor v){ if(m_authors==null) m_authors= new ArrayList(); m_authors.add(v);}
    public void         addPublication(ImportPublication v){ if(m_publications==null) m_publications= new ArrayList(); m_publications.add(v);}
   
    public void         addPublicInfoItems(ArrayList v)
    {
        PublicInfoItem p_info = null;
        for (int count = 0; count < v.size(); count++)
        {
            p_info = (PublicInfoItem) v.get(count);
            if ( ! PublicInfoItem.contains( m_additional_info, p_info))
                m_additional_info.add(p_info);
        }
    }
   public void         setId(int v){ m_id= v;}
    public void         setUserId(String v)
    { 
        m_user_id= v;
        PublicInfoItem p_info = new  PublicInfoItem(FileStructureColumn.PROPERTY_NAME_USER_ID,v, null,null, true);
        this.addPublicInfo(p_info);
    }
    
     public String toString()
    {
        StringBuffer seq = new StringBuffer();
        seq.append("ID: "+m_id +"\n");
         seq.append("User ID: "+m_user_id +"\n");
        seq.append("Clone Type: "+i_clone_type+"\n");
        seq.append("Clone Status: "+i_clone_status+"\n");
        seq.append("Vector Name: "+i_vector_name+"\n");
        seq.append("5 Linker: "+i_5_linker_name+"\n");
        seq.append("3 Linker: "+i_3_linker_name+"\n");
     
    
        for (int count =0; count < m_additional_info.size(); count++)
        {
            seq.append( (PublicInfoItem) m_additional_info.get(count)+"\n");
        }
        return seq.toString();
     
    }
    public  boolean      isValidCLoneStatus()   {         return isValidCLoneStatus(i_clone_status);   }
    protected static boolean      isValidCLoneStatus(String status)
   {
         if ( status.intern() == CLONE_STATUS_SEQUENCE_VERIFIED ||
                 status.intern() == CLONE_STATUS_UNSEQUENCED 
                 || status.intern() == CLONE_STATUS_IN_PROCESS
                 || status.intern() == CLONE_STATUS_SUCESSFUL
                 || status.intern() == CLONE_STATUS_FAIL 
                 || status.intern() == CLONE_STATUS_FAILED_BY_SEQUENCE_VALIDATION) return true	;//SEQUENCE REJECTED
     return false;
   }
   
     public int      mapCloneStatusToConstructStatus(){ return mapCloneStatusToConstructStatus(i_clone_status);}
   
     protected static int      mapCloneStatusToConstructStatus(String status)
   {
      
     if ( status.intern() == CLONE_STATUS_SEQUENCE_VERIFIED)   return 4	;//SEQUENCE VERIFIED
     if ( status.intern() == CLONE_STATUS_UNSEQUENCED ) return 1	;//CLONE OBTAINED
     if ( status.intern() == CLONE_STATUS_IN_PROCESS) return 3	;//IN SEQUENCING PROCESS	
     if ( status.intern() == CLONE_STATUS_SUCESSFUL) return 4	;//SEQUENCE VERIFIED
     if ( status.intern() == CLONE_STATUS_FAIL ) return 6	;//FAILED CLONING
     if ( status.intern() == CLONE_STATUS_FAILED_BY_SEQUENCE_VALIDATION) return 5	;//SEQUENCE REJECTED
     return -1;
   }
     
     public void         reasignCloneProperties(ImportClone temp_clone)
     {
         this.setStatus(temp_clone.getStatus());
         this.setType( temp_clone.getType());
         this.set5LinkerName(temp_clone.get5LinkerName());
         this.set3LinkerName(temp_clone.get3LinkerName());
         this.setSequenceText(temp_clone.getSequenceText());
         this.setVectorName(temp_clone.getVectorName());
         this.setCloningStrategyId(temp_clone.getCloningStrategyId());
         this.addPublicInfoItems( temp_clone.getPublicInfo());
         this.setUserId( temp_clone.getUserId());
          }
     
     
      public  void          assignCloningStrategyID(Hashtable cloning_strategies)
            throws Exception
     {
         if ( i_cloning_strategy_id != -1) return ;
         int[] result = {-1,-1,-1};
         
         if (i_vector_name == null || i_5_linker_name == null || i_3_linker_name == null)
            return ;
         
         String strategy_key =  i_vector_name+"_"+i_5_linker_name +"_"+ i_3_linker_name;
         // try to get cloning strategies for this parameters
         if ( cloning_strategies.get(strategy_key) != null)
         {
             i_cloning_strategy_id = ((Integer)cloning_strategies.get(strategy_key)).intValue();
         }
         else
         {
             i_cloning_strategy_id = CloningStrategy.findStrategyByVectorAndLinker( i_vector_name,  i_5_linker_name, i_3_linker_name);
             if (i_cloning_strategy_id == -1) return ;
             cloning_strategies.put(strategy_key, new Integer(i_cloning_strategy_id));
             return;
         }
       
     }
      
      public void       insertAuthor(Connection conn) throws Exception
      {
            ImportAuthor author = null;
            String sql = null;
            if ( m_authors != null && m_authors.size() > 0)
            {
                for (int count = 0; count < m_authors.size(); count++)
                {
                    author = (ImportAuthor) m_authors.get(count);
                    if ( author == null) continue;
                    if ( author.getId() == -1) author.insert(conn);
                    // insert connector
                     sql = 	"insert into CLONEAUTHOR  (CLONEID  ,AUTHORID ,AUTHORTYPE) "+
                      " values ("+m_id+","+author.getId()+",'"+author.getType() +"')";
                      DatabaseTransaction.executeUpdate(sql,conn);
                }
            }
      }
      
      public void       insertPublications(Connection conn) throws Exception
      {
            ImportPublication publication = null;
            String sql = null;
            if ( m_publications != null && m_publications.size() > 0)
            {
                for (int count = 0; count < m_publications.size(); count++)
                {
                    publication = (ImportPublication) m_publications.get(count);
                    if ( publication.getId() == -1) publication.insert(conn);
                    // insert connector
                     sql = 	"insert into CLONEpublication  (CLONEID  ,publicationid) "+
                      " values ("+m_id+","+publication.getId() +")";
                      DatabaseTransaction.executeUpdate(sql,conn);
                }
            }
      }
}
