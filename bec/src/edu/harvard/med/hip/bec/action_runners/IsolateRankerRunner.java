/*
 * IsolateRankerRunner.java
 *
 * Created on August 28, 2003, 2:17 PM
 */

package edu.harvard.med.hip.bec.action_runners;
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
 * @author  HTaycher
 */
public class IsolateRankerRunner implements Runnable
{

    /** Creates a new instance of IsolateRankerRunner */
    public IsolateRankerRunner()
    {
         m_error_messages = new ArrayList();
    }

    private ArrayList           m_master_container_ids = null;//get from form
    private boolean             m_isRunPolymorphismFinder = false;//get from form
    private FullSeqSpec         m_fullseq_spec = null;
    private EndReadsSpec        m_endreads_spec = null;
    private PolymorphismSpec    m_polymorphism_spec = null;
    private User                m_user = null;
    private ArrayList           m_error_messages = null;


    public void         setContainerIds(ArrayList v)        { m_master_container_ids = v;}
    public void         setCutoffValuesSpec(FullSeqSpec specs)        {m_fullseq_spec = specs;}
    public void         setPenaltyValuesSpec(EndReadsSpec specs)        {m_endreads_spec = specs;}
    public void         setPolymorphismSpec(PolymorphismSpec specs)        {m_polymorphism_spec = specs; m_isRunPolymorphismFinder = true;}
    public  void        setUser(User v){m_user=v;}

    public void run()
    {
        // The database connection used for the transaction
            Connection conn = null;
            ArrayList master_plates = new ArrayList();
            String requested_plates="";
            int container_id = -1;
             IsolateRanker isolate_ranker = null;
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
               //get construct from db// synchronize block here
                for (int plate_count = 0; plate_count < m_master_container_ids.size(); plate_count++)
                {
                    container_id = ((Integer)m_master_container_ids.get(plate_count)).intValue();
//get common primers used for reads - assumption each plate run with the same primers
// this staff will work only for one pair of end read primers !!!!!!!!!!!!
                    // fix for multipal pairs
                    Oligo[] oligos = Container.findEndReadsOligos(container_id);
                     requested_plates += container_id +"\n";
                    ArrayList constructs = getConstructs(container_id);
                     CloningStrategy container_cloning_strategy = Container.getCloningStrategy(container_id);
                    
                     int linker3_length = 0;   int linker5_length = 0;
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
                    if (m_isRunPolymorphismFinder)
                    {
                        isolate_ranker = new IsolateRanker(m_fullseq_spec,  m_endreads_spec,constructs,  m_polymorphism_spec);
                    }
                    else
                    {
                         isolate_ranker = new IsolateRanker(m_fullseq_spec,  m_endreads_spec,constructs);
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
                }

                m_error_messages.addAll( isolate_ranker.getErrorMessages() );
                     //request object
                if (isolate_ranker.getProcessedConstructId().size() > 0)
                {

                    ArrayList processes = new ArrayList();
                    Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                new java.util.Date(),
                                                m_user.getId(),
                                                processes,
                                                Constants.TYPE_OBJECTS);
// Process object create
                     //create specs array for the process
                    ArrayList specs = new ArrayList();
                    specs.add(m_fullseq_spec);
                    specs.add( m_endreads_spec );
                    if ( m_polymorphism_spec != null)
                    {
                        specs.add(m_polymorphism_spec);
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
                m_error_messages.add(ex1.getMessage());
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
                        "elena_taycher@hms.harvard.edu", "Request for end reads evaluation: error messages.", "Errors\n Processing of requested for the following plates(bec ids):\n"+requested_plates ,m_error_messages);

                    }
                    if (m_error_messages.size()==0)
                    {
                         Mailer.sendMessage(m_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                        "elena_taycher@hms.harvard.edu", "Request for end reads evaluation: error messages.", "\nIsolate Ranker finished request for the following plates(bec ids):\n"+requested_plates );

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
