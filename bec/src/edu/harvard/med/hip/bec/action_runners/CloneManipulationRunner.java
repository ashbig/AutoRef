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
import edu.harvard.med.hip.bec.file.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import  edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.phred.*;
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
                                    if ( count == sql_groups_of_items.size()-1)  
                                        process_messages.append(Constants.LINE_SEPARATOR +"Distribution file name is " + distribution_file_name);
                       
                                     writeDistributionFile( count,sql_groups_of_items.size()-1,current_items , distribution_file_name);
                                 }
                                 //clean up hraddrive
                                  if (   m_clone_final_status == IsolateTrackingEngine.FINAL_STATUS_ACCEPTED ||
                                        m_clone_final_status == IsolateTrackingEngine.FINAL_STATUS_REJECTED)
                                 {
                                   
                                     deleteIntermidiateFilesFromHardDrice(current_items );
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
    //-------------------------------------------------------------------------------------
    public void            deleteIntermidiateFilesFromHardDrice(String current_clone_ids)throws Exception
    {
        ArrayList clone_directories = getCloneDirectories(current_clone_ids);
        deleteIntermidiateFilesFromHardDrice( clone_directories );
    }
  
  private  ArrayList    getCloneDirectories(String current_clone_ids)throws Exception
  {
      ArrayList clone_directories = new ArrayList ();
      ResultSet rs = null;
      CloneDescription clone_desc = null;
                 String sql = " select   flexcloneid as cloneid,  flexsequenceid as userrefsequenceid  from flexinfo f, isolatetracking iso, sequencingconstruct  sc "
        + " where sc.constructid = iso.constructid and f.isolatetrackingid=iso.isolatetrackingid and flexcloneid in ("+current_clone_ids+") ";
    
        try
        {
              // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);

            while(rs.next())
            {
                clone_desc = new CloneDescription();
                 clone_desc.setFlexSequenceId(rs.getInt("userrefsequenceid"));
                  clone_desc.setCloneId(  rs.getInt("cloneid"));
                clone_directories.add( clone_desc );
            }
            return clone_directories;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting clone information /SQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
        
      
  }
    
  private void          deleteIntermidiateFilesFromHardDrice(ArrayList clone_directories )throws Exception
  {
      EndReadsWrapperRunner er = new EndReadsWrapperRunner();
      String trace_files_root = er.getOuputBaseDir();
      CloneDescription clone_desc = null;
     
      String trace_files_clone_directory = null;
      for (int count = 0; count < clone_directories.size(); count++)
      {
          clone_desc= (CloneDescription)clone_directories.get(count);
          trace_files_clone_directory = trace_files_root+File.pathSeparator + clone_desc.getFlexSequenceId() + File.pathSeparator + clone_desc.getCloneId();
          cleanUpDirectory(trace_files_clone_directory);
      }
  }
  
  private void          cleanUpDirectory(String trace_files_clone_directory)throws Exception
  {
      //clean up all phred files
        FileOperations.deleteAllFilesFormDirectory(trace_files_clone_directory + File.separator +PhredWrapper.QUALITY_DIR_NAME);
        FileOperations.deleteAllFilesFormDirectory(trace_files_clone_directory + File.separator +PhredWrapper.CONTIG_DIR_NAME);
 FileOperations.deleteAllFilesFormDirectory(trace_files_clone_directory + File.separator +PhredWrapper.SEQUENCE_DIR_NAME);

        //delete all .phd files from previous processing
        FileOperations.deleteAllFilesFormDirectory(trace_files_clone_directory + File.separator +PhredWrapper.PHD_DIR_NAME);
         
  }
    //--------------------------------------------------------------------------------
    private void         writeDistributionFile( int dump_count, int number_of_cycles, 
                String sql_clone_ids , String distribution_file_name) throws Exception
    {
        ArrayList distribution_file_messages = new ArrayList();
         FileWriter fr =  new FileWriter(distribution_file_name, true);
         
         //write header
        if ( dump_count == 0) 
        {
                fr.write(  Algorithms.getXMLFileHeader() + Constants.LINE_SEPARATOR);
                fr.write(  "<clone-data-collections>");
                fr.flush();
        }
        ArrayList clone_data = getCloneData(sql_clone_ids);
        Hashtable cloning_strategies = getCloningStrategies(clone_data);
        writeClones(clone_data, cloning_strategies, fr);
        
        //write footer
        if ( dump_count == number_of_cycles )fr.write( "</clone-data-collections>");
        fr.close();
    }
      
    private Hashtable       getCloningStrategies(ArrayList clone_data)throws Exception
    {
        Hashtable cloning_strategies = new Hashtable();
        CloneDescription clone_desc = null;
        CloningStrategy cloning_strategy= null;
        for (int count = 0; count < clone_data.size(); count++)
        {
            clone_desc = (CloneDescription) clone_data.get(count);
            if ( !cloning_strategies.containsKey(new Integer(clone_desc.getCloningStrategyId())))

            {
                try
                {
                    cloning_strategy =  CloningStrategy.getById(clone_desc.getCloningStrategyId());
                    cloning_strategy.setLinker3(BioLinker.getLinkerById(cloning_strategy.getLinker3Id()));
                    cloning_strategy.setLinker5(BioLinker.getLinkerById(cloning_strategy.getLinker5Id()));
                    
                    cloning_strategies.put(new Integer(clone_desc.getCloningStrategyId()),cloning_strategy);
                }
                catch(Exception e)
                {
                    m_error_messages.add("Cannot get cloning strategy information\n"+e.getMessage());
                    throw new Exception();
                }
            }

        }
        return cloning_strategies;
    }
    private ArrayList       getCloneData(String sql_clone_ids) throws Exception
    {
        ArrayList clone_data = new ArrayList ();
                ResultSet rs = null;
              CloneDescription seq_desc = null;
       /* String sql = " select process_status, flexcloneid as cloneid, label, position, flexsequenceid as userrefsequenceid, cs.sequenceid as clone_sequenceid "
        +" from flexinfo f, isolatetracking iso, sample s, containerheader c, assembledsequence cs "
        + " where f.isolatetrackingid=iso.isolatetrackingid and s.sampleid=iso.sampleid and s.containerid = c.containerid and cs.isolatetrackingid = iso.isolatetrackingid "
        +" and flexcloneid in ("+sql_clone_ids+") order by flexcloneid, cs.sequenceid";
        **/
              String sql = " select cloningstrategyid, process_status, flexcloneid as cloneid, label, position, flexsequenceid as userrefsequenceid, cs.sequenceid as clone_sequenceid, "
        +" cs.refsequenceid as refsequenceid from flexinfo f, isolatetracking iso, sample s, containerheader c, assembledsequence cs, sequencingconstruct  sc "
        + " where sc.constructid = iso.constructid and f.isolatetrackingid=iso.isolatetrackingid and s.sampleid=iso.sampleid and s.containerid = c.containerid and cs.isolatetrackingid = iso.isolatetrackingid "
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
                seq_desc.setBecRefSequenceId( rs.getInt("refsequenceid"));
              //  seq_desc.setIsolateTrackingId( rs.getInt("isolatetrackingid"));
                 seq_desc.setCloneId(  rs.getInt("cloneid"));
                 seq_desc.setCloneFinalStatus( rs.getInt("process_status"));
                 seq_desc.setCloningStrategyId(rs.getInt("cloningstrategyid"));
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
        
    }
    
    private void writeClones(ArrayList clone_data, Hashtable cloning_strategies, 
                FileWriter fr) throws Exception
    {
        CloneDescription clone_description = null;
        CloningStrategy cloning_strategy = null;
        for (int count = 0; count < clone_data.size(); count ++)
        {
            clone_description = (CloneDescription) clone_data.get(count);
            cloning_strategy = (CloningStrategy) cloning_strategies.get(new Integer(clone_description.getCloningStrategyId()));
            fr.write( getCloneInXMLFormat(clone_description, cloning_strategy));
            
            fr.flush();
            
        }
    }
        
    private CloneSequence        getCloneSequence(int clone_id) throws Exception
    {
        CloneSequence clone_sequence = null;
        CloneDescription clone_description = null;
        ArrayList discrepancy_descriptions = null;
        
          clone_sequence = CloneSequence.getOneByCloneId(clone_id);
         discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions( clone_sequence.getDiscrepancies());
         clone_sequence.setDiscrepancies(discrepancy_descriptions);
         return clone_sequence;
        
    }
    
    
    private String getCloneInXMLFormat(CloneDescription clone_description, CloningStrategy cloning_strategy) throws Exception
    {
        StringBuffer clone_data_in_xml_format = new StringBuffer();
        CloneSequence clone_sequence =  getCloneSequence( clone_description.getCloneId());
        RefSequence ref_sequence = new RefSequence(clone_description.getBecRefSequenceId(), false);
clone_data_in_xml_format.append(" <clone-data cloneid='" +clone_description.getCloneId());
clone_data_in_xml_format.append("' well='"+clone_description.getPosition()+"' pale_label='" + clone_description.getPlateName());
clone_data_in_xml_format.append("' referense_sequence_id='" +clone_description.getFlexSequenceId()+ "' clone-status='"+clone_description.getCloneFinalStatus()+"'>"+Constants.LINE_SEPARATOR);

clone_data_in_xml_format.append(" <reference_data>"+Constants.LINE_SEPARATOR);
clone_data_in_xml_format.append("<target_sequence>" + ref_sequence.getCodingSequence()+Constants.LINE_SEPARATOR);
clone_data_in_xml_format.append("</target_sequence>"+Constants.LINE_SEPARATOR);
clone_data_in_xml_format.append("<linker_5 description='' sequence='"+cloning_strategy.getLinker5().getSequence()+"'/>"+Constants.LINE_SEPARATOR);
clone_data_in_xml_format.append("<linker_3 description='' sequence='"+cloning_strategy.getLinker3().getSequence()+"'/>"+Constants.LINE_SEPARATOR);
clone_data_in_xml_format.append("</reference_data>"+Constants.LINE_SEPARATOR);
        
clone_data_in_xml_format.append(" <sequence_description cds_start='" + clone_sequence.getCdsStart());
clone_data_in_xml_format.append("'  cds_stop='" + clone_sequence.getCdsStop()+"' id='"+ clone_description.getCloneSequenceId() );
clone_data_in_xml_format.append("'   linker_start='"+clone_sequence.getLinker5Start() );
clone_data_in_xml_format.append("'   linker_stop='"+ clone_sequence.getLinker3Stop() +"'>"+Constants.LINE_SEPARATOR);

clone_data_in_xml_format.append("  <sequence-text>"+clone_sequence.getText()+" </sequence-text> "+Constants.LINE_SEPARATOR);
clone_data_in_xml_format.append(" <sequence-score>"+clone_sequence.getScores()+"</sequence-score>" +Constants.LINE_SEPARATOR);

clone_data_in_xml_format.append(" <discrepancy_collection> " +Constants.LINE_SEPARATOR);
   

clone_data_in_xml_format.append( getDiscrepancyCollectionInXMLFormat(clone_sequence.getDiscrepancies())+Constants.LINE_SEPARATOR);
clone_data_in_xml_format.append(" </discrepancy_collection>" +Constants.LINE_SEPARATOR );
clone_data_in_xml_format.append(" </sequence_description>" +Constants.LINE_SEPARATOR );
clone_data_in_xml_format.append("  </clone-data>" +Constants.LINE_SEPARATOR );

return clone_data_in_xml_format.toString() ;


    }
    
    
    private String getDiscrepancyCollectionInXMLFormat(ArrayList clone_discrepancies)
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
            runner.setInputData(Constants.ITEM_TYPE_CLONEID, "158784  158499 158507 158515 158523 158579 ");
            runner.setProcessType(Constants.PROCESS_SET_CLONE_FINAL_STATUS);
            runner.setCloneFinalStatus(IsolateTrackingEngine.FINAL_STATUS_REJECTED);
            runner.setIsCreateDistributionFile(true);
            runner.run();
      
        }
        catch(Exception e){}
        System.exit(0);
    }
    
    
}
