/*
 * RunEndReadsWrapper.java
 *
 * Created on April 7, 2003, 1:53 PM
 */

package edu.harvard.med.hip.bec.action;

import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;

/**
 *
 * @author  htaycher
 */
public class tester_isolateranker 
{
    public tester_isolateranker(){}
    
    public static void main(String args[]) 
     
    {
        ArrayList master_container_ids = new ArrayList();
       
     
      master_container_ids.add(new Integer(67));
      
     
        tester_isolateranker runner = new tester_isolateranker();
      
        int specid_polymorphism = -1;
        User user  = null;
        try
        {
            user = AccessManager.getInstance().getUser("htaycher1","htaycher");
            runner.setContainerIds(master_container_ids );
            runner.setCutoffValuesSpec( (FullSeqSpec)Spec.getSpecById(11, Spec.FULL_SEQ_SPEC_INT));
            runner.setPenaltyValuesSpec( (EndReadsSpec)Spec.getSpecById(10, Spec.END_READS_SPEC_INT));
            if (specid_polymorphism != -1)
                runner.setPolymorphismSpec((PolymorphismSpec)Spec.getSpecById(specid_polymorphism, Spec.POLYMORPHISM_SPEC_INT));
            runner.setUser(user);
            runner.run();
        }
        catch(Exception e){}
        
        System.exit(0); 
     }
      
       
         private ArrayList           i_master_container_ids = null;//get from form
        private boolean             i_isRunPolymorphismFinder = false;//get from form
        
        private FullSeqSpec         i_fullseq_spec = null;
        private EndReadsSpec        i_endreads_spec = null;
        private PolymorphismSpec    i_polymorphism_spec = null;
        private User                i_user = null;
        
        private ArrayList   i_error_messages = null;
        
       
        public void         setContainerIds(ArrayList v)        { i_master_container_ids = v;}
        public void         setCutoffValuesSpec(FullSeqSpec specs)        {i_fullseq_spec = specs;}
        public void         setPenaltyValuesSpec(EndReadsSpec specs)        {i_endreads_spec = specs;}
        public void         setPolymorphismSpec(PolymorphismSpec specs)        {i_polymorphism_spec = specs; i_isRunPolymorphismFinder = true;}
        public  void        setUser(User v){i_user=v;}
        
        public void run()
        {
           
            // The database connection used for the transaction
            Connection conn = null;
            ArrayList master_plates = new ArrayList();
            String requested_plates=null;
            i_error_messages = new ArrayList();
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
           
           
                int container_id = -1;
                IsolateRanker isolate_ranker = null;
            
                //get construct from db// synchronize block here
                for (int plate_count = 0; plate_count < i_master_container_ids.size(); plate_count++)
                {
                    container_id = ((Integer)i_master_container_ids.get(plate_count)).intValue();
                    //get common primers used for reads - assumption each plate run with the same primers
// this staff will work only for one pair of end read primers !!!!!!!!!!!!
                    // fix for multipal pairs
                    Oligo[] oligos = Container.findEndReadsOligos(container_id);
                     requested_plates += container_id;
                    
                    ArrayList constructs = getConstructs(container_id);
                      int linker3_length = 0;
                     int linker5_length = 0;
                     
                     CloningStrategy container_cloning_strategy = Container.getCloningStrategy(container_id);
                      if (container_cloning_strategy != null)
                     {
                         container_cloning_strategy.setLinker3( BioLinker.getLinkerById( container_cloning_strategy.getLinker3Id()) );
                         container_cloning_strategy.setLinker5( BioLinker.getLinkerById( container_cloning_strategy.getLinker5Id() ));
                          if (container_cloning_strategy.getLinker3() == null || container_cloning_strategy.getLinker5() == null ||
                            container_cloning_strategy.getStartCodon() == null || container_cloning_strategy.getFusionStopCodon() == null ||
                            container_cloning_strategy.getClosedStopCodon()==null)
                         {
                             throw new Exception("Cannot get Cloning strategy details");
                         }
                      }
                     
                     
                    if (i_isRunPolymorphismFinder)
                    {
                        isolate_ranker = new IsolateRanker(i_fullseq_spec,  i_endreads_spec,constructs,  i_polymorphism_spec);
                    }
                    else
                    {
                         isolate_ranker = new IsolateRanker(i_fullseq_spec,  i_endreads_spec,constructs);
                    }
                   
                     isolate_ranker.setCloningStrategy(container_cloning_strategy);
                     linker3_length = container_cloning_strategy.getLinker3().getSequence().length();
                     linker5_length = container_cloning_strategy.getLinker5().getSequence().length();
                      
                     
                    if (oligos[0] != null)
                    {
                         isolate_ranker.set5pMinReadLength(oligos[0].getLeaderLength() + linker5_length + Constants.NUMBER_OF_BASES_ADD_TO_LINKER_FORREAD_QUALITY_DEFINITION);
                         isolate_ranker.setForwardReadSence( oligos[0].getOrientation() == Oligo.ORIENTATION_SENSE);
                    }
                    if (oligos[1] != null)
                    {
                         isolate_ranker.set3pMinReadLength(oligos[1].getLeaderLength() + linker3_length + Constants.NUMBER_OF_BASES_ADD_TO_LINKER_FORREAD_QUALITY_DEFINITION);
                        isolate_ranker.setReverseReadSence( oligos[1].getOrientation() == Oligo.ORIENTATION_SENSE) ; 
                    }
                  
                   
                    isolate_ranker.run(conn);
                   System.out.println("Finished container "+container_id);
                }
  
                i_error_messages.addAll( isolate_ranker.getErrorMessages() );
                     //request object
                if (isolate_ranker.getProcessedConstructId().size() > 0)
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
                    specs.add(i_fullseq_spec);
                    specs.add( i_endreads_spec );
                    if ( i_polymorphism_spec != null)
                    {
                        specs.add(i_polymorphism_spec);
                    }
                    ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                                ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_ENDREADS_EVALUATION),
                                                                actionrequest.getId(),
                                                                specs,
                                                                Constants.TYPE_OBJECTS) ;
                    processes.add(process);
                     //finally we must insert request
                    actionrequest.insert(conn);
                    
                    String sql = "";
                    Statement stmt = conn.createStatement();
                    
                    for (int construct_count = 0; construct_count < isolate_ranker.getProcessedConstructId().size();construct_count++)

                    {
                        sql = "insert into process_object (processid,objectid,objecttype) values("+process.getId()+","+((Integer)isolate_ranker.getProcessedConstructId().get(construct_count)).intValue()+","+Constants.PROCESS_OBJECT_TYPE_CONSTRUCT+")";
                        stmt.executeUpdate(sql);
                    }

                    
                    conn.commit();
                }
            }

            catch(Exception ex1)
            {
                 ex1.printStackTrace();
                i_error_messages.add(ex1.getMessage());
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
                        "elena_taycher@hms.harvard.edu", "Request for end reads evaluation: error messages.", "Errors\n Processing of requested for the following plates:\n"+requested_plates ,i_error_messages);
                
                    }
                }
                    catch(Exception e){}
                DatabaseTransaction.closeConnection(conn);
            }
        
    }
        
        private ArrayList getConstructs(int container_id) throws BecDatabaseException
        {
            ArrayList constructs = new ArrayList();
             int[] sequence_analysis_status = {
                BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED ,
                BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_YES_DISCREPANCIES,
                BaseSequence.CLONE_SEQUENCE_STATUS_ANALIZED_NO_DISCREPANCIES ,
                BaseSequence.CLONE_SEQUENCE_STATUS_POLYMORPHISM_CLEARED ,
                 };
            String clone_sequence_analysis_status = Algorithms.convertArrayToString(sequence_analysis_status, ",");
            int[] sequence_type = {BaseSequence.CLONE_SEQUENCE_TYPE_ASSEMBLED };
            String clone_sequence_type = Algorithms.convertArrayToString(sequence_type, ",");
         
            constructs = Construct.getConstructsFromPlate(container_id,clone_sequence_analysis_status,clone_sequence_type,1);
            return constructs;
        }
}

