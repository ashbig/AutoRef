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
        String item_id = null; 
        ArrayList process_items_per_item = new ArrayList();
        PreparedStatement   pstm_config_info = DatabaseTransaction.getInstance().requestConnection().prepareStatement("select CONFIGID,   CONFIGTYPE from processconfig  where processid=?");
        ItemHistory item_entry = null;
        
        for (int index = 0; index < items.size(); index ++)
        {
            item_id = (String) items.get(index);
            try
            {
                process_items_per_item = getProcessItemsForSearchItem(object_type,item_id ,pstm_config_info);
                item_entry = new ItemHistory(item_id, process_items_per_item, ItemHistory.HISTORY_PROCESSED);
                process_items.add(item_entry);
            }
            catch (Exception e)
            {
                ArrayList err_array = new ArrayList();
                err_array.add("Error processing item history "+e.getMessage());
                process_items.add( new ItemHistory(item_id,err_array , ItemHistory.HISTORY_FAILED));
            }
        }
        return process_items;
    }
    
    
    //--------------------
    
    
    
    private static ArrayList          getProcessItemsForSearchItem(int object_type, String  item, PreparedStatement pstm_config_info) throws Exception
    {
        String sql = getSqlString( object_type,  item);
        if (sql != null)
            return getProcessItems(sql, pstm_config_info);
        else
            return null;
    }
    
    private static String             getSqlString(int object_type, String item)
    {
        String sql = null;
        String resultid =  getQuerySqlStringPerObjectType( object_type, item,  Constants.PROCESS_OBJECT_TYPE_RESULT);
        String containerid =getQuerySqlStringPerObjectType( object_type, item,  Constants.PROCESS_OBJECT_TYPE_CONTAINER);
        String constructid = getQuerySqlStringPerObjectType( object_type, item,  Constants.PROCESS_OBJECT_TYPE_CONSTRUCT);
        String clonesequenceid = getQuerySqlStringPerObjectType( object_type, item,  Constants.PROCESS_OBJECT_TYPE_CLONE_SEQUENCE);
        String refsequenceid  =getQuerySqlStringPerObjectType( object_type, item,  Constants.PROCESS_OBJECT_TYPE_REFSEQUENCE);
        boolean isAddOr = false;
        sql = "select p.EXECUTIONDATE as EXECUTIONDATE ,pd.processname as processname,p.processid as processid, "
        +" (select username from userprofile where userid=( select researcherid from request where requestid = "
        +"  p.requestid)) as username "
        //+" ,(select configtype from processconfig where processid= p.processid) as configtype "
       // +" ,(select configid from processconfig where processid= p.processid) as configid 
        +" from process p, processdefinition pd "
        +" where pd.processdefinitionid=p.processdefinitionid and processid in (select processid from process_object where (";
        if ( resultid != null)
        {
            isAddOr = true;
            sql+= " (objectid in (" + resultid +") and objecttype=" +Constants.PROCESS_OBJECT_TYPE_RESULT+") ";
        }
        if ( containerid != null)
        {
            if ( isAddOr) sql +=" or ";
            isAddOr =true;
            sql +=" (objectid in ( "+containerid +") and objecttype="+Constants.PROCESS_OBJECT_TYPE_CONTAINER+")  " ;
        }
        if ( constructid != null)
        {
            if ( isAddOr) sql +=" or ";
            isAddOr = true;
            sql +="  (objectid in ("+constructid+") and objecttype="+Constants.PROCESS_OBJECT_TYPE_CONSTRUCT+")  ";
        }
        if ( refsequenceid != null)
        {
            if ( isAddOr) sql +=" or ";
            isAddOr =true;
            sql +="  (objectid in ("+refsequenceid+") and objecttype="+Constants.PROCESS_OBJECT_TYPE_REFSEQUENCE+")  ";
        }
        if ( clonesequenceid!= null)
        {
            if ( isAddOr) sql +=" or ";
            sql +="  (objectid in ("+clonesequenceid+") and objecttype="+Constants.PROCESS_OBJECT_TYPE_CLONE_SEQUENCE+"))";
        }
        sql +=" ) order by processid desc";
        
        return sql;
        
    }
    
    
    private static String             getQuerySqlStringPerObjectType(int object_type, String item, int processed_as_object_type)
    {
        String sql = null;
        switch ( processed_as_object_type)
        {
            case Constants.PROCESS_OBJECT_TYPE_CONTAINER:
            {
                switch (object_type)
                {
                    case Constants.ITEM_TYPE_CLONEID :
                    {
                        return "(select containerid from sample where sampleid = (select sampleid from isolatetracking where isolatetrackingid =(select isolatetrackingid from flexinfo where flexcloneid="+item+")))";
                    }
                    case Constants.ITEM_TYPE_PLATE_LABELS :
                    {
                        //return "(select containerid from containerheader where label='"+item+"'))";
                         return item+")";
                    }
                }
                
            }
            case Constants.PROCESS_OBJECT_TYPE_RESULT:
            {
                switch (object_type)
                {
                    case Constants.ITEM_TYPE_CLONEID :
                    {
                        return "(select resultid from result where sampleid = (select sampleid from isolatetracking where isolatetrackingid =(select isolatetrackingid from flexinfo where flexcloneid="+item+")))";
                    }
                    case Constants.ITEM_TYPE_PLATE_LABELS :
                    {
                    }
                }
            }
            case Constants.PROCESS_OBJECT_TYPE_CONSTRUCT :
            {
                switch (object_type)
                {
                    case Constants.ITEM_TYPE_CLONEID :
                        return "(select constructid from sequencingconstruct where constructid = (select constructid from isolatetracking where isolatetrackingid =(select isolatetrackingid from flexinfo where flexcloneid="+item+")))";
                    case Constants.ITEM_TYPE_PLATE_LABELS : 
                       // return "(select min(constructid) from sequencingconstruct where constructid = (select constructid from isolatetracking where sampleid =(select min(sampleid) from sample where sampletype='ISOLATE' and containerid=( select containerid from containerheader where label='" +item+"'))))";
                        return "(select min(constructid) from sequencingconstruct where constructid = (select constructid from isolatetracking where sampleid =(select min(sampleid) from sample where sampletype='ISOLATE' and containerid=" +item+")))";
                }
            }
            case Constants.PROCESS_OBJECT_TYPE_REFSEQUENCE :
            {
                switch (object_type)
                {
                    case Constants.ITEM_TYPE_CLONEID :
                        return "(select refsequenceid from sequencingconstruct where constructid = (select constructid from isolatetracking where isolatetrackingid =(select isolatetrackingid from flexinfo where flexcloneid="+item+")))";
                    case Constants.ITEM_TYPE_PLATE_LABELS :
                }
            }
            case Constants.PROCESS_OBJECT_TYPE_CLONE_SEQUENCE:
            {
                switch (object_type)
                {
                    case Constants.ITEM_TYPE_CLONEID :
                        return "(select sequenceid from assembledsequence where isolatetrackingid =(select isolatetrackingid from flexinfo where flexcloneid="+item+"))";
                    case Constants.ITEM_TYPE_PLATE_LABELS :
                }
                
            }
        }
        return sql;
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
         // ArrayList h = ProcessHistory.getProcessHistory(Constants.ITEM_TYPE_CLONEID, "8703\n837\n650");
          ArrayList h = ProcessHistory.getProcessHistory(Constants.ITEM_TYPE_PLATE_LABELS, "67");
         
          System.out.print(h.size());
    
    }
    
}
