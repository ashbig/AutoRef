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
        
        
        
        public  void        setUser(User v){i_user=v;}
        public void         setResultType(String v){ i_result_type=v;}
        
        public void run()
        {
      // The database connection used for the transaction
            Connection conn = null;
            ArrayList process_clones = new ArrayList();
            ArrayList sequences = new ArrayList();
            i_error_messages = new ArrayList();
            int min_result_id = 1;
            boolean isLastRecord = false;
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
      
          //process only sequences  that are exspected
                while (isLastRecord)
                {
                    ArrayList expected_sequence_definition = getExspectedSequenceDescriptions(conn, i_result_type, min_result_id);

                    if (expected_sequence_definition.size() == 0)      return ;
                    CloneAssembly clone_data = null;
                    SequenceDescription sequence_definition = null;
                    CloneSequence clone_sequence = null;
                    for (int count = 0; count < expected_sequence_definition.size(); count ++)
                    {
                        sequence_definition= ( SequenceDescription)expected_sequence_definition.get(count);
                        try
                        {
                           clone_data = assembleSequence( sequence_definition );
                           clone_sequence = insertSequence(sequence_definition, clone_data, conn );
                           IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ASSEMBLY_WITH_SUCESS,
                                                                sequence_definition.getIsolateTrackingId(),  conn );
                           conn.commit();
                           process_clones.add( new Integer( clone_sequence.getId() ));
                           
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
             return clone_assembly;
         }
         catch(Exception e)
         {
             throw new BecDatabaseException("Error while trying to assemble sequence for isolte: "+ sequence_definition.getIsolateTrackingId()+"\n error "+ e.getMessage());
         }
     }
     
     private ArrayList getExspectedSequenceDescriptions(Connection conn, String result_type, int min_result_id)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        String sql = "select  FLEXSEQUENCEID ,FLEXCLONEID , r.resultid as resultid, refsequenceid, iso.isolatetrackingid as isolatetrackingid "
        +" from flexinfo f, isolatetracking iso, result r, sample s, sequencingconstruct  constr "
        +" where constr.constructid = iso.constructid and f.ISOLATETRACKINGID =iso.ISOLATETRACKINGID  and r.sampleid =s.sampleid"
        +" and iso.sampleid=s.sampleid and iso.sampleid in"
        +" (select sampleid from  result where resultvalueid is null and resulttype in ("+
        result_type +")) and rownum = " + MAX_NUMBER_OF_ROWS_TO_RETURN + " and resultid > "+ min_result_id +" order by resultid";
        
        ResultSet rs = null;
      SequenceDescription seq_desc = null;
        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.executeQuery(sql, conn);
            
            while(rs.next())
            {
                seq_desc = new SequenceDescription(rs.getInt("FLEXSEQUENCEID"),
                                 rs.getInt("FLEXCLONEID"), rs.getInt("resultid")
                                ,rs.getInt("refsequenceID"), rs.getInt("isolatetrackingid"));
               // System.out.println(entry.toString());
                res.add( seq_desc );
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
     
     
     private   CloneAssembly runAssemblerandParseOutput(
                         SequenceDescription sequence_definition
                        )throws BecUtilException
     {
         CloneAssembly res = null;
         
         return res;
     }
     
     
     
     private   CloneSequence insertSequence(
                         SequenceDescription sequence_definition, 
                         CloneAssembly clone_data,
                         Connection conn)throws BecDatabaseException
     {
         //create sequence 
         try
         {
             CloneSequence clone_seq = new CloneSequence( clone_data.getSequence(),  clone_data.getScores(), sequence_definition.getBecRefSequenceId());
             clone_seq.setResultId( sequence_definition.getResultId()) ;//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setIsolatetrackingId ( sequence_definition.getIsolateTrackingId() );//BecIDGenerator.BEC_OBJECT_ID_NOTSET= v;}
            clone_seq.setStatus (BaseSequence.ASSEMBLY_STATUS_ASSESMBLED);
            clone_seq.insert(conn);
            return clone_seq;
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
         private int        i_becrefsequenceid = -1;
         private int        i_isolatetrackingid = -1;
         
         public SequenceDescription(){}
         public SequenceDescription(int v1, int v2, int v3, int v4, int v5)
         {
             i_flex_sequenceid = v1;
             i_flex_cloneid = v2;
             i_resultid = v3;
              i_becrefsequenceid = v4;
             i_isolatetrackingid = v5;
         }
         
         public int        getFlexSequenceId (){ return i_flex_sequenceid   ;}
         public int        getFlexCloneId (){ return i_flex_cloneid   ;}
         public int        getResultId (){ return i_resultid   ;}
         public int        getBecRefSequenceId (){ return i_becrefsequenceid   ;}
         public int        getIsolateTrackingId (){ return i_isolatetrackingid   ;}
         
         public void        setFlexSequenceId  ( int v){  i_flex_sequenceid =v  ;}
         public void        setFlexCloneId ( int v){  i_flex_cloneid   =v;}
         public void        setResultId ( int v){  i_resultid =v  ;}
          public void        setBecRefSequenceId (int v){  i_becrefsequenceid =v  ;}
         public void          setIsolateTrackingId (int v){  i_isolatetrackingid =v  ;}
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
