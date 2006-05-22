//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * ProcessHistory.java
 *
 * Created on June 17, 2003, 3:21 PM
 */

package edu.harvard.med.hip.bec.util_objects;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.ui_objects.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.database.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;
/**
 *
 * @author  htaycher
 */
public class ProcessHistory
{
    
    private int         m_process_id = -1;
    private String      m_process_name = null;
    private String      m_process_date = null;
    private String      m_process_username = null;
    private ArrayList   m_configs = null;
    
    
    
    /** Creates a new instance of ProcessHistory */
    public ProcessHistory()
    {
        m_configs = new ArrayList();
    }
    public int         getId()    { return m_process_id   -1;}
    public String      getName()    { return m_process_name    ;}
    public String      getDate()    { return m_process_date    ;}
    public String      getUsername()    { return m_process_username    ;}
    public ArrayList   getConfigs()    { return m_configs    ;}
    
    public void         setId(int v)    {   m_process_id   = v;}
    public void      setName(String v)    {   m_process_name    = v;}
    public void      setDate(String v)    {   m_process_date    = v;}
    public  void     setUsername(String v)    {   m_process_username    = v;}
    public  void  setConfis(ArrayList v)    {   m_configs    = v;}
    public  void  addConfis(int id, int type)
    {
        if ( id < 1) return;
        UIConfigItem conf = new UIConfigItem( id, type);
        m_configs.add(conf);
    }
    
    
    public static ArrayList    getProcessHistory(int object_type, String object_ids) throws Exception
    {
        ArrayList items = Algorithms.splitString( object_ids);
        ArrayList process_items = new ArrayList();
         //get clone descriptions 
        ArrayList clone_descriptions = getCloneInfo(  object_type,  items);
        
        ArrayList process_items_per_item = new ArrayList();
        PreparedStatement   pstm_config_info = DatabaseTransaction.getInstance().requestConnection().prepareStatement("select CONFIGID,   CONFIGTYPE from processconfig  where processid=?");
        CloneDescription clone = null;
        
        for (int index = 0; index < clone_descriptions.size(); index ++)
        {
            clone = (CloneDescription) clone_descriptions.get(index);
            try
            { 
               if (  object_type ==  Constants.ITEM_TYPE_CLONEID )
                  process_items.add(  getCloneHistory(clone ,pstm_config_info) );
               else if  (  object_type ==  Constants.ITEM_TYPE_PLATE_LABELS )
                  process_items.add(  getContainerHistory(clone ,pstm_config_info) );
            }
            catch (Exception e)
            {
                ArrayList err_array = new ArrayList();
                err_array.add("Error processing item history "+e.getMessage());
                process_items.add( new ItemHistory(String.valueOf( clone.getCloneId()),err_array , ItemHistory.HISTORY_FAILED));
            }
        }
         
        return process_items;
    }
    
    
    //--------------------
    private static ItemHistory   getCloneHistory(CloneDescription clone, PreparedStatement pstm_config_info)throws Exception
    {
        ArrayList history = new ArrayList(); String sql = null;
        //upload plate
        ArrayList process_items = getProcessItemsForSearchItem(null,Constants.PROCESS_OBJECT_TYPE_CONTAINER, clone.getContainerId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
        
         //order end reads - result
        sql = "select resultid from result where sampleid  in ("+ clone.getSampleId() +")";
       process_items = getProcessItemsForSearchItem(sql,Constants.PROCESS_OBJECT_TYPE_RESULT, clone.getContainerId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
    
      //clone evaluation
        process_items = getProcessItemsForSearchItem(null,Constants.PROCESS_OBJECT_TYPE_CONSTRUCT, clone.getConstructId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
   //design oligo     
        process_items = getProcessItemsForSearchItem(null,Constants.PROCESS_OBJECT_TYPE_OLIGOCALCULATION, clone.getBecRefSequenceId(),pstm_config_info);  
       if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
      
        //oligo order (containerid - oligo container
        sql = "select oligocontainerid from oligosample where cloneid = "+clone.getCloneId();
        process_items = getProcessItemsForSearchItem(sql,Constants.PROCESS_OBJECT_TYPE_CONTAINER, clone.getCloneId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
        
        //approve oligo PROCESS_OBJECT_TYPE_OLIGO_ID
        sql = "select oligoid from geneoligo where oligocalculationid in ( select oligocalculationid from oligo_calculation where sequenceid = "+clone.getBecRefSequenceId() +")";
          process_items = getProcessItemsForSearchItem(sql,Constants.PROCESS_OBJECT_TYPE_OLIGO_ID, clone.getBecRefSequenceId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
    
    //run discrepancy finder    
          sql = "select sequenceid from assembledsequence where isolatetrackingid = " + clone.getIsolateTrackingId();
        process_items = getProcessItemsForSearchItem(sql,Constants.PROCESS_OBJECT_TYPE_CLONE_SEQUENCE, clone.getCloneSequenceId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
     
        
        //clone final status set
        sql = "select configid from process_object where objecttype="+Constants.PROCESS_OBJECT_TYPE_CLONEID+" objectid = " + clone.getIsolateTrackingId();
        process_items = getProcessItemsForSearchItem(null,Constants.PROCESS_OBJECT_TYPE_CLONEID, clone.getCloneId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
  
       sortByDate(history);
       return new ItemHistory(String.valueOf(clone.getCloneId() ), history, ItemHistory.HISTORY_PROCESSED);
     
    }
    
    private static ItemHistory   getContainerHistory(CloneDescription clone, PreparedStatement pstm_config_info)throws Exception
    {
        ArrayList history = new ArrayList(); String sql = null;
        //upload plate
        ArrayList process_items = getProcessItemsForSearchItem(null,Constants.PROCESS_OBJECT_TYPE_CONTAINER, clone.getContainerId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
      //clone evaluation
        process_items = getProcessItemsForSearchItem(null,Constants.PROCESS_OBJECT_TYPE_CONSTRUCT, clone.getConstructId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
         //order end reads - result
        sql = "select resultid from result where sampleid  in ("+ clone.getSampleId() +")";
       process_items = getProcessItemsForSearchItem(sql,Constants.PROCESS_OBJECT_TYPE_RESULT, clone.getContainerId(),pstm_config_info);  
        if ( process_items!= null && process_items.size() > 0 ) history.addAll(process_items);
   
       sortByDate(history);
       return new ItemHistory(String.valueOf(clone.getCloneId() ), history, ItemHistory.HISTORY_PROCESSED);
     
    }
    
  
    
     private static  ArrayList getCloneInfo( int object_type, ArrayList items) throws BecDatabaseException
     {
        ArrayList clones = new ArrayList();String additional_id = null;
        String sql = null;
        CloneDescription clone = null;
        ResultSet rs = null;
        sql = constructQueryString( object_type,items);
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 clone = new CloneDescription();
                 clone.setCloneId (rs.getInt("CLONEID"));
                 clone.setConstructId (rs.getInt("CONSTRUCTID"));
                 clone.setContainerId (rs.getInt("CONTAINERID"));
                 clone.setBecRefSequenceId( rs.getInt("REFSEQUENCEID"));
                 clone.setPosition (rs.getInt("POSITION"));
                 clone.setIsolateTrackingId (rs.getInt("ISOLATETRACKINGID"));
                 clone.setSampleId(rs.getInt("SAMPLEID"));
                 clones.add(clone);
            }
            return clones;
            
        }
        catch(Exception e)
        {
          throw new BecDatabaseException("Cannot get data for clone "+e.getMessage() +"\n"+sql);
        }
    }
    
      
   
    private static String constructQueryString(int object_type, ArrayList items)
    {
        String sql = null;
        switch (object_type)
        {
            case Constants.ITEM_TYPE_PLATE_LABELS://plates
            {
              return "select s.containerid, POSITION, s.sampleid as sampleid,  flexcloneid  as CLONEID, sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID  "
 +" from flexinfo f,isolatetracking i, sample s,  sequencingconstruct sc, containerheader ch   where  f.isolatetrackingid=i.isolatetrackingid and "
+" i.sampleid=s.sampleid and ch.containerid=s.containerid   and sc.constructid=i.constructid  and s.sampleid in "
+" (select min(sampleid ) from sample where sampletype='ISOLATE' and containerid in (select containerid from containerheader where Upper(label) ='"+items.get(0)+"') )";

            } 
            case Constants.ITEM_TYPE_CLONEID:
            {
                 return "select containerid, POSITION,  s.sampleid as sampleid,flexcloneid  as CLONEID, sc.refsequenceid as refsequenceid,  i.CONSTRUCTID,  i.ISOLATETRACKINGID as ISOLATETRACKINGID  "
        +"  from flexinfo f,isolatetracking i, sample s,  sequencingconstruct sc "
        +"  where  f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
        +"  and sc.constructid=i.constructid and flexcloneid in ("+Algorithms.convertStringArrayToString(items,"," )+") ";
            }
            default : return "";
        }
       
   
    }
   
   
    
    private static ArrayList          getProcessItemsForSearchItem(String sql ,int object_type, int object_id, PreparedStatement pstm_config_info) throws Exception
    {
        if ( sql == null)
        {
            sql =  "select p.EXECUTIONDATE as EXECUTIONDATE ,pd.processname as processname,p.processid as processid, "
            +" (select username from userprofile where userid=( select researcherid from request where requestid = "
            +"  p.requestid)) as username  from process p, processdefinition pd "
            +" where pd.processdefinitionid=p.processdefinitionid and processid in (select processid from process_object where ("
            +" (objectid in ( "+object_id +") and objecttype="+object_type+")  )) order by processid desc";
        }
        else 
       {
            sql =  "select p.EXECUTIONDATE as EXECUTIONDATE ,pd.processname as processname,p.processid as processid, "
            +" (select username from userprofile where userid=( select researcherid from request where requestid = "
            +"  p.requestid)) as username  from process p, processdefinition pd "
            +" where pd.processdefinitionid=p.processdefinitionid and processid in (select processid from process_object where ("
            +" (objectid in ( "+sql +") and objecttype="+object_type+")  )) order by processid desc";
        }
       
            
        return getProcessItems(sql, pstm_config_info);
       
    }
    
     
    private static ArrayList          getProcessItems(String query_sql,PreparedStatement pstm_config_info) throws Exception
    {
        if ( query_sql == null) return null;
        ArrayList items = new ArrayList();
        DatabaseTransaction t = null;
        RowSet crs = null; RowSet crs1 = null;
        ProcessHistory pr_history = null;
        crs = DatabaseTransaction.getInstance().executeQuery(query_sql);
        int cur_pr_history = -1;
        while(crs.next())
        {
            cur_pr_history = crs.getInt("processid") ;
            pr_history= new ProcessHistory();
            pr_history.setId(cur_pr_history );
            pr_history.setName( crs.getString("processname") );
            pr_history.setDate( crs.getString("EXECUTIONDATE") );
            pr_history.setUsername( crs.getString("username") );
            
            pstm_config_info.setInt(1,cur_pr_history);
            crs1 = DatabaseTransaction.getInstance().executeQuery(pstm_config_info);
            while(crs1.next())
            {
                pr_history.addConfis (crs1.getInt("configid"), crs1.getInt("configtype"));
            }
            items.add(pr_history);
           
           
        }
        return items;
    }
    
    private static ArrayList sortByDate(ArrayList process_items)
    {
        for (int i=0; i< process_items.size();i++)
            Collections.sort(process_items, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    return ((ProcessHistory) o1).getDate().compareTo(((ProcessHistory) o2).getDate());
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            
            
            return process_items;
    }
    
    
    
    
     public static void main(String [] args) throws Exception
    {
          ArrayList h = ProcessHistory.getProcessHistory(Constants.ITEM_TYPE_CLONEID, "158499 158507 ");
    //      ArrayList h = ProcessHistory.getProcessHistory(Constants.ITEM_TYPE_PLATE_LABELS, "YGS000357-1");
         
          System.out.print(h.size());
    
    }
    
}
