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
import edu.harvard.med.hip.bec.user.*;
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
                 
               ArrayList sql_groups_of_items =  prepareItemsListForSQL();
               ArrayList plates = null;String sql_history = null;
               OligoContainer oligo_container = null;
                String sql_plate_update = null;
               if ( m_plate_status == OligoContainer.STATUS_RECIEVED)
                {
                    process_description = ProcessDefinition.RUN_OLIGO_ORDER_RECIEVED;
                }
                else if (m_plate_status == OligoContainer.STATUS_SENT_FOR_SEQUENCING)
                {
                    process_description = ProcessDefinition.RUN_OLIGO_PLATE_USED_FOR_SEQUENCING;
                }
                int process_id = Request.createProcessHistory( conn, process_description, new ArrayList(),m_user) ;
                DatabaseTransaction.commit(conn);       
               for (int count = 0; count < sql_groups_of_items.size(); count++)
               {
                   plates = getOligoPlates( (String)sql_groups_of_items.get(count));
                   for (int plate_count = 0; plate_count < plates.size(); plate_count++)
                   {
                       oligo_container = (OligoContainer)plates.get(plate_count);
                       try
                       {
                           plate_name = oligo_container.getLabel();
                       
                           sql_plate_update = " update oligocontainer set status = " + m_plate_status ;
                             if(  m_order_comment != null && m_order_comment.trim().length() > 0)
                                 sql_plate_update += ", descr_oligo_processing = '" + oligo_container.getCommentOrder() +" "+ m_order_comment+ "'";
                             if (  m_sequencing_comment != null && m_sequencing_comment.trim().length() > 0)
                                 sql_plate_update += ", descr_sequencing ='" + oligo_container.getCommentSequencing() +" "+ m_sequencing_comment+ "'";
                             DatabaseTransaction.executeUpdate(sql_plate_update, conn);

                           sql_history = "insert into process_object (processid,objectid,objecttype)";
                            sql_history+=" values("+process_id+", (select oligocontainerid from oligocontainer where label = '"+plate_name+"'),"+Constants.PROCESS_OBJECT_TYPE_CONTAINER+")";

                            DatabaseTransaction.executeUpdate( sql_history,conn);
                            DatabaseTransaction.commit(conn);
                       }
                        catch(Exception e1)
                       {m_error_messages.add("Cannot process oligo plate " + plate_name +"\n"+e1.getMessage());}
                    }
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
     
     
    private ArrayList getOligoPlates(String sql_items)
    {
        String sql = " select oligocontainerid, label, status,descr_oligo_processing,descr_sequencing from oligocontainer "
        +" where label in ("+sql_items+")";
        OligoContainer container = null;
        ArrayList containers = new ArrayList();
        ResultSet rs = null; String ordcom = null; String seqcom = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                ordcom = rs.getString("descr_oligo_processing");
                ordcom = (ordcom == null) ? "": ordcom;
                seqcom = rs.getString("descr_sequencing");
                seqcom = (seqcom == null) ?"":seqcom;
                containers.add( new OligoContainer(rs.getString("label"), 
                        rs.getInt("status"), m_user.getId(), 
                        ordcom, seqcom));
            }
            return containers;
        }
        catch (Exception e)
        {
            
            m_error_messages.add("Cannot get data for plates "+ sql_items +e.getMessage());
            return containers;
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
 
    public static void main(String[] args)
    {
         OligoPlateProcessor_Runner input = null;
        User user  = null;
        try
        {
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
      
             
            user = AccessManager.getInstance().getUser("htaycher123","htaycher");
            input = new OligoPlateProcessor_Runner();
            input.setOrderComment("ads");
            input.setUser(user);
            input.setPlateStatus( OligoContainer.STATUS_RECIEVED );
             input.setInputData(Constants.ITEM_TYPE_PLATE_LABELS,  "OPLATE000369 OPLATE000370 ");
             input.run();
        }catch(Exception e)
        {
            
        }
        
    }
    
    
    
}
