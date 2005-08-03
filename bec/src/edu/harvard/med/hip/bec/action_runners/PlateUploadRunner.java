/*
 * PlateUploaderRunner.java
 *
 * Created on August 27, 2003, 11:09 AM
 */

package edu.harvard.med.hip.bec.action_runners;

/**
 *
 * @author  HTaycher
 */



import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
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
import java.util.*;
/**
 *
 * @author  htaycher
 */
public class PlateUploadRunner implements Runnable
{

    /** Creates a new instance of tester */
    public PlateUploadRunner()
    {
        m_error_messages = new ArrayList();
    }


    private ArrayList   m_master_container_labels = null;//get from form
    private int         m_vector_id = -1;//get from form
    private int         m_linker3_id = -1;
    private int         m_linker5_id = -1;
    private int         m_isolate_status = -1;
    private User        m_user = null;
    private int         m_plate_info_type = -1;
    private ArrayList   m_error_messages = null;
      private String     m_start_codon = null;
    private String      m_fusion_stop_codon = null;
    private String      m_close_stop_codon = null;

    public void         setContainerLabels(ArrayList v)    { m_master_container_labels = v;}
    public void         setVectorId(int vectorid )    { m_vector_id = vectorid;}
    public void         setLinker3Id(int linker3id)    { m_linker3_id = linker3id;}
    public void         setLinker5Id(int linker5id)    { m_linker5_id = linker5id;}
    public void         setNextStep(int put_plate_for_step)    { m_isolate_status = put_plate_for_step;}
    public void         setPlateInfoType(int plate_info_type){m_plate_info_type = PlateUploader.PLATE_NAMES;}
    public  void        setUser(User v)    {m_user=v;}
     public  void        setStartCodon(String v){m_start_codon = v;}
    public  void        setFusionStopCodon(String v){m_fusion_stop_codon = v;}
    public  void        setClosedStopCodon(String v){m_close_stop_codon = v;}
    
    public void run()
    {
       
        // The database connection used for the transaction
        Connection conn = null;
        ArrayList master_plates = new ArrayList();
        m_error_messages = new ArrayList();
         String requested_plates = Algorithms.convertStringArrayToString(m_master_container_labels,",");
        PlateUploader pb = null;
        try
        {
            // conncection to use for transactions
            conn = DatabaseTransaction.getInstance().requestConnection();
            //request object
            int cloning_startegy_id ;
           //get clonningstategy, if it does not exist - create new
         cloning_startegy_id = CloningStrategy.getCloningStrategyIdByVectorLinkerInfo( m_vector_id , m_linker3_id ,  m_linker5_id,m_start_codon ,m_fusion_stop_codon ,m_close_stop_codon );
             if (cloning_startegy_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET )
            {
                CloningStrategy str = new CloningStrategy(BecIDGenerator.BEC_OBJECT_ID_NOTSET ,m_vector_id , m_linker3_id ,  m_linker5_id ,m_start_codon ,m_fusion_stop_codon ,m_close_stop_codon," ");
                str.insert(conn);
                conn.commit();
                cloning_startegy_id = str.getId();
            }


              //upload plates
            pb = new PlateUploader( m_master_container_labels, m_plate_info_type, cloning_startegy_id, m_isolate_status);
            pb.upload(conn);
            m_error_messages.addAll(pb.getErrors());
            //if at least one plate was uploaded create request
            if (pb.getContainerIds().size() > 0)
            {
             //insert process_object_records
                  ArrayList processes = new ArrayList();
                Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                            new java.util.Date(),
                                            m_user.getId(),
                                            processes,
                                            Constants.TYPE_OBJECTS);
                  //create specs array for the process
                ArrayList specids = new ArrayList();
                specids.add(new Integer(cloning_startegy_id));
              //  specids.add(new Integer(m_reverse_primerid));
                // Process object create
                ProcessExecution process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                                ProcessDefinition.ProcessIdFromProcessName(ProcessDefinition.RUN_UPLOAD_PLATE),
                                                actionrequest.getId(),
                                                specids,
                                                Constants.TYPE_ID) ;
                processes.add(process);
                 //finally we must insert request
                actionrequest.insert(conn);
                //patch here
                process.insertConnectorToSpec(conn, Spec.CLONINGSTRATEGY_SPEC_INT);

                String sql = "";
                Statement stmt = conn.createStatement();
                for (int count = 0; count  < pb.getContainerIds().size(); count++)
                {
                    sql = "insert into process_object (processid,objectid,objecttype) values("+process.getId()+","+((Integer)pb.getContainerIds().get(count)).intValue()+","+Constants.PROCESS_OBJECT_TYPE_CONTAINER+")";
                    stmt.executeUpdate(sql);
                }

                conn.commit();
            }

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            DatabaseTransaction.rollback(conn);

        }

        finally
        {
            try
            {
                DatabaseTransaction.closeConnection(conn);
             //   System.out.println(m_error_messages.size()+" l");
                //send errors
                if (m_error_messages.size() != 0)
                {
                  //  System.out.println(m_error_messages.size()+" l1");
                    Mailer.sendMessage(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                    "hip_informatics@hms.harvard.edu", "Upload plates: error messages.",
                    "Errors"+Constants.LINE_SEPARATOR+"Processing of requested for the following plates:"+Constants.LINE_SEPARATOR+requested_plates +Constants.LINE_SEPARATOR+
                    Algorithms.convertStringArrayToString(m_error_messages,"\n"));
                }
                if (pb.getPassPlateNames().size()!=0)
                {
                  //  System.out.println(pb.getPassPlateNames().size()+" l3");
                     Mailer.sendMessage(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                    "hip_informatics@hms.harvard.edu", "Uploaded plates.",
                    Constants.LINE_SEPARATOR+"Processing of requested for the following plates:"+Constants.LINE_SEPARATOR+requested_plates +Constants.LINE_SEPARATOR+"The follwing plates have been sucessfully uploaded from FLEX into BEC: "+
                    Algorithms.convertStringArrayToString(pb.getPassPlateNames(), Constants.LINE_SEPARATOR));
                }
                 if (pb.getFailedPlateNames().size() != 0)
                 {
                    Mailer.sendMessage(m_user.getUserEmail(), "hip_informatics@hms.harvard.edu",
                    "hip_informatics@hms.harvard.edu", "Failed Plates.",
                    "Processing of requested for the following plates:"+Constants.LINE_SEPARATOR+requested_plates +Constants.LINE_SEPARATOR+"Failed plates"+Constants.LINE_SEPARATOR
                    + Algorithms.convertStringArrayToString(pb.getFailedPlateNames(),Constants.LINE_SEPARATOR));
                 }

            }
            catch(Exception e){System.out.println(e.getMessage());}
        }

    }

    
    
    public static void main(String args[]) 
{
    PlateUploadRunner runner = new PlateUploadRunner();
      try
    {
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
       edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
    ArrayList arr = new ArrayList(); arr.add("SAE000946");
         runner.setUser( AccessManager.getInstance().getUser("tigr","tigr"));
                 runner.setContainerLabels(arr );
                    runner.setVectorId(3 );
                    runner.setLinker3Id(2);
                    runner.setLinker5Id(1);
                    runner.setStartCodon("ATG");
                    runner.setFusionStopCodon("TAA");
                    runner.setClosedStopCodon("TAG");
                    runner.setPlateInfoType(PlateUploader.PLATE_NAMES);
                    runner.run();
              
    }
      catch(Exception e){}
    }
 }
