/*
 * EndReadsRequestRunner.java
 *
 * Created on September 2, 2003, 11:55 AM
 */

package edu.harvard.med.hip.bec.action_runners;

/**
 *
 * @author  HTaycher
 */

    /*
 
 * end reads processing request
 * Created on May 6, 2003, 3:00 PM
 */


import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
  import java.util.*;
/**
 *
 * @author  htaycher
 */
  public class EndReadsRequestRunner extends ProcessRunner
{
    
     
  
        private ArrayList   i_master_container_ids = null;//get from form
        private boolean     i_isForward = false;//get from form
        private boolean     i_isReverse = false;//get from form
        private int         i_forward_primerid = -1;//get from form
        private int         i_reverse_primerid = -1;
     
        public void         setContainerIds(ArrayList v)        { i_master_container_ids = v;}
        public void         setForwardPrimerId(int id)        {i_forward_primerid = id; i_isForward =true;}
        public void         setRevercePrimerId(int id)        {i_reverse_primerid = id; i_isReverse = true;}
        public String getTitle()     {   return "Request for end reads sequencing";  }     
     
        public void run()
        {
            
            
            // The database connection used for the transaction
            Connection conn = null;
            ArrayList master_plates = new ArrayList();
            ArrayList files = new ArrayList();
            String requested_plates = "";
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                //request object
                ArrayList processes = new ArrayList();
                Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                            new java.util.Date(),
                                            m_user.getId(),
                                            processes,
                                            Constants.TYPE_OBJECTS);
                //create specs array for the process
                ArrayList specids = new ArrayList();
                specids.add(new Integer(i_forward_primerid));
                specids.add(new Integer(i_reverse_primerid));
                // Process object create
                ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                        ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_ENDREADS_SEQUENCING),
                                                        actionrequest.getId(),
                                                        specids,
                                                        Constants.TYPE_ID) ;
                
                 processes.add(process);
                 //finally we must insert request
                actionrequest.insert(conn);
                //patch here
                 process.insertConnectorToSpec(conn, Spec.VECTORPRIMER_SPEC_INT);
                
                //get master plates from db
                Container container = null;Sample smp = null;
                
                int[] result_types = {Result.RESULT_TYPE_ENDREAD_FORWARD,Result.RESULT_TYPE_ENDREAD_REVERSE};
                for (int count =0; count < i_master_container_ids.size(); count++)
                {
                    container = new Container( ( (Integer)i_master_container_ids.get(count)).intValue());
                    requested_plates += container.getLabel();
                    
                    //check wether this plate has end read results already
                    if (container.checkForResultTypes(result_types))
                    {
                        m_error_messages.add("Plate "+container.getLabel()+" has enr read result - it wont be processed");
                        continue;
                    }
                  
                    container.restoreSampleIsolate(false,false);
                    master_plates.add(container);
                }
                
                // for each plate
                ArrayList isolates = null;
                Result result = null;
                IsolateTrackingEngine istrk = null;
                
                for (int count = 0; count < master_plates.size(); count++)
                {
                    //get all isolate tracking with status 'submitted'
                    container = (Container) master_plates.get(count);
                    processPlate( container,  m_file_list_reports, conn, process.getId());
                   
                }
                // commit the transaction
                if (master_plates.size() != 0)
                    conn.commit();
            }
            catch(Exception ex)
            {
                m_error_messages.add(ex.getMessage());
                DatabaseTransaction.rollback(conn);
            }
            finally
            {
               sendEMails ( getTitle() );
                DatabaseTransaction.closeConnection(conn);
            }
            
        }
        
     
        
        private void processPlate(Container container, ArrayList file_list, Connection conn, int process_id) throws Exception
        {
            ArrayList naming_file_entries_reverse = new ArrayList();
            ArrayList  naming_file_entries_forward =  new ArrayList();
            ArrayList rearray_file_entries_reverse =  new ArrayList();
            ArrayList  rearray_file_entries_forward =  new ArrayList();
            NamingFileEntry file_entry = null;
            File file = null; Result result = null;
            int cloneid = 0;
            IsolateTrackingEngine istrk = null;
            Sample smp = null;
            boolean isStatusUpdated = false;
            try
            {
                for (int sample_count = 0; sample_count < container.getSamples().size(); sample_count++)
                {
                    smp = (Sample) container.getSamples().get(sample_count);
                    isStatusUpdated = false;

                    //valid sample - create result
                    if (i_isForward)
                    {
                        cloneid = 0;
                        if ( smp.isClone())
                        {
                             //create result for each not empty /not control sample per read
                            istrk = smp.getIsolateTrackingEngine();

                            result = new Result(BecIDGenerator.BEC_OBJECT_ID_NOTSET,     process_id,
                                                    istrk.getSampleId(),       null,
                                                    Result.RESULT_TYPE_ENDREAD_FORWARD,
                                                    BecIDGenerator.BEC_OBJECT_ID_NOTSET      );
                            result.insert(conn, process_id );
                            cloneid = istrk.getFlexInfo().getFlexCloneId();
                            //change isolate status
                            if ( !isStatusUpdated) 
                            {

                                IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_INITIATED,istrk.getId(),  conn );
                                 isStatusUpdated = true;
                            }

                        }

                        naming_file_entries_forward.add(  createNamingFileEntry( smp, Constants.READ_DIRECTION_FORWARD)  );
                       // rearray_file_entries_forward.add(new RearrayFileEntry( cloneid,container.getLabel(),smp.getPosition(),  container.getLabel()+"-F", smp.getPosition()));
                    }
                    if (i_isReverse)
                    {
                        cloneid = 0;
                        if ( smp.isClone())
                        {
                            istrk = smp.getIsolateTrackingEngine();
                            cloneid = istrk.getFlexInfo().getFlexCloneId();
                            result = new Result(BecIDGenerator.BEC_OBJECT_ID_NOTSET,    process_id,
                                                istrk.getSampleId(),     null,
                                                Result.RESULT_TYPE_ENDREAD_REVERSE,     
                                                BecIDGenerator.BEC_OBJECT_ID_NOTSET
                                                );
                            result.insert(conn, process_id);
                             //change isolate status
                             if ( !isStatusUpdated) 
                            {
                                 IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_ER_INITIATED,istrk.getId(),  conn );
                                 isStatusUpdated = true;
                             }
                        }
                        naming_file_entries_reverse.add(  createNamingFileEntry( smp, Constants.READ_DIRECTION_REVERSE )  );
                        //rearray_file_entries_reverse.add(new RearrayFileEntry( cloneid,container.getLabel(),smp.getPosition(),  container.getLabel()+"-R", smp.getPosition()));

                    }
               }

                //create files and append them to the file list
                if (i_isForward)
                {
                    file = NamingFileEntry.createNamingFile(naming_file_entries_forward, Constants.getTemporaryFilesPath()+ container.getLabel() + "_naming_endreads_f.txt");
                    file_list.add(file);
                   // file = RearrayFileEntry.createRearrayFile( rearray_file_entries_forward, "/tmp/"+ container.getLabel() + "rearray_endreads_f.txt");
                  //  file_list.add(file);
                }
                if (i_isReverse)
                {
                    file = NamingFileEntry.createNamingFile(naming_file_entries_reverse,Constants.getTemporaryFilesPath()+ container.getLabel() + "_naming_endreads_r.txt");
                    file_list.add(file);
                 //   file = RearrayFileEntry.createRearrayFile( rearray_file_entries_reverse, "/tmp/"+ container.getLabel() + "rearray_endreads_r.txt");
                 //   file_list.add(file);
                }
            }catch(Exception ex)
            {
                System.out.println(ex.getMessage());
                throw new BecDatabaseException("Cannot process request for plate "+container.getLabel()+" Request will be aborted. Please try later.");
            }
    }
        
        private NamingFileEntry createNamingFileEntry(Sample smp, String orientation)
                                throws Exception
        {
            NamingFileEntry entry =new  NamingFileEntry(smp.getIsolateTrackingEngine().getFlexInfo().getFlexCloneId()
                        , orientation,
                        smp.getIsolateTrackingEngine().getFlexInfo().getFlexPlateId(),
                       edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_int_to_alphanumeric( smp.getPosition()), 
                        smp.getIsolateTrackingEngine().getFlexInfo().getFlexSequenceId(),
                        0);
            return entry;
        }
    
     public static void main(String args[])
    {
        try
        {
          ArrayList master_container_ids = new ArrayList();
          ArrayList  master_container_labels = new ArrayList();
           master_container_labels.add("YMG000488");
           master_container_ids = Container.findContainerIdsFromLabel(master_container_labels);


            int forward_primer_id = 1 ;
            int reverse_primer_id = -1;

            EndReadsRequestRunner runner = new EndReadsRequestRunner();
            runner.setContainerIds(master_container_ids );
            if ( forward_primer_id != -1) runner.setForwardPrimerId( forward_primer_id );
            if ( reverse_primer_id != -1)runner.setRevercePrimerId(reverse_primer_id);
            runner.setUser(AccessManager.getInstance().getUser("htaycher345","htaycher"));
            runner.run();

        }catch(Exception e){}
        System.exit(0);
     }
    
   
    
}

    

