/*
 * OligoPlateProcessor_Runner.java
 *
 * Created on February 4, 2005, 2:44 PM
 */

package edu.harvard.med.hip.bec.action_runners;

import java.util.*;
import java.io.*;
import sun.jdbc.rowset.*;
import java.sql.*;
import org.apache.regexp.*;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.*;
/**
 *
 * @author  htaycher
 */
public class OligoPlateProcessor_Runner extends ProcessRunner
{
    
    private String              m_order_comment = null;
    private String              m_sequencing_comment = null;
    private int                 m_plate_status = -1;
    
    
    public void                 setOrderComment(String v){ m_order_comment = v;}
    public void                 setSequencingComment(String v){   m_sequencing_comment = v;}
    public void                 setPlateStatus(int v){m_plate_status =v;}
   
    public String getTitle() {return "Request for oligo plates status change. Plates: " + m_items;    }
   
     public void run()
    {
       Connection  conn =null; String plate_name = null;
       String process_description = null;
       try
         {
                conn = DatabaseTransaction.getInstance().requestConnection();
                 
                ArrayList plates = Algorithms.splitString(m_items);
               for (int count = 0; count < plates.size(); count++)
               {
                   try
                   {
                        plate_name = (String) plates.get(count);

                        if ( m_plate_status == OligoContainer.STATUS_RECIEVED)
                        {
                            process_description = ProcessDefinition.RUN_OLIGO_ORDER_RECIEVED;
                        }
                        else if (m_plate_status == OligoContainer.STATUS_SENT_FOR_SEQUENCING)
                        {
                            process_description = ProcessDefinition.RUN_OLIGO_PLATE_USED_FOR_SEQUENCING;
                        }

                        int process_id = Request.createProcessHistory( conn, process_description, new ArrayList(),m_user) ;
                        String sql = "insert into process_object (processid,objectid,objecttype)";
                            sql=" values("+process_id+", (select oligocontainerid from oligocontainer where label = '"+plate_name+"'),"+Constants.PROCESS_OBJECT_TYPE_CONTAINER+")";

                        DatabaseTransaction.executeUpdate( sql,conn);
                        DatabaseTransaction.commit(conn);
                   }
                   catch(Exception e1)
                   {m_error_messages.add("Cannot process oligo plate " + plate_name +"\n"+e1.getMessage());}
                }
          } 
        catch(Exception e)  
       {m_error_messages.add(e.getMessage());}
        finally
            {
                sendEMails( getTitle() );
                DatabaseTransaction.closeConnection(conn);
            }
    }
     
     
     private void       executePlateUpdate(Connection conn, String plate_label) throws Exception
     {
         String sql = " update oligocontainer set status = " + m_plate_status ;
         if(  m_order_comment != null && m_order_comment.trim().length() > 0)
             sql += ", descr_oligo_processing = " + m_order_comment;
         if (  m_sequencing_comment != null && m_sequencing_comment.trim().length() > 0)
             sql += ", descr_sequencing =" + m_sequencing_comment;
         DatabaseTransaction.executeUpdate(sql, conn);
     }
 
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    
    
}
