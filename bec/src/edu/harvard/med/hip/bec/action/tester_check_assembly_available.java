/*
 * tester_check_assembly_available.java
 *
 * Created on July 14, 2003, 11:40 AM
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
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
  import java.util.*;
/**
 *
 * @author  htaycher
 */
public class tester_check_assembly_available
{
    
    /** Creates a new instance of tester */
    public tester_check_assembly_available()    {    }
    
          
        private ArrayList   i_master_container_ids = null;//get from form
        private ArrayList   i_error_messages = null;
        private boolean     i_isAssembleAllFromEndReads = true;
        private User        i_user = null;
        
        
        public void         isAssembleAllFromEndReads(boolean v){ i_isAssembleAllFromEndReads = v;}
        public  void        setUser(User v){i_user=v;}
        public void         setPlateIds(ArrayList v){ i_master_container_ids = v;}
        public void         addPlateId(int v){if (i_master_container_ids == null) i_master_container_ids = new ArrayList(); i_master_container_ids.add(new Integer(v));}
        
        public void run()
        {
      // The database connection used for the transaction
            Connection conn = null;
            ArrayList sequences = new ArrayList();
            i_error_messages = new ArrayList();
            ArrayList created_results = new ArrayList();
            int process_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
              String sql = "";
              int cur_plate_id =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
              ArrayList constructs = new ArrayList();
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                Statement stmt = conn.createStatement();
                Construct construct = null;
                for (int plate_count = 0; plate_count < i_master_container_ids.size(); plate_count ++)
                {
                    
                    try
                    {
                        cur_plate_id = ((Integer)i_master_container_ids.get(plate_count)).intValue();
                        constructs = Construct.getConstructsFromPlate(cur_plate_id);
                        for (int construct_count = 0; construct_count < constructs.size();construct_count++)
                        {
                            construct = (Construct) constructs.get(construct_count);
                            created_results.addAll( runConstruct(construct, conn) );
                        }
                        
                        //if process object not created yet, create it
                        if (process_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                        {
                            process_id = createRequestProcessObject(conn);
                        }
                        //insert connectors to result objects
                        for (int result_count = 0; result_count < created_results.size(); result_count++)
                        {
                             sql = "insert into process_object (processid,objectid,objecttype) values("+process_id+","+((Integer)created_results.get(result_count)).intValue()+","+Constants.PROCESS_OBJECT_TYPE_RESULT+")";
                             stmt.executeUpdate(sql);
                        }
                        conn.commit();
                    }
                    catch(Exception ex)
                    {
                        i_error_messages.add(ex.getMessage());
                        DatabaseTransaction.rollback(conn);
                    }
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
                    if (i_error_messages.size()>0)//send errors
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
     
    
    
     
    private ArrayList runConstruct(Construct construct, Connection conn)throws Exception
    {
        ArrayList result_ids = new ArrayList();
        ArrayList isolate_trackings = construct.getIsolateTrackings();
        IsolateTrackingEngine it = null;
        ArrayList reads = null;
        Result result = null;
               
        RefSequence refsequence = construct.getRefSequence();
        for (int isolate_count = 0; isolate_count < isolate_trackings.size(); isolate_count++)
        {
            it = (IsolateTrackingEngine) isolate_trackings.get(isolate_count);
              // this is for not known gerry sequences
            if (refsequence.getText().equals("NNNNN") ) 
            {
                result = new Result(BecIDGenerator.BEC_OBJECT_ID_NOTSET,   BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                      it.getSampleId(),       null,
                                                    Result.RESULT_TYPE_ASSEMBLED_FROM_END_READS,
                                                    BecIDGenerator.BEC_OBJECT_ID_NOTSET      );
                result.insert(conn, BecIDGenerator.BEC_OBJECT_ID_NOTSET );
                result_ids.add(new Integer( result.getId()));
                IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_READY_FOR_ASSEMBLY,it.getId(),  conn );
            }
                
            if ( it.getStatus() != IsolateTrackingEngine.PROCESS_STATUS_ER_CONFIRMED ) continue;
            if ( it.isCoveredByEndReads(refsequence) )
            {
                result = new Result(BecIDGenerator.BEC_OBJECT_ID_NOTSET,   BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                      it.getSampleId(),       null,
                                                    Result.RESULT_TYPE_ASSEMBLED_FROM_END_READS,
                                                    BecIDGenerator.BEC_OBJECT_ID_NOTSET      );
                result.insert(conn, BecIDGenerator.BEC_OBJECT_ID_NOTSET );
                result_ids.add(new Integer( result.getId()));
                IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_READY_FOR_ASSEMBLY,it.getId(),  conn );
            }
            else
            {
                IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_READY_FOR_INTERNAL_READS,it.getId(),  conn );
            }
        }
        return result_ids;
    }
     
    private boolean isCoveredByEndReads()
    {
        return false;
    }
     private int createRequestProcessObject(Connection conn) throws Exception
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
                                                        ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_PREPARE_ASSEMBLY),
                                                        actionrequest.getId(),
                                                        specs,
                                                        Constants.TYPE_OBJECTS) ;
            processes.add(process);
             //finally we must insert request
            actionrequest.insert(conn);
            
            conn.commit();
            return process.getId();   

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
       
        tester_check_assembly_available runner = new tester_check_assembly_available();
        int plateid = 0;
        User user  = null;
        try
        {
            user = AccessManager.getInstance().getUser("htaycher","htaycher");
        }
        catch(Exception e){}
        runner.setUser(user);
        runner.addPlateId(plateid);
        runner.isAssembleAllFromEndReads(true);
        runner.run();
        System.exit(0);
     }
    


    
}
