/*
 * tester_run_assembler.java
 *
 * Created on July 8, 2003, 2:03 PM
 */

package edu.harvard.med.hip.bec.action;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
  import java.util.*;
/**
 *
 * @author  htaycher
 */
public class tester_run_assembler
{
    
    /** Creates a new instance of tester */
    public tester_run_assembler()
    {
         i_error_messages = new ArrayList();
    }
    
         // outputBaseDir specify the base directory for trace file distribution
        private static final String INPUT_BASE_DR = "";
        private static final int MAX_NUMBER_OF_ROWS_TO_RETURN = 200;
    
        private ArrayList   i_master_container_ids = null;//get from form
        private String      i_phrap_params_file = null;
        private String      i_traceFilesBaseDir = null;
        private ArrayList   i_error_messages = null;
        private String      i_result_type = null;
        private User        i_user = null;
         private String      i_vector_file_name = null;
        
        
        public  void        setUser(User v){i_user=v;}
        public void         setResultType(String v){ i_result_type=v;}
        public void         setVectorFileName(String v){i_vector_file_name = v;}
        
        public void run()
        {
      // The database connection used for the transaction
            Connection conn = null;
            ArrayList process_clones = new ArrayList();
            ArrayList sequences = new ArrayList();
            i_error_messages = new ArrayList();
            int min_result_id = 1;
            boolean isLastRecord = false;
            RefSequence refsequence = null;BaseSequence base_refsequence = null;
            BioLinker   linker5 = null; int cds_start = 0; int cds_stop = 0;
            BioLinker   linker3 = null;
            int cur_containerid = -1;
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
      
          //process only sequences  that are exspected
                while (isLastRecord)
                {
                    ArrayList expected_sequence_definition = getExspectedSequenceDescriptions(conn, i_result_type, min_result_id);

                    if (expected_sequence_definition.size() == 0 )      return ;
                    CloneAssembly clone_assembly = null;
                    SequenceDescription sequence_definition = null;
                    int clone_sequence_id = -1;
                    for (int count = 0; count < expected_sequence_definition.size(); count ++)
                    {
                        sequence_definition= ( SequenceDescription)expected_sequence_definition.get(count);
                        //get linker info
                        try
                        {
                            if ((linker5 == null || linker3 == null) || sequence_definition.getContainerId() != cur_containerid )
                            {
                                CloningStrategy container_cloning_strategy = Container.getCloningStrategy(sequence_definition.getContainerId());
                                if (container_cloning_strategy != null)
                                 {
                                     linker3 = BioLinker.getLinkerById( container_cloning_strategy.getLinker3Id() );
                                     linker5 = BioLinker.getLinkerById( container_cloning_strategy.getLinker5Id() );
                                 }
                            }
                        
                        //get refsequence if needed
                            if (base_refsequence == null || base_refsequence.getId() != sequence_definition.getBecRefSequenceId())
                            {
                                refsequence = new RefSequence( sequence_definition.getBecRefSequenceId());
                                base_refsequence =  new BaseSequence(refsequence.getCodingSequence(), BaseSequence.BASE_SEQUENCE );
                                if (base_refsequence.getText().length() >1500) continue;
                                cds_start = linker5.getSequence().length();
                                cds_stop = linker5.getSequence().length() +  base_refsequence.getText().length() -2;
                                base_refsequence.setText( linker5.getSequence() + base_refsequence.getText()+linker3.getSequence());
                            }
                            
                           clone_sequence_id = -1;
                           clone_assembly = assembleSequence( sequence_definition );
                           if (clone_assembly == null )
                           {
                               clone_assembly.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_NO_CONTIGS);
                           }
                           else if( clone_assembly.getContigs().size() != 1)
                           {
                               clone_assembly.setStatus(IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_N_CONTIGS);
                           }
                           else
                           {
                               //check coverage
                               Contig contig = (Contig) clone_assembly.getContigs().get(0);
                               contig.checkForCoverage(sequence_definition.getFlexCloneId(), cds_start,  cds_stop,  base_refsequence);
                               if ( clone_assembly.getStatus() == IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_PASS)
                               {
                                        clone_sequence_id = insertSequence(sequence_definition, clone_assembly, conn );
                               }
                           }
                           IsolateTrackingEngine.updateStatus(clone_assembly.getStatus(),
                                     sequence_definition.getIsolateTrackingId(),  conn );
                           conn.commit();
                           if ( clone_sequence_id != -1) process_clones.add( new Integer( clone_sequence_id ));
                        }
                        catch(Exception e)
                        {
                            i_error_messages.add(e.getMessage() );
                            DatabaseTransaction.rollback(conn);
                        }
                    }
                    //finished all clones in the group
                    min_result_id = sequence_definition.getResultId();
                    isLastRecord = expected_sequence_definition.size() < MAX_NUMBER_OF_ROWS_TO_RETURN;
                }
                
                //insert history
                if ( process_clones.size() > 0)
                {
                    ArrayList processes = new ArrayList();
                    Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                new java.util.Date(),
                                                i_user.getId(),
                                                processes,
                                                Constants.TYPE_OBJECTS);

                    // Process object create
                     //create specs array for the process
                    ArrayList specs = new ArrayList();
                  
                    ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                                ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_ASSEMBLY),
                                                                actionrequest.getId(),
                                                                specs,
                                                                Constants.TYPE_OBJECTS) ;
                    processes.add(process);
                     //finally we must insert request
                    actionrequest.insert(conn);
                    String sql = "";
                    Statement stmt = conn.createStatement();
                    for (int sequence_count = 0; sequence_count < process_clones.size();sequence_count++)
                    {
                        sql = "insert into process_object (processid,objectid,objecttype) values("+process.getId()+","+((Integer)process_clones.get(sequence_count)).intValue() +","+Constants.PROCESS_OBJECT_TYPE_ASSEMBLED_SEQUENCE+")";
                        stmt.executeUpdate(sql);
                    }
                    conn.commit();
                }
            }
            catch(Exception ex)
            {
                i_error_messages.add(ex.getMessage());
                DatabaseTransaction.rollback(conn);
            }
            finally
            {
                try
                {
         //send errors
                    if (i_error_messages.size()>0)
                    {
                         Mailer.sendMessage(i_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                        "elena_taycher@hms.harvard.edu", "Request for sequence assemblyu : error messages.", "Errors\n " ,i_error_messages);
                
                    }
                }
                catch(Exception e){}
                DatabaseTransaction.closeConnection(conn);
            }
            
        }
        
     public ArrayList getErrorMessages(){ return i_error_messages;}
     
     
     
     
     //-------------------------------------------------------
     
     private CloneAssembly assembleSequence(  SequenceDescription sequence_definition )throws BecDatabaseException
     
     {
         CloneAssembly clone_assembly = null;
         try
         {
             //f:\clone_files\546\626\chromat_di     contig_dir
            String trace_files_directory_path =  sequence_definition.getReadFilePath()  ;
            trace_files_directory_path = trace_files_directory_path.substring(1,trace_files_directory_path.lastIndexOf(File.pathSeparatorChar));
            //call phredphrap
            PhredPhrap pp = new PhredPhrap();
            if (i_vector_file_name != null)pp.setVectorFileName(i_vector_file_name);
            String output_file_name = sequence_definition.getFlexCloneId()+ ".fasta.screen.ace.1";
           
            pp.run(trace_files_directory_path, output_file_name );
        
            //get phrdphrap output
            PhredPhrapParser pparser = new PhredPhrapParser();
            clone_assembly = pparser.parse(trace_files_directory_path+output_file_name);
        
            return clone_assembly;
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Error while trying to assemble sequence for isolte: "+ sequence_definition.getIsolateTrackingId()+"\n error "+ e.getMessage());
         }
     }
     
     private ArrayList getExspectedSequenceDescriptions(Connection conn, String status, int min_result_id)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        String sql = "select   refsequenceid, iso.isolatetrackingid as isolatetrackingid , containerid, s.sampleid as sampleid"
+ " from isolatetracking iso,  sample s, sequencingconstruct  constr "
+" where constr.constructid = iso.constructid and iso.sampleid=s.sampleid "
+" and status in "+ status +" and rownum < "+MAX_NUMBER_OF_ROWS_TO_RETURN+"  order by containerid ,refsequenceid";
 
      
        
        ResultSet rs = null;ResultSet rs_ref = null;String sql_sample = null;
      SequenceDescription seq_desc = null;
        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.executeQuery(sql, conn);
            
            while(rs.next())
            {
                seq_desc = new SequenceDescription();
               
                seq_desc.setBecRefSequenceId(rs.getInt("refsequenceID"));
                seq_desc.setIsolateTrackingId( rs.getInt("isolatetrackingid"));
                seq_desc.setContainerId(rs.getInt("containerid"));
                seq_desc.setSampleId(rs.getInt("sampleid"));
               
                if (seq_desc.getSampleId() != -1)
                {
                    sql_sample = "select distinct localpath from filereference where filereferenceid in "
                    +" (select filereferenceid from resultfilereference "
                    +" where resultid in (select resultid from result where sampleid="+seq_desc.getSampleId()+"))";
                    rs_ref = DatabaseTransaction.executeQuery(sql_sample, conn);
                     seq_desc.setReadFilePath("localpath");
                     // reparse f:\clone_files\5968\560\chromat_di
                     ArrayList data = Algorithms.splitString(seq_desc.getReadFilePath(),  String.valueOf(File.pathSeparatorChar));
                     
                     seq_desc.setFlexSequenceId(rs.getInt( Integer.parseInt( (String) data.get(data.size() - 3))));
                    seq_desc.setFlexCloneId(  rs.getInt(Integer.parseInt( (String) data.get(data.size() - 2))));
                    res.add( seq_desc );
                
                }
               // System.out.println(entry.toString());
                
            }
            return res;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting inforamation for sequences that can be assembled from end reads:\nSQL: "+sql);
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }
    }
     
     
    
     
     
     private   int insertSequence(
                         SequenceDescription sequence_definition, 
                         CloneAssembly clone_data,
                         Connection conn)throws BecDatabaseException
     {
         //create sequence 
         try
         {
            Contig contig = (Contig)clone_data.getContigs().get(0);
            CloneSequence clone_seq = new CloneSequence( contig.getSequence(),  contig.getScores(), sequence_definition.getBecRefSequenceId());
            clone_seq.setResultId( sequence_definition.getResultId()) ;//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setIsolatetrackingId ( sequence_definition.getIsolateTrackingId() );//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setStatus (BaseSequence.ASSEMBLY_STATUS_ASSESMBLED);
            clone_seq.insert(conn);
            return clone_seq.getId();
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Error while trying to insert clone sequence with result id "+ sequence_definition.getResultId());
         }
         //insert
     }
     
     
     
     
     protected class SequenceDescription
     {
         private int        i_flex_sequenceid = -1;
         private int        i_flex_cloneid = -1;
         private int        i_resultid = -1;
         private String     i_read_filepath = null;
         private int        i_becrefsequenceid = -1;
         private int        i_isolatetrackingid = -1;
         private int        i_containerid    = -1;
         private int        i_sampleid = -1;
         
         public SequenceDescription(){}
         public SequenceDescription(int v1, int v2, int v3, int v4, int v5, int v7,int v8,String v6)
         {
             i_flex_sequenceid = v1;
             i_flex_cloneid = v2;
             i_resultid = v3;
              i_becrefsequenceid = v4;
             i_isolatetrackingid = v5;
             i_read_filepath = v6;
             i_containerid = v7;
             i_sampleid = v8;
         }
         
         public int        getFlexSequenceId (){ return i_flex_sequenceid   ;}
         public int        getFlexCloneId (){ return i_flex_cloneid   ;}
         public int        getResultId (){ return i_resultid   ;}
         public int        getBecRefSequenceId (){ return i_becrefsequenceid   ;}
         public int        getIsolateTrackingId (){ return i_isolatetrackingid   ;}
         public int         getContainerId(){ return i_containerid;}
         public String      getReadFilePath(){ return i_read_filepath;}
         public int         getSampleId(){ return i_sampleid;}
         
         public void        setReadFilePath(String c){ i_read_filepath=c;}
         public void        setFlexSequenceId  ( int v){  i_flex_sequenceid =v  ;}
         public void        setFlexCloneId ( int v){  i_flex_cloneid   =v;}
         public void        setResultId ( int v){  i_resultid =v  ;}
          public void        setBecRefSequenceId (int v){  i_becrefsequenceid =v  ;}
         public void          setIsolateTrackingId (int v){  i_isolatetrackingid =v  ;}
         public void        setContainerId(int v){ i_containerid = v;}
         public void        setSampleId(int v){ i_sampleid = v;}
     }
     
     
   //***************************************  
    
      public static void main(String args[]) 
     
    {
       
        tester_run_assembler runner = new tester_run_assembler();
        
        User user  = null;
        try
        {
            user = AccessManager.getInstance().getUser("htaycher","htaycher");
        }
        catch(Exception e){}
        runner.setUser(user);
        runner.setResultType( String.valueOf(Result.RESULT_TYPE_ASSEMBLED_FROM_END_READS));
        runner.run();
        System.exit(0);
     }
    
}
