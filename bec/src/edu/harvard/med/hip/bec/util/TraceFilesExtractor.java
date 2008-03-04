/*
 * TraceFilesExtractor.java
 *
 * Created on March 3, 2008, 2:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.bec.util;



import java.sql.*;
import java.io.*;
import edu.harvard.med.hip.bec.action_runners.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.ui_objects.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.programs.phred.*;
import edu.harvard.med.hip.bec.file.*;
  import java.util.*;
  import edu.harvard.med.hip.utility.*;
/**
 *
 * @author htaycher
 */
public class TraceFilesExtractor  extends ProcessRunner 
{
    private String      m_dir = null;
    
    public String       getTitle()     { return "Request for trace files extraction.";     }
    public void setOutPutDirectory(String dir){ m_dir = dir;}
     
    
     public void run ()
     {   
         run_process(); 
     }
    public void run_process()
    {
      try
      {
             //convert item into array
          ArrayList clones ;
           ArrayList sql_groups_of_items =  prepareItemsListForSQL();
     
           for (int count = 0; count < sql_groups_of_items.size(); count++)
           {
               clones = getCloneInfo(m_items_type, (String) sql_groups_of_items.get(count));
               transferFiles(clones);
          }
      }
      catch(Exception e)
      {
          
      }
      }
    
    //------------------------------------------
     private ArrayList getCloneInfo(int submission_type, String sql_items) throws Exception
    {
        ArrayList clones = new ArrayList();
        String sql = null;
        UICloneSample clone = null;
        Hashtable processed_clone_ids = new Hashtable();
        Integer current_clone_id = null;
        ResultSet rs = null;
        EndReadsWrapperRunner wr = new EndReadsWrapperRunner();
        String root_dir =  wr.getOuputBaseDir()+ File.separator;
        sql = constructQueryString(submission_type, sql_items);
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                 clone = new UICloneSample();
                 clone.setCloneId (rs.getInt("CLONEID"));
                 if (clone.getCloneId() > 0 )
                 {
                    current_clone_id = new Integer(clone.getCloneId());
                    if (  processed_clone_ids.contains( current_clone_id ))
                    {
                        continue;
                    }
                 }
                 else 
                     current_clone_id = null;
                 clone.setPlateLabel (rs.getString("LABEL"));
                 clone.setPosition (rs.getInt("POSITION"));
                 clone.setSampleType (rs.getString("PSI_USER_ID"));
                 clone.setSequenceId (rs.getInt("CLONESEQUENCEID"));
                  clone.setFLEXRefSequenceId(rs.getInt("FLEXSEQUENCEID"));
                
                 if ( clone.getSequenceId () > 0)
                    clone.setCloneStatus (1);
                 else
                     clone.setCloneStatus (0);
                 
                 clone.setRefSequenceId(rs.getInt("REFSEQUENCEID"));
                 
                 if ( clone.getCloneId() > 1) 
                 { 
                     clone.setTraceFilesDirectory( root_dir + clone.getFLEXRefSequenceId() +
                        File.separator + clone.getCloneId());
                 }
                 if (  current_clone_id != null)
                 {
                     processed_clone_ids.put(current_clone_id, current_clone_id );
                 }
                 clones.add(clone);
            }
            return clones;
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot get data for clone "+e.getMessage() +"\n"+sql);
            throw new Exception();
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    
    private String constructQueryString(int submission_type, String sql_items)
    {
        String sql = null;
        if ( submission_type == Constants.ITEM_TYPE_PLATE_LABELS)//plates
        {
            
            sql=  "select FLEXSEQUENCEID,LABEL, POSITION, flexcloneid  as CLONEID, "
  +" a.SEQUENCEID as CLONESEQUENCEID, sc.refsequenceid as refsequenceid  , namevalue as PSI_USER_ID "
+" from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a , "
+" sequencingconstruct sc , name n "
 +" where  f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
 +"  and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) "
 +"=i.isolatetrackingid and n.sequenceid=sc.refsequenceid and n.nametype='PSI_USER_ID' "
+" and s.containerid in (select containerid from containerheader where Upper(label) in ("+sql_items+")) ";
            
        } 
        else if (submission_type == Constants.ITEM_TYPE_CLONEID)
        {
            sql="select FLEXSEQUENCEID,LABEL, POSITION, flexcloneid  as CLONEID, "
  +" a.SEQUENCEID as CLONESEQUENCEID, sc.refsequenceid as refsequenceid  , namevalue as PSI_USER_ID "
+" from flexinfo f,isolatetracking i, sample s, containerheader c,assembledsequence a , "
+" sequencingconstruct sc , name n "
 +" where  f.isolatetrackingid=i.isolatetrackingid and i.sampleid=s.sampleid "
 +"  and sc.constructid(+)=i.constructid and   s.containerid=c.containerid and a.isolatetrackingid(+) "
 +"=i.isolatetrackingid and n.sequenceid=sc.refsequenceid and n.nametype='PSI_USER_ID' "
+" and  flexcloneid in ("+sql_items+") ";
        }
       
       
   
        return sql;
    }
    
    
    
    private void transferFiles(ArrayList clones)throws Exception
    {
         UICloneSample clone = null;
       
        for (int count = 0; count < clones.size(); count++)
        {
            clone = (UICloneSample) clones.get(count);
            if ( clone.getTraceFilesDirectory() != null)
            {
                String dir_name = ( clone.getCloneStatus () == 1) ? "Y" :"N"; 
                 dir_name = clone.getSampleType() + "_"+ dir_name
                + "_"+  clone.getPlateLabel ()   + "_"+ clone.getPosition ();
                dir_name= m_dir + File.separator + dir_name;
                FileOperations.createDirectory(  dir_name,true);
                File trace_dir = new File(clone.getTraceFilesDirectory() + File.separator + PhredWrapper.CHROMAT_DIR_NAME);
                File[] traces = trace_dir.listFiles();
                if ( traces != null && traces.length > 0)
                {
                    for (int tr = 0; tr < traces.length; tr++)
                    {
                        File output = new File (dir_name +File.separator + traces[tr].getName());
                        FileOperations.copyFile(traces[tr] , output ,true);
                    }
                }
            }
        }
       
    }
    //--------------------------------------------
    
    /*****
    
    change in setting befor erun
            
            TRACE_FILES_OUTPUT_PATH_ROOT=\\\\Bighead\\data\\trace_files_root
            */
      public static void main(String args[]) 
     
    {
       // InputStream input = new InputStream();
        User user  = null;
        TraceFilesExtractor runner = new TraceFilesExtractor();;
        try
        {
BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
sysProps.verifyApplicationSettings();
edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();

user = AccessManager.getInstance().getUser("htaycher123","htaycher");

runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS,  "EGS003558 ");            
runner.setUser(user);
runner.setOutPutDirectory("Z:\\HTaycher\\PSI_traces");
             
runner.run();
        }
        catch(Exception e){}
     
        
       
        System.exit(0);
     }
     
}
