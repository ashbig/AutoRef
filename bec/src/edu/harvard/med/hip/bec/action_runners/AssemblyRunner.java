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
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.util_objects.*;
  import java.util.*;
/**
 *
 * @author  htaycher
 */
public class AssemblyRunner implements Runnable
{

    /** Creates a new instance of tester */
    public  AssemblyRunner()
    {
         m_error_messages = new ArrayList();
    }

         // outputBaseDir specify the base directory for trace file distribution
        private static final String INPUT_BASE_DR = "";
        private static final int MAX_NUMBER_OF_ROWS_TO_RETURN = 50;

        private ArrayList   m_master_container_ids = null;//get from form
        private String      m_phrap_params_file = null;
        private String      m_traceFilesBaseDir = null;
        private ArrayList   m_error_messages = null;
        private String      m_result_type = null;
        private User        m_user = null;
         private String      m_vector_file_name = null;


        public  void        setUser(User v){m_user=v;}
        public void         setResultType(String v){ m_result_type=v;}
        public void         setVectorFileName(String v){m_vector_file_name = v;}

        public void run()
        {
      // The database connection used for the transaction
            Connection conn = null;
            ArrayList process_clones = new ArrayList();
            ArrayList sequences = new ArrayList();
            m_error_messages = new ArrayList();
            int min_result_id = 1;
            boolean isLastRecord = true;
            RefSequence refsequence = null;BaseSequence base_refsequence = null;
            BioLinker   linker5 = null; int cds_start = 0; int cds_stop = 0;
            BioLinker   linker3 = null;
            int cur_containerid = -1;
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                int process_id = createProcessHistory(conn);
          //process only sequences  that are exspected
                while (isLastRecord)
                {
                    ArrayList expected_sequence_definition = getExspectedSequenceDescriptions(conn, m_result_type, min_result_id);

                    if (expected_sequence_definition.size() == 0 )      return ;
                    CloneAssembly clone_assembly = null;
                    CloneDescription clone_definition = null;
                    int clone_sequence_id = -1;
                    for (int count = 0; count < expected_sequence_definition.size(); count ++)
                    {
                        clone_definition= ( CloneDescription)expected_sequence_definition.get(count);
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
                                //gerry's sequences
                                if (refsequence.getText().equals("NNNNN"))
                                {
                                     IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED,
                                                        clone_definition.getIsolateTrackingId(),  conn );
                                    IsolateTrackingEngine.updateAssemblyStatus(
                                                        IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_CDS_NOT_COVERED,
                                                        clone_definition.getIsolateTrackingId(),  conn);
                                    conn.commit();

                                    continue;
                                }
                                base_refsequence =  new BaseSequence(refsequence.getCodingSequence(), BaseSequence.BASE_SEQUENCE );
                                base_refsequence.setId(refsequence.getId());
                                if (base_refsequence.getText().length() >2000)
                                {
                                    IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED,
                                                        clone_definition.getIsolateTrackingId(),  conn );
                                    IsolateTrackingEngine.updateAssemblyStatus(
                                                        IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_CDS_NOT_COVERED,
                                                        clone_definition.getIsolateTrackingId(),  conn);
                                    conn.commit();
                                     System.out.println("Sequence too long. Clone "+clone_definition.getFlexCloneId() +" "+clone_definition.getFlexSequenceId());
                                    continue;
                                }
                                cds_start = linker5.getSequence().length();
                                cds_stop = linker5.getSequence().length() +  base_refsequence.getText().length();
                                base_refsequence.setText( linker5.getSequence() + base_refsequence.getText()+linker3.getSequence());

                            }

                           clone_sequence_id = -1;
                           clone_assembly = assembleSequence( clone_definition );
                           if (clone_assembly == null )
                           {
                                IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED,
                                                        clone_definition.getIsolateTrackingId(),  conn );
                                IsolateTrackingEngine.updateAssemblyStatus(
                                                IsolateTrackingEngine.ASSEMBLY_STATUS_NO_CONTIGS,
                                                clone_definition.getIsolateTrackingId(),  conn);
                                conn.commit();
                                 System.out.println("Assembly null. Clone "+clone_definition.getFlexCloneId() +" "+clone_definition.getFlexSequenceId());
                                continue;
                           }
                           else if( clone_assembly.getContigs().size() != 1)
                           {

                                 IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED,
                                                        clone_definition.getIsolateTrackingId(),  conn );
                                IsolateTrackingEngine.updateAssemblyStatus(
                                                    IsolateTrackingEngine.ASSEMBLY_STATUS_N_CONTIGS,
                                                    clone_definition.getIsolateTrackingId(),  conn);
                               conn.commit();
                               continue;
                           }
                           else
                           {
                               //check coverage
                               Contig contig = (Contig) clone_assembly.getContigs().get(0);
                               int result = contig.checkForCoverage(clone_definition.getFlexCloneId(), cds_start,  cds_stop,  base_refsequence);

                               if ( result == IsolateTrackingEngine.ASSEMBLY_STATUS_PASS
                                || result == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_LINKER5_NOT_COVERED
                                 || result == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_LINKER3_NOT_COVERED
                                  || result == IsolateTrackingEngine.ASSEMBLY_STATUS_FAILED_BOTH_LINKERS_NOT_COVERED )

                               {
                                        clone_sequence_id = insertSequence(clone_definition, contig,

                                        process_id,conn );
                                        if ( clone_sequence_id != -1) process_clones.add( new Integer( clone_sequence_id ));
                                       // System.out.println("Clone "+clone_definition.getFlexCloneId() +" "+result+" "+clone_definition.getFlexSequenceId()+" "+clone_sequence_id);
                               }
                                 IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED,
                                                        clone_definition.getIsolateTrackingId(),  conn );
                                IsolateTrackingEngine.updateAssemblyStatus(
                                                result,
                                                clone_definition.getIsolateTrackingId(),  conn);
                                conn.commit();
                                continue;
                           }


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
         //send errors
                    if (m_error_messages.size()>0)
                    {
                         Mailer.sendMessage(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                        "elena_taycher@hms.harvard.edu", "Request for sequence assembly : error messages.", "Errors\n " ,m_error_messages);

                    }
                    if (m_error_messages.size()==0)
                    {
                         Mailer.sendMessage(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                        "elena_taycher@hms.harvard.edu", "Request for sequence assembly: finished without errors.", "Assembly Request complited.\n ");

                    }
                }
                catch(Exception e){}
                DatabaseTransaction.closeConnection(conn);
            }

        }

     public ArrayList getErrorMessages(){ return m_error_messages;}
 
     //-------------------------------------------------------

     private CloneAssembly assembleSequence(  CloneDescription sequence_definition )throws BecDatabaseException

     {
         CloneAssembly clone_assembly = null;
         try
         {
             //f:\clone_files\546\626\chromat_di     contig_dir
            String trace_files_directory_path =  sequence_definition.getReadFilePath()  ;
            if (trace_files_directory_path == null) return null;
             //replace to c drive
            trace_files_directory_path = trace_files_directory_path.substring(0,trace_files_directory_path.lastIndexOf(File.separator));
         //   trace_files_directory_path =   "c"+trace_files_directory_path;
            //call phredphrap
            PhredPhrap pp = new PhredPhrap();
            if (m_vector_file_name != null)pp.setVectorFileName(m_vector_file_name);
            String output_file_name =  sequence_definition.getFlexCloneId()+ ".fasta.screen.ace.1";
             pp.run(trace_files_directory_path, output_file_name );

            //get phrdphrap output
            PhredPhrapParser pparser = new PhredPhrapParser();
            clone_assembly = pparser.parse(trace_files_directory_path+File.separator +"contig_dir" + File.separator + output_file_name);

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
        String sql = "select flexcloneid, flexsequenceid,  refsequenceid, iso.isolatetrackingid as isolatetrackingid , containerid, s.sampleid as sampleid"
+ " from isolatetracking iso,  sample s, sequencingconstruct  constr , flexinfo f "
+" where constr.constructid = iso.constructid and iso.sampleid=s.sampleid and f.isolatetrackingid=iso.isolatetrackingid "
+" and status in ("+ status +") and rownum < "+MAX_NUMBER_OF_ROWS_TO_RETURN+"  order by containerid ,refsequenceid";



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
                seq_desc.setFlexCloneId(  rs.getInt("flexcloneid"));

                if (seq_desc.getSampleId() != -1)
                {
                    sql_sample = "select distinct localpath from filereference where filereferenceid in "
                    +" (select filereferenceid from resultfilereference "
                    +" where resultid in (select resultid from result where sampleid="+seq_desc.getSampleId()+"))";
                    rs_ref = DatabaseTransaction.executeQuery(sql_sample, conn);
                    if(rs_ref.next())
                    {
                        seq_desc.setReadFilePath(rs_ref.getString("LOCALPATH"));
                     // reparse f:\clone_files\5968\560\chromat_di
                    }
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
            /*Result result = new Result(BecIDGenerator.BEC_OBJECT_ID_NOTSET,     process_id,
                                        sequence_definition.getSampleId(),       null,
                                        Result.RESULT_TYPE_ASSEMBLED_FROM_END_READS_PASS,
                                        BecIDGenerator.BEC_OBJECT_ID_NOTSET      );
            result.insert(conn, process_id );


            CloneSequence clone_seq = new CloneSequence( contig.getSequence(),  contig.getScores(), sequence_definition.getBecRefSequenceId());
            clone_seq.setResultId( result.getId()) ;//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setIsolatetrackingId ( sequence_definition.getIsolateTrackingId() );//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setCloneSequenceStatus (BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED);
            clone_seq.setCloneSequenceType (BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED); //final\conseq\final editied
            clone_seq.setCdsStart( contig.getCdsStart() );
            clone_seq.setCdsStop( contig.getCdsStop() );
            clone_seq.insert(conn);

            */
           return_value = CloneSequence.insertSequenceWithResult(
                         sequence_definition.getSampleId(),
                         sequence_definition.getIsolateTrackingId(),
                         sequence_definition.getBecRefSequenceId(),
                         Result.RESULT_TYPE_ASSEMBLED_FROM_END_READS_PASS,
                         BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED,
                         BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED, contig, process_id, conn);
            return return_value;
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Error while trying to insert clone sequence with result id "+ sequence_definition.getResultId());
         }
         //insert
     }


     private int createProcessHistory(Connection conn) throws BecDatabaseException
     {
         ArrayList processes = new ArrayList();
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
                                                    ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_ASSEMBLY_FROM_END_READS),
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
/*
     protected class SequenceDescription
     {
         private int        m_flex_sequenceid = -1;
         private int        m_flex_cloneid = -1;
         private int        m_resultid = -1;
         private String     m_read_filepath = null;
         private int        m_becrefsequenceid = -1;
         private int        m_isolatetrackingid = -1;
         private int        m_containerid    = -1;
         private int        m_sampleid = -1;

         public SequenceDescription(){}
         public SequenceDescription(int v1, int v2, int v3, int v4, int v5, int v7,int v8,String v6)
         {
             m_flex_sequenceid = v1;
             m_flex_cloneid = v2;
             m_resultid = v3;
              m_becrefsequenceid = v4;
             m_isolatetrackingid = v5;
             m_read_filepath = v6;
             m_containerid = v7;
             m_sampleid = v8;
         }

         public int        getFlexSequenceId (){ return m_flex_sequenceid   ;}
         public int        getFlexCloneId (){ return m_flex_cloneid   ;}
         public int        getResultId (){ return m_resultid   ;}
         public int        getBecRefSequenceId (){ return m_becrefsequenceid   ;}
         public int        getIsolateTrackingId (){ return m_isolatetrackingid   ;}
         public int         getContainerId(){ return m_containerid;}
         public String      getReadFilePath(){ return m_read_filepath;}
         public int         getSampleId(){ return m_sampleid;}

         public void        setReadFilePath(String c){ m_read_filepath=c;}
         public void        setFlexSequenceId  ( int v){  m_flex_sequenceid =v  ;}
         public void        setFlexCloneId ( int v){  m_flex_cloneid   =v;}
         public void        setResultId ( int v){  m_resultid =v  ;}
          public void        setBecRefSequenceId (int v){  m_becrefsequenceid =v  ;}
         public void          setIsolateTrackingId (int v){  m_isolatetrackingid =v  ;}
         public void        setContainerId(int v){ m_containerid = v;}
         public void        setSampleId(int v){ m_sampleid = v;}
     }

*/


}



