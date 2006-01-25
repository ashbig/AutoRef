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
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
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
    private boolean         m_IsCreateDistributionFile = false;
    
    public void             setCloneFinalStatus(int v){m_clone_final_status = v;}
    public void             setIsCreateDistributionFile(boolean v){ m_IsCreateDistributionFile = v;}
    
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
                        String distribution_file_name = Constants.getTemporaryFilesPath() + "distribution_file"+ System.currentTimeMillis();
                          
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
                                 if ( m_IsCreateDistributionFile && 
                                 (  m_clone_final_status == IsolateTrackingEngine.FINAL_STATUS_ACCEPTED ||
                                        m_clone_final_status == IsolateTrackingEngine.FINAL_STATUS_REJECTED))
                                 {
                                     writeDistributionFile( count,current_items , distribution_file_name);
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
    
    
    
  
    private void         writeDistributionFile( int dump_count, String sql_clone_ids , String distribution_file_name) throws Exception
    {
        ArrayList distribution_file_messages = new ArrayList();
        if ( dump_count == 0) 
        {
                distribution_file_messages.add( Algorithms.getXMLFileHeader());
                 distribution_file_messages.add( "<clone-data-collections>");
        }
        ArrayList clone_data = getCloneData(sql_clone_ids);
        Hashtable sequences = getCloneSequences(clone_data);
        ArrayList clone_data_in_xml_format =  transferCloneDataIntoXMLFormat(clone_data, sequences);
       distribution_file_messages.addAll(clone_data_in_xml_format);
       distribution_file_messages.add( "</clone-data-collections>");
        Algorithms. writeArrayIntoFile( distribution_file_messages,true, distribution_file_name);
    }
                               
    private ArrayList       getCloneData(String sql_clone_ids) throws Exception
    {
        ArrayList clone_data = new ArrayList ();
                ResultSet rs = null;
              CloneDescription seq_desc = null;
        String sql = " select flexcloneid as cloneid, label, position, flexsequenceid as userrefsequenceid, cs.sequenceid as clone_sequenceid "
        +" from flexinfo f, isolatetracking iso, sample s, containerheader c, assembledsequence cs "
        + " where f.isolatetrackingid=iso.isolatetrackingid and s.sampleid=iso.sampleid and s.containerid = c.containerid and cs.isolatetrackingid = iso.isolatetrackingid "
        +" and flexcloneid in ("+sql_clone_ids+") order by flexcloneid, cs.sequenceid";
        try
        {
              // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);

            while(rs.next())
            {
                seq_desc = new CloneDescription();

                seq_desc.setFlexSequenceId(rs.getInt("userrefsequenceid"));
                seq_desc.setPlateName(rs.getString("label")); 
                seq_desc.setPosition(rs.getInt("position"));
                seq_desc.setCloneSequenceId(rs.getInt("clone_sequenceid")); 
              //  seq_desc.setIsolateTrackingId( rs.getInt("isolatetrackingid"));
                 seq_desc.setCloneId(  rs.getInt("cloneid"));
                 //seq_desc.setCloneFinalStatus( rs.getInt("process_status"));
               // seq_desc.setCloningStrategyId(rs.getInt("cloningstrategyid"));
                // seq_desc.setContainerId(rs.getInt("containerid"));
               // seq_desc.setSampleId(rs.getInt("sampleid"));
               // seq_desc.setCloneStatus( rs.getInt("Status"));
                 clone_data.add( seq_desc );
                
            }
            return clone_data;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
        return clone_data;
    }
    
    
    private Hashtable        getCloneSequences(ArrayList clone_data) throws Exception
    {
        CloneSequence clone_sequence = null;
        CloneDescription clone_description = null;
        ArrayList discrepancy_descriptions = null;
        Hashtable clone_sequences = new Hashtable();
        for (int count = 0; count < clone_data.size(); count++)
        {
             clone_description = (CloneDescription) clone_data.get(count);
             clone_sequence = CloneSequence.getOneByCloneId(clone_description.getCloneId());
             discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_sequence.getDiscrepancies());
             clone_sequence.setDiscrepancies(discrepancy_descriptions);
             clone_sequences.put( new Integer(clone_description.getCloneId()), clone_sequence);
        }
        return clone_sequences;
    }
    private ArrayList       transferCloneDataIntoXMLFormat(ArrayList clone_data, Hashtable sequences)throws Exception
    {
        ArrayList clone_data_in_xml_format = new ArrayList();
        CloneDescription clone_description = null;
        for (int count = 0; count < clone_data.size(); count ++)
        {
            clone_description = (CloneDescription) clone_data.get(count);
            clone_data_in_xml_format.add( getCloneInXMLFormta(clone_description, sequences));
        }
        return  clone_data_in_xml_format ;
    }
    
    
    private String getCloneInXMLFormta(CloneDescription clone_description, Hashtable sequences) throws Exception
    {
        StringBuffer clone_data_in_xml_format = new StringBuffer();
        CloneSequence clone_sequence = (CloneSequence)sequences.get(new Integer(clone_description.getCloneId()));
        
        clone_data_in_xml_format.append(" <clone-data cloneid='" +clone_description.getCloneId());
        clone_data_in_xml_format.append("' well='"+clone_description.getPosition()+"' pale_label='" + clone_description.getPlateName());
        clone_data_in_xml_format.append("' referense_sequence_id='" +clone_description.getFlexSequenceId()+ "'>"+Constants.LINE_SEPARATOR);
 clone_data_in_xml_format.append(" <sequence_description cds_start='" + clone_sequence.getCdsStart());
  clone_data_in_xml_format.append("  cds_stop='" + clone_sequence.getCdsStop()+"' id='"+ clone_description.getCloneSequenceId() );
 clone_data_in_xml_format.append("   linker_start='"+clone_sequence.getLinker5Start() );
 clone_data_in_xml_format.append("   linker_stop='"+ clone_sequence.getLinker3Stop() +"'>"+Constants.LINE_SEPARATOR);
  
 clone_data_in_xml_format.append("  <sequence-text>"+clone_sequence.getText()+" </sequence-text> "+Constants.LINE_SEPARATOR);
  clone_data_in_xml_format.append(" <sequence-score>"+clone_sequence.getScores()+"</sequence-score>" +Constants.LINE_SEPARATOR);
//---------------------  
/* String discrepancy_summary_cds_region = 
  (String)DiscrepancyDescription.detailedDiscrepancyreport( clone_sequence.getDiscrepancies() , "type") ;
 String discrepancy_summary_linker5ds_region = 
  (String)DiscrepancyDescription.detailedDiscrepancyreport( clone_sequence.getDiscrepancies() , "type") ;
String discrepancy_summary_linker3_region = 
  (String)DiscrepancyDescription.detailedDiscrepancyreport( clone_sequence.getDiscrepancies() , "type") ;
//---------------------
            
  clone_data_in_xml_format.append(" <discrepancy_summary cds_region='"+discrepancy_summary_cds_region+"'");
clone_data_in_xml_format.append(" linker5_region='"+discrepancy_summary_linker5ds_region+"'");
clone_data_in_xml_format.append(" linker3_region='"+discrepancy_summary_linker3_region+"' />"+Constants.LINE_SEPARATOR);
*/
  
  clone_data_in_xml_format.append(" <discrepancy_collection> " +Constants.LINE_SEPARATOR);
   
clone_data_in_xml_format.append( getDiscrepancyCollectionInXMLFormta(clone_sequence.getDiscrepancies()));
 clone_data_in_xml_format.append(" </discrepancy_collection>" +Constants.LINE_SEPARATOR);
  clone_data_in_xml_format.append(" </sequence_description>" +Constants.LINE_SEPARATOR);
 clone_data_in_xml_format.append("  </clone-data>" +Constants.LINE_SEPARATOR);
        return clone_data_in_xml_format.toString();
    }
    
    
    private String getDiscrepancyCollectionInXMLFormta(ArrayList clone_discrepancies)
    {
        StringBuffer discrepancies_in_xml_format = new StringBuffer();
        DiscrepancyDescription discrepancy_description = null;
        String xml_discription = null;
        for (int count = 0; count < clone_discrepancies.size(); count++)
        {
            discrepancy_description= (DiscrepancyDescription)clone_discrepancies.get(count);
            xml_discription = discrepancy_description.writeInXMLFormat();
            discrepancies_in_xml_format.append( xml_discription);
   
    
            
        }
     
        return discrepancies_in_xml_format.toString();
    }
    //---------------------------------------------------------
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
