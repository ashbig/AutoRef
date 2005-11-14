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
public class PlateUploadRunner extends ProcessRunner
{



  //  private ArrayList   m_master_container_labels = null;//get from form
    private int         m_vector_id = -1;//get from form
    private int         m_linker3_id = -1;
    private int         m_linker5_id = -1;
    private int         m_isolate_status = -1;
    private int         m_plate_info_type = -1;
    private ArrayList   m_error_messages = null;
    private String     m_start_codon = null;
    private String      m_fusion_stop_codon = null;
    private String      m_close_stop_codon = null;

  //  public void         setContainerLabels(ArrayList v)    { m_master_container_labels = v;}
    public void         setVectorId(int vectorid )    { m_vector_id = vectorid;}
    public void         setLinker3Id(int linker3id)    { m_linker3_id = linker3id;}
    public void         setLinker5Id(int linker5id)    { m_linker5_id = linker5id;}
    public void         setNextStep(int put_plate_for_step)    { m_isolate_status = put_plate_for_step;}
    public void         setPlateInfoType(int plate_info_type){m_plate_info_type = PlateUploader.PLATE_NAMES;}
     public  void        setStartCodon(String v){m_start_codon = v;}
    public  void        setFusionStopCodon(String v){m_fusion_stop_codon = v;}
    public  void        setClosedStopCodon(String v){m_close_stop_codon = v;}
     public String          getTitle() {return "Request for plates upload";    }


     public void run_process()
    {

        // The database connection used for the transaction
        Connection conn = null;
        ArrayList master_plates = new ArrayList();
        m_error_messages = new ArrayList();
        ArrayList master_container_labels = Algorithms.splitString(m_items);
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
            pb = new PlateUploader( master_container_labels, m_plate_info_type, cloning_startegy_id, m_isolate_status);
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
             if (pb.getPassPlateNames().size()!=0)
            {
                m_additional_info ="\nThe follwing plates have been sucessfully uploaded from FLEX into BEC: "+
                Algorithms.convertStringArrayToString(pb.getPassPlateNames(), Constants.LINE_SEPARATOR);
            }
             if (pb.getFailedPlateNames().size() != 0)
             {
                 m_additional_info ="\nFailed plates"+Constants.LINE_SEPARATOR
                + Algorithms.convertStringArrayToString(pb.getFailedPlateNames(),Constants.LINE_SEPARATOR);
             }

        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);

        }

        finally
        {
            if ( conn != null )DatabaseTransaction.closeConnection(conn);
            sendEMails(getTitle());

        }

    }



    public static void main(String args[])
{
    ProcessRunner runner = new PlateUploadRunner();
      try
    {
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
       edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadDefinitionsFromDatabase();
         runner.setUser( AccessManager.getInstance().getUser("htaycher123","htaycher"));
                 runner.setInputData(Constants.ITEM_TYPE_PLATE_LABELS, "VcxXG002290-2.012-1 ");
                    ((PlateUploadRunner)runner).setVectorId(9 );
                     ((PlateUploadRunner)runner).setLinker3Id(74);
                     ((PlateUploadRunner)runner).setLinker5Id(73);
                     ((PlateUploadRunner)runner).setStartCodon("ATG");
                     ((PlateUploadRunner)runner).setFusionStopCodon("TAA");
                     ((PlateUploadRunner)runner).setClosedStopCodon("TAG");
                     ((PlateUploadRunner)runner).setPlateInfoType(PlateUploader.PLATE_NAMES);
                     runner.setProcessType(Constants.PROCESS_UPLOAD_PLATES);
                    runner.run();

    }
      catch(Exception e){}
    }



 }
