/*
 * CloneManipulationRunner.java
 *
 * Created on October 11, 2005, 2:26 PM
 */

package edu.harvard.med.hip.bec.action_runners;

import java.util.*;
import java.io.*;
import sun.jdbc.rowset.*;
import java.sql.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import javax.naming.*;
import javax.sql.*;

import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import  edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
/**
 *
 * @author  htaycher
 */
public class CloneManipulationRunner extends ProcessRunner
{
    
    /** Creates a new instance of CloneManipulationRunner */
    private int              m_clone_final_status = IsolateTrackingEngine.FINAL_STATUS_INPROCESS;
    
    
    public void             setCloneFinalStatus(int v){m_clone_final_status = v;}
    
    public String getTitle()
    {
        switch ( m_process_type)
        {
            case Constants.PROCESS_SET_CLONE_FINAL_STATUS: return "Set final clone status";
            default: return "";
            
        }
    }
    
    
    
    
    public void run_process()
    {
         Connection  conn =null;
         String sql = null;
         String current_items = null;
         ArrayList clones = new ArrayList();
         StringBuffer process_messages = new StringBuffer();
           ArrayList  sql_groups_of_items = new ArrayList();
       try
         {
               conn = DatabaseTransaction.getInstance().requestConnection();
               switch (m_process_type)
                {
                    case Constants.PROCESS_SET_CLONE_FINAL_STATUS  :
                    {
                        sql = "update isolatetracking set process_status = "+m_clone_final_status
                        +" where process_status != "+IsolateTrackingEngine.FINAL_STATUS_NOT_APPLICABLE
                        +" and isolatetrackingid in (select isolatetrackingid from flexinfo where flexcloneid in (";
                     
                        sql_groups_of_items =  prepareItemsListForSQL();
                        if (sql_groups_of_items == null || sql_groups_of_items.size() < 1) return;
                        process_messages.append(Constants.LINE_SEPARATOR+"New clones final status "+IsolateTrackingEngine.getCloneFinalStatusAsString(m_clone_final_status)+Constants.LINE_SEPARATOR);
                            
                        int process_id = Request.createProcessHistory( conn, ProcessDefinition.RUN_UPDATE_FINAL_CLONE_STATUS, new ArrayList(),m_user) ;
          
                        PreparedStatement pst_insert_process_object = conn.prepareStatement("insert into process_object (processid,objectid,objecttype) values("+process_id+",?,"+Constants.PROCESS_OBJECT_TYPE_CLONEID+")");
       
                        for ( int count = 0; count < sql_groups_of_items.size(); count++)
                        {
                            try
                            {
                                current_items=(String)sql_groups_of_items.get(count);
                                 DatabaseTransaction.executeUpdate(sql+current_items+"))", conn);
                                 process_messages.append("The following clones have been updated "+Algorithms.replaceChar(current_items, ',', ' '));
                                 
                                 clones = Algorithms.splitString(current_items, ",");
                                 for ( int count_clones = 0; count_clones < clones.size(); count_clones++ )
                                 {
                                     pst_insert_process_object.setInt(1,Integer.parseInt((String)clones.get(count_clones)));
                                     DatabaseTransaction.executeUpdate(pst_insert_process_object);
                                 }
                                 conn.commit();
                            }
                            catch(Exception e)
                            {
                                process_messages.append("The following clones failed update "+Algorithms.replaceChar(current_items, ',', ' '));
                                 
                            }
                        }
    
                        
                        break;
                    }
                  
                 }
              m_additional_info= process_messages.toString();
         } 
        catch(Exception e)  
        {
            m_error_messages.add(e.getMessage());
        }
        finally
        {
            sendEMails( getTitle() );
            DatabaseTransaction.closeConnection(conn);
        }
        
        
        
    }
    
    
    
     public static void main(String[] args) {
        // TODO code application logic here
        User user  = null;
        try
        {

            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
            CloneManipulationRunner runner = new CloneManipulationRunner();
            runner.setUser(user);
            runner.setInputData(Constants.ITEM_TYPE_CLONEID, "158499 158507 158515 158523 158579 ");
            runner.setProcessType(Constants.PROCESS_SET_CLONE_FINAL_STATUS);
            runner.setCloneFinalStatus(IsolateTrackingEngine.FINAL_STATUS_REJECTED);
            runner.run();
      
        }
        catch(Exception e){}
        System.exit(0);
    }
    
    
}
