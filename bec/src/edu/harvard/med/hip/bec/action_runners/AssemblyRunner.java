/*
 * AssemblyRunner.java
 *
 * Created on August 27, 2003, 11:02 AM
 */

package edu.harvard.med.hip.bec.action_runners;


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
import edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.util_objects.*;
import  edu.harvard.med.hip.bec.file.*;
  
import java.util.*;
/**
 *
 * @author  htaycher
 */
public class AssemblyRunner extends ProcessRunner
{

     public static final int       END_READS_ASSEMBLY = 10;
     public static final int       FULL_SEQUENCE_ASSEMBLY = -10;
     public static final int       MINIMUM_LINKER_COVERAGE = 10;
     
     public static final int       MAXIMUM_READ_LENGTH = 800;
     
         // outputBaseDir specify the base directory for trace file distribution
      //  private static final String INPUT_BASE_DR = "";
      
        private ArrayList   m_master_container_ids = null;//get from form
        private String      m_phrap_params_file = null;
        private String      m_traceFilesBaseDir = null;
        private ArrayList   m_error_messages = null;
        private String      m_result_type = null;
        private User        m_user = null;
        private String      m_vector_file_name = null;
        private int         m_assembly_mode = END_READS_ASSEMBLY;
        private int         m_quality_trimming_phd_score = 0;
        private int         m_quality_trimming_phd_first_base = 0;
        private int         m_quality_trimming_phd_last_base = 0;
   
        public  void        setUser(User v){m_user=v;}
        public void         setResultType(String v){ m_result_type=v;}
        public void         setVectorFileName(String v){m_vector_file_name = v;}
        public void         setAssemblyMode(int mode){ m_assembly_mode = mode;}
        public void         setQualityTrimmingScore (int v){ m_quality_trimming_phd_score = v;}
        public void         setQualityTrimmingLastBase (int v){ m_quality_trimming_phd_last_base = v;}
        public void         setQualityTrimmingFirstBase (int v){ m_quality_trimming_phd_first_base = v;}
   
   
   
        public String       getTitle(){ return "Request for sequence assembler run.";}


        public void run()
        {
      // The database connection used for the transaction
           
            Connection conn = null;
            ArrayList process_clones = new ArrayList();
            ArrayList sequences = new ArrayList();
            m_error_messages = new ArrayList();
            boolean isLastRecord = true;
            RefSequence refsequence = null;BaseSequence base_refsequence = null;
            BioLinker   linker5 = null; int cds_start = 0; int cds_stop = 0;
            BioLinker   linker3 = null;
            ArrayList expected_sequence_definition = null;
            ArrayList sql_groups_of_items = null;
             CloneDescription clone_definition = null;
             int[] isolate_status = getIsolateStatus();// statuses for isolate - first pass; second - failed
            int cur_containerid = -1;
            int count_of_clones = 0;
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                int process_id = createProcessHistory(conn);
                if (m_assembly_mode == FULL_SEQUENCE_ASSEMBLY) distributeInternalReads();
          //process only sequences  that are exspected
                sql_groups_of_items =  prepareItemsListForSQL();
               for (int count = 0; count < sql_groups_of_items.size(); count++)
               {
                    
                    expected_sequence_definition = getExspectedSequenceDescriptions(conn, m_result_type, (String)sql_groups_of_items.get(count) );
                    if (expected_sequence_definition.size() == 0 )      break; ;
                    for (int clone_count = 0; clone_count < expected_sequence_definition.size(); clone_count ++)
                    {
                        clone_definition= ( CloneDescription)expected_sequence_definition.get(clone_count);
                        //get linker info
                        try
                        {
                            if ((linker5 == null || linker3 == null) || clone_definition.getContainerId() != cur_containerid )
                            {
                                CloningStrategy container_cloning_strategy = Container.getCloningStrategy(clone_definition.getContainerId());
                                if (container_cloning_strategy != null)
                                 {
                                     linker3 = BioLinker.getLinkerById( container_cloning_strategy.getLinker3Id() );
                                     linker5 = BioLinker.getLinkerById( container_cloning_strategy.getLinker5Id() );
                                 }
                                cur_containerid =  clone_definition.getContainerId() ;
                            }

                        //get refsequence if needed
                            if (base_refsequence == null || base_refsequence.getId() != clone_definition.getBecRefSequenceId())
                            {
                                refsequence = new RefSequence( clone_definition.getBecRefSequenceId());
                              
                                base_refsequence =  new BaseSequence(refsequence.getCodingSequence(), BaseSequence.BASE_SEQUENCE );
                                base_refsequence.setId(refsequence.getId());
                                if (m_assembly_mode == END_READS_ASSEMBLY && base_refsequence.getText().length() >1400)
                                {
                                    IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED,
                                                        clone_definition.getIsolateTrackingId(),  conn );
                                    IsolateTrackingEngine.updateAssemblyStatus(
                                                        IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_CDS_NOT_COVERED,
                                                        clone_definition.getIsolateTrackingId(),  conn);
                                    conn.commit();
                                    System.out.println("Sequence too long. Clone "+clone_definition.getCloneId() +" "+clone_definition.getFlexSequenceId());
                                    continue;
                                }
                                cds_start = linker5.getSequence().length();
                                cds_stop = linker5.getSequence().length() +  base_refsequence.getText().length();
                                base_refsequence.setText( linker5.getSequence() + base_refsequence.getText()+linker3.getSequence());

                            }
                            processCloneAssembly(  clone_definition, isolate_status[0], isolate_status[1],
                                        process_id,  conn,  base_refsequence, cds_start,  cds_stop, process_clones);
    

                        }
                        catch(Exception e)
                        {
                            System.out.println(e.getMessage() );
                            m_error_messages.add(e.getMessage() );
                            DatabaseTransaction.rollback(conn);
                        }
                    }

                }


            }
            catch(Exception ex)
            {
                m_error_messages.add(ex.getMessage());
                DatabaseTransaction.rollback(conn);
            }
            finally
            {
                try
                {
                    String message=null;
                    if (m_error_messages != null && m_error_messages.size()>0)
                    {
                        String file_name = Constants.getTemporaryFilesPath() + File.separator + "ErrorMessages"+ System.currentTimeMillis()+".txt";
                        Algorithms.writeArrayIntoFile( m_error_messages, false,  file_name) ;
                        message = getTitle()+ Constants.LINE_SEPARATOR + "Please find error messages for your request in  attached file";
                        Mailer.sendMessageWithAttachedFile(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu", "hip_informatics@hms.harvard.edu",getTitle(), message , 
                        new File(file_name) );
                    }
                    message =  getTitle()+  Constants.LINE_SEPARATOR +"Process finished.";
                    if ( m_items != null && m_items.length() > 0)
                        message +=  Constants.LINE_SEPARATOR + "Request item's ids:\n"+m_items;
                     Mailer.sendMessage      ( m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",  "hip_informatics@hms.harvard.edu", getTitle(), message);

                }catch(Exception e1){}
                if ( conn != null) DatabaseTransaction.closeConnection(conn);
               
            }

        }

     public ArrayList getErrorMessages(){ return m_error_messages;}
 
     //-------------------------------------------------------
     
     private void distributeInternalReads()
     {
         EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
         TraceFilesDistributor tfb = new TraceFilesDistributor();
         tfb.distributeNotActiveChromatFiles(erw.getInputDir(),  erw.getOuputBaseWrongFormat(), erw.getEmptySamplesDir(), erw.getControlSamplesDir());
         m_error_messages.addAll( tfb.getErrorMesages());
         tfb.setIsInnerReads(true); 
         tfb.distributeInternalReadsChromatFiles(erw.getInputDir(), erw.getOuputBaseDir());
         m_error_messages.addAll( tfb.getErrorMesages());
     }
   
     
     
     
     //function returns array of isolatet statuses to be set
     // depend on assembler invocation mode
     //first - member status pass; second - fail
     
     private int[]  getIsolateStatus()
     {
         int[] isolate_status = new int[2];
         switch ( m_assembly_mode ) 
         {
             case END_READS_ASSEMBLY : 
             {
                 isolate_status[0] = IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED;
                 isolate_status [1] =IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED;
                 break;
             }
             case FULL_SEQUENCE_ASSEMBLY : 
             {
                 isolate_status[0] = IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_ASSEMBLED_FROM_INTERNAL_READS;
                 isolate_status [1] =IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_ASSEMBLED_FROM_INTERNAL_READS;
                 break;
             }
            
         }
         return isolate_status;
     }
     
     private void processCloneAssembly( CloneDescription clone_definition,
                            int isolate_status_pass, int isolate_status_fail,
                            int process_id, Connection conn, BaseSequence refsequence,
                            int cds_start, int cds_stop,
                            ArrayList process_clones) throws Exception
     {
           
          CloneAssembly clone_assembly = assembleSequence( clone_definition );
          int clone_sequence_id = -1;
           //if only one read and it covers the whole sequence it can be 
               //taken as assembled sequence if pass checks, create psevdo assembly object  
           if (clone_assembly == null  &&  m_assembly_mode == END_READS_ASSEMBLY && refsequence.getText().length() < MAXIMUM_READ_LENGTH )
           {
               clone_assembly = getAssemblyFromRead(clone_definition, refsequence.getText().length() + MINIMUM_LINKER_COVERAGE);
           }
           if (clone_assembly == null )//&& m_assembly_mode == END_READS_ASSEMBLY )
           {
                IsolateTrackingEngine.updateStatus(isolate_status_fail, clone_definition.getIsolateTrackingId(),  conn );
                IsolateTrackingEngine.updateAssemblyStatus(
                                IsolateTrackingEngine.ASSEMBLY_STATUS_NO_CONTIGS,
                                clone_definition.getIsolateTrackingId(),  conn);
                System.out.println("Assembly null. Clone "+clone_definition.getCloneId() +" "+clone_definition.getFlexSequenceId());
           }
           else if( clone_assembly.getContigs().size() != 1  )// && m_assembly_mode == END_READS_ASSEMBLY )
           {
                IsolateTrackingEngine.updateStatus(isolate_status_fail, clone_definition.getIsolateTrackingId(),  conn );
                IsolateTrackingEngine.updateAssemblyStatus(
                                    IsolateTrackingEngine.ASSEMBLY_STATUS_N_CONTIGS,
                                    clone_definition.getIsolateTrackingId(),  conn);
           }
           else
           {
               //check coverage
               Contig contig = (Contig) clone_assembly.getContigs().get(0);
               int result = contig.checkForCoverage(clone_definition.getCloneId(), cds_start,  cds_stop,  refsequence);

               if ( result == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_NO_MATCH)
               {
                   //check maybe contig is compliment
                    contig.setSequence(SequenceManipulation.getComplimentCaseSencetive(contig.getSequence()));
                    int[] complement_scores = SequenceManipulation.getScoresComplement( contig.getScores() );
                    contig.setScores( Algorithms.convertArrayToString(complement_scores, " "));
                    result = contig.checkForCoverage(clone_definition.getCloneId(), cds_start,  cds_stop,  refsequence);

               }
               if ( result == IsolateTrackingEngine.ASSEMBLY_STATUS_PASS
                || result == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_LINKER5_NOT_COVERED
                 || result == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_LINKER3_NOT_COVERED
                  || result == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_BOTH_LINKERS_NOT_COVERED )

               {
                        clone_sequence_id = insertSequence(clone_definition, contig,   process_id,conn );
                        if ( clone_sequence_id != -1) process_clones.add( new Integer( clone_sequence_id ));
                }
             
                 IsolateTrackingEngine.updateStatus(isolate_status_pass,clone_definition.getIsolateTrackingId(),  conn );
                IsolateTrackingEngine.updateAssemblyStatus( result,clone_definition.getIsolateTrackingId(),  conn);
            }
            conn.commit();
     }  
     
     
     //runs phredphrap and returns parsed output 
     private CloneAssembly assembleSequence(  CloneDescription sequence_definition )throws BecDatabaseException

     {
         CloneAssembly clone_assembly = null;
         try
         {
             //f:\clone_files\546\626\chromat_di     contig_dir
            String trace_files_directory_path =  sequence_definition.getReadFilePath()  ;
            if (trace_files_directory_path == null) return null;
             //replace to c drive
          //  trace_files_directory_path = trace_files_directory_path.substring(0,trace_files_directory_path.lastIndexOf(File.separator));
         //   trace_files_directory_path =   "c"+trace_files_directory_path;
          
            //call phredphrap
            PhredPhrap pp = new PhredPhrap();
            pp.setQualityTrimmingScore (m_quality_trimming_phd_score);
            pp.setQualityTrimmingLastBase(m_quality_trimming_phd_last_base);
            pp.setQualityTrimmingFirstBase (m_quality_trimming_phd_first_base);


   
           
            if (m_vector_file_name != null)pp.setVectorFileName(m_vector_file_name);
            String output_file_name =  sequence_definition.getCloneId()+ ".fasta.screen.ace.1";
            //delete quality and sequence files from end read processing
            FileOperations.deleteAllFilesFormDirectory(trace_files_directory_path + File.separator +"quality_dir");
            FileOperations.deleteAllFilesFormDirectory(trace_files_directory_path + File.separator +"sequence_dir");
            
            //delete all .phd files from previous processing
            FileOperations.deleteAllFilesFormDirectory(trace_files_directory_path + File.separator +"phd_dir");
            pp.run(trace_files_directory_path, output_file_name );
           //get phrdphrap output
            PhredPhrapParser pparser = new PhredPhrapParser();
            clone_assembly = pparser.parse(trace_files_directory_path+File.separator +"contig_dir" + File.separator + output_file_name);
            return clone_assembly;
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Error while trying to assemble sequence for isolte: "+ sequence_definition.getIsolateTrackingId()+" with cloneid :"+sequence_definition.getCloneId()+" error "+ e.getMessage());
         }
     }
  
            
     
      private ArrayList getExspectedSequenceDescriptions(Connection conn, String status, String sql_items)throws BecDatabaseException
      {
          String sql = null;
          EndReadsWrapperRunner erw = new EndReadsWrapperRunner();
          String trace_files_path = erw.getOuputBaseDir();
          switch ( m_assembly_mode ) 
         {
             case END_READS_ASSEMBLY :
             {
                 //submission by plate
                sql = "select flexcloneid, flexsequenceid,  refsequenceid, iso.isolatetrackingid as isolatetrackingid , containerid, s.sampleid as sampleid"
+ " from isolatetracking iso,  sample s, sequencingconstruct  constr , flexinfo f "
+" where constr.constructid = iso.constructid and iso.sampleid=s.sampleid and f.isolatetrackingid=iso.isolatetrackingid "
+" and status in ("+ status +") and containerid in ( select containerid from containerheader where label in "
+ "("+sql_items + ")) order by containerid ,refsequenceid";
               //  System.out.println(sql);
                break;
             }
             case FULL_SEQUENCE_ASSEMBLY :
             {
                   
                 sql =  "select flexcloneid, flexsequenceid,  refsequenceid, iso.isolatetrackingid as isolatetrackingid , containerid, s.sampleid as sampleid"
+ " from isolatetracking iso,  sample s, sequencingconstruct  constr , flexinfo f "
+" where constr.constructid = iso.constructid and iso.sampleid=s.sampleid and f.isolatetrackingid=iso.isolatetrackingid "
+" and f.flexcloneid in ("+sql_items +") order by containerid ,refsequenceid";
                break;
             }
             
         }
        return getExspectedCloneDescriptions( conn,  sql, trace_files_path);
      }
     
     private ArrayList getExspectedCloneDescriptions(Connection conn, String sql, String trace_files_path)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
       ResultSet rs = null;ResultSet rs_ref = null;String sql_sample = null;
      CloneDescription seq_desc = null;
        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.executeQuery(sql,conn);

            while(rs.next())
            {
                seq_desc = new CloneDescription();

                seq_desc.setBecRefSequenceId(rs.getInt("refsequenceID"));
                seq_desc.setIsolateTrackingId( rs.getInt("isolatetrackingid"));
                seq_desc.setContainerId(rs.getInt("containerid"));
                seq_desc.setSampleId(rs.getInt("sampleid"));
                seq_desc.setFlexSequenceId(rs.getInt( "flexsequenceid"));
                seq_desc.setCloneId(  rs.getInt("flexcloneid"));

                if (seq_desc.getSampleId() != -1)
                {
                        seq_desc.setReadFilePath(trace_files_path +File.separator +seq_desc.getFlexSequenceId() + File.separator + seq_desc.getCloneId() );
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
                         CloneDescription sequence_definition,
                         Contig contig,
                         int process_id,
                         Connection conn)throws BecDatabaseException
     {
         int return_value = -1;
         //create sequence
         try
         {
        //check wether the same sequence have been already submitted
            if ( m_assembly_mode == FULL_SEQUENCE_ASSEMBLY)
            {
                if (isSequenceExsists(sequence_definition,contig)) 
                    return return_value;
            }
            CloneSequence clone_seq = new CloneSequence( contig.getSequence(),  contig.getScores(),  sequence_definition.getBecRefSequenceId());
          //  clone_seq.setResultId( result.getId()) ;//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setIsolatetrackingId ( sequence_definition.getIsolateTrackingId() );//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setCloneSequenceStatus (BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED);
            clone_seq.setCloneSequenceType (BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED); //final\conseq\final editied
            clone_seq.setCdsStart( contig.getCdsStart() );
            clone_seq.setCdsStop( contig.getCdsStop() );
            clone_seq.insert(conn);    
            /*
           return_value = CloneSequence.insertSequenceWithResult(
                         sequence_definition.getSampleId(),
                         sequence_definition.getIsolateTrackingId(),
                         sequence_definition.getBecRefSequenceId(),
                         Result.RESULT_TYPE_ASSEMBLED_FROM_END_READS_PASS,
                         BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED,
                         BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED, contig, process_id, conn);
            return return_value;
             */
            return clone_seq.getId();
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Error while trying to insert clone sequence with result id "+ sequence_definition.getResultId());
         }
         //insert
     }
//function checks whether this sample has exact the same sequence assigned
     private boolean isSequenceExsists(CloneDescription sequence_definition,Contig contig)throws BecDatabaseException
     {
         
        String sql = "select SEQUENCEID      from ASSEMBLEDSEQUENCE where isolatetrackingid = "+sequence_definition.getIsolateTrackingId() +" order by submissiondate ";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        int sequenceid = -1;
        ArrayList sequences = new ArrayList();
       // String contig_sequence = contig.getSequence().toUpperCase();
        ScoredSequence sequence = null; String text = null; String scores = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                sequenceid =  rs.getInt ("SEQUENCEID") ;
                text = BaseSequence.getSequenceInfo(sequenceid, BaseSequence.SEQUENCE_INFO_TEXT);
                scores = BaseSequence.getSequenceInfo(sequenceid, BaseSequence.SEQUENCE_INFO_SCORE);
                sequence = new ScoredSequence(text,scores);
                sequences.add(sequence);
            }
          
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Cannot extract sequence information\nsql "+sql);
         }
        if (sequence == null) return false;
        ScoredSequence contig_sequence = new ScoredSequence(contig.getSequence(),contig.getScores());
        return sequence.isTheSame( contig_sequence );
      
     }
     private int createProcessHistory(Connection conn) throws BecDatabaseException
     {
         ArrayList processes = new ArrayList();
         String process_name = null;
         if ( (process_name = getProcessName()) == null) throw new BecDatabaseException("Unknown process");
        
         try
         {
        Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                    new java.util.Date(),
                                    m_user.getId(),
                                    processes,
                                    Constants.TYPE_OBJECTS);

        // Process object create
         //create specs array for the process
        ArrayList specs = new ArrayList();

        ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                    ProcessDefinition.ProcessIdFromProcessName(process_name),
                                                    actionrequest.getId(),
                                                    specs,
                                                    Constants.TYPE_OBJECTS) ;
        processes.add(process);
         //finally we must insert request
        actionrequest.insert(conn);
        conn.commit();
        return process.getId();
         } catch(Exception e)
         {
             throw new BecDatabaseException("Cannot create process");
         }
     }
     
     
     
     private CloneAssembly getAssemblyFromRead(CloneDescription clone_definition, int refsequence_length)
     {
         
         ArrayList reads = null;
         Read read = null; String sequence = null; String scores = null;
         int read_length = -1;
         try
         {
             reads = Read.getReadByIsolateTrackingId(clone_definition.getIsolateTrackingId( ));
             if ( reads.size() > 1)return null;
             read = (Read)reads.get(0);
             //if read reverse get compliment
             if ( read.getType() == Read.TYPE_ENDREAD_REVERSE)
             {
                    sequence = SequenceManipulation.getCompliment(read.getSequence().getText()) ;
                    int[] arr_scores = SequenceManipulation.getScoresComplement(read.getSequence().getScores());
                    scores = Algorithms.convertArrayToString(arr_scores, " ");
   //System.out.println(Algorithms.convertArrayToString(arr_scores, " "));
             }
             else if ( read.getType() == Read.TYPE_ENDREAD_FORWARD)
             {
                 sequence = read.getSequence().getText() ;
                 scores = read.getSequence().getScores();
             }
             else return null;
         }
         catch(Exception e){ return null;}
         
       
         //take longest read
         if (read == null) return null;
         CloneAssembly clone_assembly = new CloneAssembly();
         clone_assembly.setNumOfReads(1);
         Contig contig = new Contig();
         contig.setNumberOfReadsInContig(1);
         contig.setName("");   
         System.out.println(scores);
         contig.setSequence(sequence );         contig.setScores(scores);
         clone_assembly.addContig(contig);
         
         return clone_assembly;
     }

     private String getProcessName()
     {
         switch ( m_assembly_mode ) 
         {
             case END_READS_ASSEMBLY : return ProcessDefinition.RUN_ASSEMBLY_FROM_END_READS;
             case FULL_SEQUENCE_ASSEMBLY : return ProcessDefinition.RUN_ASSEMBLY;
             default : return null;
         }
     }

public static void main(String args[]) 
{
    AssemblyRunner runner = new AssemblyRunner();
       CloneAssembly clone_assembly =null;
    try
    {
         runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
        runner.setResultType( String.valueOf(IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN));
         runner.setAssemblyMode(AssemblyRunner.FULL_SEQUENCE_ASSEMBLY);
       //      runner.setItems("116384	");
      // runner.setItemsType( Constants.ITEM_TYPE_CLONEID);
            runner.setInputData(Constants.ITEM_TYPE_CLONEID,"134897");  
            runner.setVectorFileName("vector_empty.seq");
        runner.run();           
        /*
        CloneDescription clone_definition = new CloneDescription();
        clone_definition.setIsolateTrackingId(10934);
        clone_definition.setFlexCloneId(32955);
        BaseSequence base_refsequence =  new BaseSequence("GTGGAACTCTTCAAAGAATTCACCTTCGAATCCGCCCATCGCCTGCCCCACGTCCCCGAAGGCCACAAATGCGGGCGCCTGCACGGCCACTCGTTCCGTGTCGCCATCCACATCGAAGGCGAGGTCGATCCGCATACCGGCTGGATCCGCGACTTCGCGGAAATCAAGGCGATCTTCAAGCCGATCTACGAGCAACTCGACCACAATTATCTGAACGATATTCCAGGCCTGGAAAACCCCACCAGCGAAAACCTCTGCCGCTGGATCTGGCAACAACTCAAGCCGCTGTTGCCGGAACTCTCCAAGGTCCGCGTCCACGAAACTTGCACCAGCGGTTGCGAATATCGGGGCGATTGA"
     
        
        
        
        , BaseSequence.BASE_SEQUENCE );
        base_refsequence.setId(123);
                                
        CloneAssembly clone_assembly = runner.getAssemblyFromRead( clone_definition,  base_refsequence.getText().length());
        Contig contig = (Contig) clone_assembly.getContigs().get(0);
        
        
        
        BioLinker linker3 = BioLinker.getLinkerById(32);
        BioLinker linker5 = BioLinker.getLinkerById(31);
                                 
        int cds_start = linker5.getSequence().length();
       int  cds_stop = linker5.getSequence().length() +  base_refsequence.getText().length();
       
        base_refsequence.setText( linker5.getSequence() + base_refsequence.getText()+linker3.getSequence());

        int result = contig.checkForCoverage(clone_definition.getFlexCloneId(), cds_start,  cds_stop,  base_refsequence);
        System.out.print(result);
         
      
        String trace_files_directory_path =  "C:\\bio\\plate_analysis\\clone_samples\\12894\\43935\\"  ;
        trace_files_directory_path = trace_files_directory_path.substring(0,trace_files_directory_path.lastIndexOf(File.separator));
         //   trace_files_directory_path =   "c"+trace_files_directory_path;
         PhredPhrap pp = new PhredPhrap();
             String output_file_name =  "12.fasta.screen.ace.1";
            //delete quality and sequence files from end read processing
            runner.deleteTrimmedFiles(trace_files_directory_path);
            
             pp.run(trace_files_directory_path, output_file_name );

            //get phrdphrap output
            PhredPhrapParser pparser = new PhredPhrapParser();
            String fn = trace_files_directory_path+File.separator +"contig_dir" + File.separator + output_file_name;
             clone_assembly = pparser.parse(fn);

         */
    }catch(Exception e){}
    System.exit(0);
}


}



