/*
 * PrimerDesignerRunner.java
 *
 * Created on October 27, 2003, 5:11 PM
 */

package edu.harvard.med.hip.bec.action_runners;


import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import javax.sql.*;
import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  HTaycher
 */
public class DeleteObjectRunner extends ProcessRunner 
{
    public static final int         ACTION_DELETE_PLATE = 0;//
    public static final int         ACTION_DELETE_CLONE_READS = 1;//
    public static final int         ACTION_DELETE_CLONE_FORWARD_READ = 2;//
    public static final int         ACTION_DELETE_CLONE_REVERSE_READ = 3;//
    public static final int         ACTION_DELETE_CLONE_SEQUENCE = 4;//
    public static final int         ACTION_UPDATE_REFERENCE_SEQUENCE = 5;
    
    
    private int                     m_action_type = -1;
     public String       getTitle()     {  return "Request for objects deletion execution.";     }
    public   void        setActionType(int v){ m_action_type = v;}  
    
    public void run()
    {
          Connection conn = null;
          String sql = "";
         FileWriter reportFileWriter = null;
        try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            String  report_file_name = Constants.getTemporaryFilesPath() + "DeleteReport"+System.currentTimeMillis()+".txt";
             ArrayList sql_groups_of_items =  prepareItemsListForSQL();
               for (int count = 0; count < sql_groups_of_items.size(); count++)
               {
                   try
                   {
                        deleteItems(conn, (String)sql_groups_of_items.get(count) , report_file_name);
                   } catch(Exception e)
                    {
                        DatabaseTransaction.rollback(conn);
                        m_error_messages.add(e.getMessage());
                    }
               }
             
            m_file_list_reports.add(new File(report_file_name));   
          
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            if(conn != null)            DatabaseTransaction.closeConnection(conn);
            if ( reportFileWriter != null )
            {
                try {reportFileWriter.close();}catch(Exception e){ try { reportFileWriter.close();}catch(Exception n){} }
            }
        
            sendEMails( getTitle());
        }

            //request->process->process_config|| process_object(refsequence)
    }
    
    //----------------------------------------------------
    private void deleteItems(Connection conn, String sql_items, String report_file_name) throws Exception
    {
        ArrayList sql_for_deletion = new ArrayList();
        String sql = null;
        switch (m_action_type)
        {
            case ACTION_DELETE_PLATE :
            {
            }
            case ACTION_DELETE_CLONE_READS :
            case ACTION_DELETE_CLONE_FORWARD_READ :
            case ACTION_DELETE_CLONE_REVERSE_READ :
            {
                sql_for_deletion = getSqlReads(sql_items);
                break;
            }
            case  ACTION_DELETE_CLONE_SEQUENCE :
            {
                //delete all assembled sequences and their discrrepancies for the clone
                sql_for_deletion = getSqlDeleteCloneSequence(sql_items);
                break;
            }
            case  ACTION_UPDATE_REFERENCE_SEQUENCE : break;
  
            default: m_error_messages.add("Action not set.");
        }
        //execute statements
        for ( int count = 0; count < sql_for_deletion.size(); count++)
        {
            DatabaseTransaction.executeUpdate( (String)sql_for_deletion.get(count) , conn);
        }
        printReport(sql_for_deletion,   report_file_name);
    }
    
    //------------------------------------------------------------------
     public ArrayList      getSqlReads(String items_for_sql)throws BecDatabaseException
    {
        //delete all assembled sequences and their discrrepancies for the clone
        ArrayList sql_for_deletion= new ArrayList();
        String sql_where = null;String sql = null;
        String sql_read_type = null;String sql_result_type = null;
         switch (m_action_type)
        {
            case ACTION_DELETE_CLONE_READS :{break;}
            case ACTION_DELETE_CLONE_FORWARD_READ :
            {
                sql_read_type = "  readtype in ("+ Read.TYPE_ENDREAD_FORWARD_SHORT+","+
    Read.TYPE_ENDREAD_FORWARD_NO_MATCH +","+ Read.TYPE_ENDREAD_FORWARD_FAIL +","+
    Read.TYPE_ENDREAD_FORWARD  +") and ";
   
                sql_result_type = " resulttype in  ( "+ Result.RESULT_TYPE_ENDREAD_FORWARD+","+
                    Read.TYPE_ENDREAD_FORWARD+","+ Read.TYPE_ENDREAD_FORWARD_FAIL +") and";
            }
            case ACTION_DELETE_CLONE_REVERSE_READ :
            {
                 sql_result_type = " resulttype in ( "+  Read.TYPE_ENDREAD_REVERSE_FAIL+","+ Read.TYPE_ENDREAD_REVERSE +","+
                    Result.RESULT_TYPE_ENDREAD_REVERSE  +") and ";
                 sql_read_type = "  readtype in ("+ Read.TYPE_ENDREAD_REVERSE +","+ 
                    Read.TYPE_ENDREAD_REVERSE_FAIL+","+  Read.TYPE_ENDREAD_REVERSE_NO_MATCH +","+ 
                    Read.TYPE_ENDREAD_REVERSE_SHORT +") and";
                 
            }
         }
        if ( m_items_type == Constants.ITEM_TYPE_CLONEID)
        {
            sql_where ="( select isolatetrackingid from flexinfo where flexcloneid in ("+items_for_sql+"))";
        }
        else if ( m_items_type == Constants.ITEM_TYPE_PLATE_LABELS)
        {
            sql_where ="( select isolatetrackingid from isolatetracking where sampleid in "
            +"(select sampleid from sample where containerid in (select containerid from containerheader "
            +" where label in ("+items_for_sql+"))))";
        }
        //discrepancy          
        sql = "delete from discrepancy where sequenceid in "
        +" ( select readsequenceid from readinfo where ";
        if ( sql_read_type != null) sql+= sql_read_type;
        sql+=" isolatetrackingid in " + sql_where+")";      sql_for_deletion.add(sql);
  //sequence info
        sql = "delete from sequenceinfo where sequenceid in "
        +" ( select readsequenceid from readinfo where ";
          if ( sql_read_type != null) sql+= sql_read_type;
        sql += "isolatetrackingid in " + sql_where+")";       sql_for_deletion.add(sql);
//readinfo 
        sql = "delete from  readinfo  where ";
        if ( sql_read_type != null) sql+= sql_read_type;
        sql +=" isolatetrackingid in"+ sql_where;   sql_for_deletion.add(sql);
//get filereferens id 
    sql = "select filereferenceid from  resultfilereference  where ";
    if ( sql_result_type != null ) sql+= sql_result_type;
    sql += "resultid in  (select resultid from result where sampleid in "
+" (select sampleid from isolatetracking where isolatetrackingid in "+sql_where+"))"; 
    String fileref_ids = getFileReferenceIds(sql);            
//delete from resultfile reference
    sql = "delete from   resultfilereference  where ";
    if ( sql_result_type != null ) sql+= sql_result_type;
    sql +=" resultid in  (select resultid from result where sampleid in "
    +" (select sampleid from isolatetracking where isolatetrackingid in "
    +sql_where+"))";      sql_for_deletion.add(sql);

//filereference
sql = "delete from  filereference where filereferenceid in ("+fileref_ids+")";
sql_for_deletion.add(sql);
        //update reads
sql = "update  result set resultvalueid = null, resulttype = "+Result.RESULT_TYPE_ENDREAD_FORWARD+" where resulttype in ("
+ Read.TYPE_ENDREAD_FORWARD+","+ Read.TYPE_ENDREAD_FORWARD_FAIL+") and sampleid in (select sampleid from isolatetracking where isolatetrackingid in "
+ sql_where+")";   sql_for_deletion.add(sql);      
sql = "update  result set resultvalueid = null, resulttype = "+Result.RESULT_TYPE_ENDREAD_REVERSE+" where resulttype in ("
+Read.TYPE_ENDREAD_REVERSE+","+ Read.TYPE_ENDREAD_REVERSE_FAIL+") and sampleid in (select sampleid from isolatetracking where isolatetrackingid in "
+ sql_where+")";   sql_for_deletion.add(sql);                      
     
        return sql_for_deletion;
     }
      
     public String             getFileReferenceIds(String sql) throws BecDatabaseException
     {
        RowSet crs = null; boolean isFirst = true;
        StringBuffer result = new StringBuffer();
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
               if ( !isFirst) 
                   result.append(  ",");
               if ( isFirst ) isFirst = false;
               result.append( crs.getInt("FILEREFERENCEID"));
            }
            return result.toString();
        } 
        catch (Exception e)
        {
            throw new BecDatabaseException("Error occured while extracting filerefeerneceids: "+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
     }
    public ArrayList      getSqlDeleteCloneSequence(String items_for_sql)
    {
        //delete all assembled sequences and their discrrepancies for the clone
        ArrayList sql_for_deletion= new ArrayList();
        String sql = null;
        if ( m_items_type == Constants.ITEM_TYPE_CLONEID)
        {
            sql = "delete from discrepancy where sequenceid in "
            +" ( select sequenceid from assembledsequence where isolatetrackingid in"
            +"( select isolatetrackingid from flexinfo where flexcloneid in "
            +" ("+items_for_sql+")))";                sql_for_deletion.add(sql);
             sql = "delete from sequenceinfo where sequenceid in "
            +" ( select sequenceid from assembledsequence where isolatetrackingid in"
            +"( select isolatetrackingid from flexinfo where flexcloneid in "
            +" ("+items_for_sql+")))";                sql_for_deletion.add(sql);
             sql = "delete from  assembledsequence where isolatetrackingid in"
            +"( select isolatetrackingid from flexinfo where flexcloneid in "
            +" ("+items_for_sql+"))";                sql_for_deletion.add(sql);
        }
        else if ( m_items_type == Constants.ITEM_TYPE_BECSEQUENCE_ID)
        {
            sql = "delete from discrepancy where sequenceid in  ("+items_for_sql+")";   sql_for_deletion.add(sql);
             sql = "delete from sequenceinfo where sequenceid in ("+items_for_sql+")"; sql_for_deletion.add(sql);
             sql = "delete from  assembledsequence where sequenceid in ("+items_for_sql+")"; sql_for_deletion.add(sql);
        }
        return sql_for_deletion;
    }
    
    private void            printReport(ArrayList sql_statements,  String report_file_name)
    {
        FileWriter in = null; 
        try
        {
            in =  new FileWriter(report_file_name, true);
            for (int count =0; count < sql_statements.size(); count++)
            {
                  in.write( (String) sql_statements.get(count));
            }
            in.flush();
            in.close();
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot create report file.");
        }
   }
    
    ///////////////////////////////////////////////////////
     public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        DeleteObjectRunner input = null;
        User user  = null;
        try
        {// 3558           775       776       884       638      6947 
            input = new DeleteObjectRunner();
            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
            input.setItems("    734 345 ");
            input.setItemsType( Constants.ITEM_TYPE_CLONEID);
            input.setUser(user);
             ArrayList sql_groups_of_items =  input.prepareItemsListForSQL();
            
           
            input.setActionType(DeleteObjectRunner.ACTION_DELETE_CLONE_READS);
         //   input.setItems("    'BSE000934' ");
            //input.setItemsType( Constants.ITEM_TYPE_PLATE_LABELS);
            
            ArrayList sql = input.getSqlReads((String)sql_groups_of_items.get(0));
            for (int i = 0; i < sql.size(); i ++)
            {
                System.out.println((String) sql.get(i));
            }
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     }
    
     
}
