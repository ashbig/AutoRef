/*
 * SequenceDataSubmitter.java
 *
 * Created on September 29, 2003, 4:14 PM
 */

package edu.harvard.med.hip.bec.action_runners;

import java.sql.*;
import java.io.*;
import java.util.*;


import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.assembler.*;
import edu.harvard.med.hip.bec.modules.*;
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
import edu.harvard.med.hip.bec.util_objects.*;
/**
 *
 * @author  HTaycher
 */
public class SequenceDataUploadRunner implements Runnable
{

        private InputStream     m_input = null;
        private User            m_user=null;
        private ArrayList       m_report = null;
        private Hashtable       m_cloning_starategy = null;

        public SequenceDataUploadRunner(InputStream in, User user)
        {
            m_input = in;
            m_user = user;
            m_report = new ArrayList();
            m_cloning_starategy= new Hashtable();
        }



        public void run()
        {
               // The database connection used for the transaction
            Connection conn = null;

            BufferedReader reader = null;
            StringBuffer sample_id = new StringBuffer();
            StringBuffer sequence_text = new StringBuffer();
            String line = null;
            char cur_char ;
            try
            {
                // conncection to use for transactions
                conn = DatabaseTransaction.getInstance().requestConnection();
                int process_id = createProcessHistory(conn);
                reader = new BufferedReader(new InputStreamReader(m_input));

                 while  ((line = reader.readLine()) != null)
                {
                    if (line.trim().startsWith(">") )
                    {
                        //process previous sequence
                        processSequence(sequence_text.toString(), sample_id.toString(), process_id,conn);
                        sequence_text = new StringBuffer();
                        sample_id = new StringBuffer();
                        //extract sample id
                        for (int index = 1; index < line.length(); index++)
                        {
                            cur_char = line.charAt(index);
                            if (Character.isDigit(cur_char) )
                                sample_id.append( cur_char);
                            else
                               break;
                        }

                    }
                    else
                    {
                        line = line.trim();
                        sequence_text.append( Algorithms.cleanWhiteSpaces(line) );
                     }

                }
                reader.close();



            }
            catch(Exception e)
            {
               // System.out.println(e.getMessage());
                DatabaseTransaction.rollback(conn);

            }

            finally
            {
                try
                {
                    if ( conn != null) DatabaseTransaction.closeConnection(conn);

                    Mailer.sendMessage(m_user.getUserEmail(),  BecProperties.getInstance().getACEEmailAddress(),
                     BecProperties.getInstance().getProperty("ACE_CC_EMAIL_ADDRESS"), "Submit sequence data.",
                    "Sequence data submission report:\n"+
                    Algorithms.convertStringArrayToString(m_report,"\n"));
                }
                catch(Exception e){//System.out.println(e.getMessage());
                }
            }
        }


    //***********************************
        private synchronized  void processSequence(String sequence_data, String flex_sample_id,
                        int process_id, Connection conn)

        {
            if (sequence_data == null || sequence_data.trim().length()==0)
                return ;

            Contig contig = null;
            int contig_quality = -1;
            CloneDescription sequence_definition = null;
            int return_value = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
             try
             {
                 //get sequence description based on flex sample id
                 sequence_definition = getSequenceDescription(flex_sample_id);

                 if (sequence_definition == null )
                 {
                     m_report.add("Cannot submit data for clone with sampleid "+flex_sample_id);
                    return;
                     //throw new BecDatabaseException("Cannot submit data for clobne with sampleid "+flex_sample_id);
                 }
                  if (sequence_definition != null && sequence_definition.getCloneId() == 0)
                 {
                     m_report.add("Cannot submit data for clone with sampleid "+flex_sample_id+".Sample is not submitted for sequencing");
                    return;
                     //throw new BecDatabaseException("Cannot submit data for clobne with sampleid "+flex_sample_id);
                 }
                 //create contig
                 contig = new Contig();
                 contig.setSequence(sequence_data.toUpperCase());

                 //analize sequence
                 contig_quality = analizeContig(contig,sequence_definition);
                 //insert result && sequence
                return_value = CloneSequence.insertSequenceWithResult(
                          sequence_definition.getSampleId(),
                         sequence_definition.getIsolateTrackingId(),
                         sequence_definition.getBecRefSequenceId(),
                         Result.RESULT_TYPE_FINAL_CLONE_SEQUENCE,
                         BaseSequence.CLONE_SEQUENCE_STATUS_ASSEMBLED,
                         BaseSequence.CLONE_SEQUENCE_TYPE_FINAL, contig, process_id, conn);

            //update isolatetracking
                 IsolateTrackingEngine.updateStatus(IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_SUBMITED_FROM_OUTSIDE,
                                                       sequence_definition.getIsolateTrackingId(),
                                                       conn );
                 IsolateTrackingEngine.updateAssemblyStatus(
                                        contig_quality,
                                        sequence_definition.getIsolateTrackingId(),
                                        conn);
                conn.commit();
                  m_report.add("Data submitted for with sampleid "+flex_sample_id + " (sequence id " + return_value +")" );
             }
             catch (Exception e)
             {
                 m_report.add("Cannot submit data for clobne with sampleid "+flex_sample_id);
                // throw new BecDatabaseException("Cannot submit data for clobne with sampleid "+flex_sample_id);
             }

        }

  //gets clone description based on flex sample id
  private CloneDescription getSequenceDescription(String flex_sample_id)throws BecDatabaseException
  {
      CloneDescription seq_description = null;
      String sql = "select flexcloneid, flexsequenceid, iso.status as process_status,  refsequenceid,iso.isolatetrackingid as isolatetrackingid , "
+" containerid, s.sampleid as sampleid from isolatetracking iso,  sample s, sequencingconstruct  constr , flexinfo f "
+" where        iso.process_status="+IsolateTrackingEngine.FINAL_STATUS_INPROCESS+" and   constr.constructid = iso.constructid and "
+" iso.sampleid=s.sampleid and f.isolatetrackingid=iso.isolatetrackingid "
+" and flexsampleid = "+flex_sample_id;
      ResultSet rs = null;ResultSet rs_ref = null;String sql_sample = null;

        try
        {
           // DatabaseTransactionLocal t = DatabaseTransactionLocal.getInstance();
            rs = DatabaseTransaction.getInstance().executeQuery(sql);

            if(rs.next())
            {
                if ( rs.getInt("process_status") != IsolateTrackingEngine.PROCESS_STATUS_SUBMITTED_FOR_SEQUENCE_ANALYSIS)
                    return null;
                seq_description = new CloneDescription();

                seq_description.setBecRefSequenceId(rs.getInt("refsequenceID"));
                seq_description.setIsolateTrackingId( rs.getInt("isolatetrackingid"));
                seq_description.setContainerId(rs.getInt("containerid"));
                seq_description.setSampleId(rs.getInt("sampleid"));
                seq_description.setFlexSequenceId(rs.getInt( "flexsequenceid"));
                seq_description.setCloneId(  rs.getInt("flexcloneid"));
            }
           return seq_description;
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while getting clone inforamation for sample id "+ flex_sample_id +" \nSQL: "+sqlE.getMessage());
        } finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
        }

  }

  private int analizeContig(Contig contig,CloneDescription clone_definition)throws Exception
  {
       CloningStrategy container_cloning_strategy = null;
       container_cloning_strategy =(CloningStrategy) m_cloning_starategy.get(new Integer(clone_definition.getContainerId()));
       if ( container_cloning_strategy == null )
        {
            container_cloning_strategy = Container.getCloningStrategy(clone_definition.getContainerId());
            if (container_cloning_strategy != null)
             {
                 container_cloning_strategy.setLinker3( BioLinker.getLinkerById( container_cloning_strategy.getLinker3Id() ));
                 container_cloning_strategy.setLinker5( BioLinker.getLinkerById( container_cloning_strategy.getLinker5Id() ));
             }
             m_cloning_starategy.put(new Integer(clone_definition.getContainerId()),container_cloning_strategy);
        }

        //get refsequence
       RefSequence refsequence = new RefSequence( clone_definition.getBecRefSequenceId());
       BioLinker linker5 = container_cloning_strategy.getLinker5();
        BioLinker linker3 = container_cloning_strategy.getLinker3();
       int cds_start = linker5.getSequence().length();
        int cds_stop = linker5.getSequence().length() +  refsequence.getCodingSequence().length();
        BaseSequence base_refsequence =  new BaseSequence(linker5.getSequence() + refsequence.getCodingSequence()+linker3.getSequence(), BaseSequence.BASE_SEQUENCE );
        base_refsequence.setId(refsequence.getId());

           //check coverage
           int result = contig.checkForCoverage(clone_definition.getCloneId(), cds_start,  cds_stop,  base_refsequence);
           return result;

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
                                                    ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_SEQUENCE_SUBMISSION),
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


       public static void main(String args[])

    {
       // InputStream input = new InputStream();
        FileInputStream input = null;
        User user  = null;
        try
        {
             input = new FileInputStream("E:\\HTaycher\\Andreas Clontech\\q_sequences.txt");
            user = AccessManager.getInstance().getUser("htaycher1","htaycher");
        }
        catch(Exception e){}
        SequenceDataUploadRunner runner = new SequenceDataUploadRunner(input,user);
       runner.run();


        System.exit(0);
     }
}
