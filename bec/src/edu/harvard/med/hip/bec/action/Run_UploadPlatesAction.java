/*
 * Run_UploadPlatesAction.java
 *
 * Created on May 27, 2003, 10:47 AM
 */

package edu.harvard.med.hip.bec.action;

/**
 *
 * @author  htaycher
 */
import java.util.*;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.util.*;
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

public class Run_UploadPlatesAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException
    {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        // The form holding the status changes made by the user
        String  container_labels = (String) request.getAttribute("plate_names");//get from form
        int     vectorid = ((Integer) request.getAttribute(Constants.VECTOR_ID_KEY)).intValue();//get from form
        int     linker3id = ((Integer) request.getAttribute("3LINKER")).intValue();//get from form
        int     linker5id = ((Integer) request.getAttribute("5LINKER")).intValue();//get from form
        int     put_plate_for_step = ((Integer) request.getAttribute("nextstep")).intValue(); //get from form 
        
        //parse plate names
        ArrayList master_container_labels = Algorithms.splitString(container_labels);
        
        ActionRunner runner = new ActionRunner();
        runner.setContainerLabels(master_container_labels );
        runner.setVectorId(vectorid );
        runner.setLinker3Id(linker3id);
        runner.setLinker5Id(linker5id);
        runner.setNextStep(put_plate_for_step);
        runner.setPlateInfoType(PlateUploader.PLATE_NAMES);
        runner.setUser(user);
        Thread t = new Thread(runner);
        t.start();
        return mapping.findForward("proccessing");
    
    }
    
    class ActionRunner implements Runnable
    {
       private ArrayList   i_master_container_labels = null;//get from form
        private int         i_vector_id = -1;//get from form
        private int         i_linker3_id = -1;
        private int         i_linker5_id = -1;
        private int         i_isolate_status = -1;
        private User        i_user = null;
        private int         i_plate_info_type = -1;
        private ArrayList   i_error_messages = null;


        public void         setContainerLabels(ArrayList v)    { i_master_container_labels = v;}
        public void         setVectorId(int vectorid )    { i_vector_id = vectorid;}
        public void         setLinker3Id(int linker3id)    { i_linker3_id = linker3id;}
        public void         setLinker5Id(int linker5id)    { i_linker5_id = linker5id;}
        public void         setNextStep(int put_plate_for_step)    { i_isolate_status = put_plate_for_step;}
        public void         setPlateInfoType(int plate_info_type){i_plate_info_type = PlateUploader.PLATE_NAMES;}
        public  void        setUser(User v)    {i_user=v;}

        
        
        
        public ActionRunner()
        {
            i_error_messages = new ArrayList();
        }
      
         
        public void run()
    {
        // The database connection used for the transaction
        Connection conn = null;
        ArrayList master_plates = new ArrayList();
        i_error_messages = new ArrayList();
        String requested_plates = Algorithms.convertStringArrayToString(i_master_container_labels,",");
        PlateUploader pb = null;
        try
        {
          
            
          
            // conncection to use for transactions
            conn = DatabaseTransaction.getInstance().requestConnection();
            //request object
            
          
            int cloning_startegy_id ;
           //get clonningstategy, if it does not exist - create new
         cloning_startegy_id = CloningStrategy.getCloningStrategyIdByVectorLinkerInfo( i_vector_id , i_linker3_id ,  i_linker5_id );

            if (cloning_startegy_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET )
            {
                CloningStrategy str = new CloningStrategy(BecIDGenerator.BEC_OBJECT_ID_NOTSET ,i_vector_id , i_linker3_id ,  i_linker5_id ," ");
                str.insert(conn);
                conn.commit();
                cloning_startegy_id = str.getId();
            }
          System.out.print(cloning_startegy_id);
           System.out.print(i_plate_info_type);
            System.out.print(i_isolate_status);
            for (int c=0;c<i_master_container_labels.size();c++)
            {
          System.out.print((String)i_master_container_labels.get(c));
            }
            return;
            /*
              //upload plates
            pb = new PlateUploader( i_master_container_labels, i_plate_info_type, cloning_startegy_id, i_isolate_status);
            pb.upload(conn);
            i_error_messages.addAll(pb.getErrors());
            
            //if at least one plate was uploaded create request
            if (pb.getContainerIds().size() > 0)
            {
             //insert process_object_records
                  ArrayList processes = new ArrayList();
                Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                            new java.util.Date(),
                                            i_user.getId(),
                                            processes,
                                            Constants.TYPE_OBJECTS);
                  //create specs array for the process
                ArrayList specids = new ArrayList();
                specids.add(new Integer(cloning_startegy_id));
              //  specids.add(new Integer(i_reverse_primerid));
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
            */
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            
        }
         
        finally
        {
            try
            {
                DatabaseTransaction.closeConnection(conn);
                System.out.println(i_error_messages.size()+" l");
                //send errors
                if (i_error_messages.size() != 0)
                {
                    System.out.println(i_error_messages.size()+" l1");
                    Mailer.sendMessage(i_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                    "elena_taycher@hms.harvard.edu", "Upload plates: error messages.",
                    "Errors\n Processing of requested for the following plates:\n"+requested_plates +"\n"+
                    Algorithms.convertStringArrayToString(i_error_messages,"\n"));
                }
                if (pb.getPassPlateNames().size()!=0)
                {
                    System.out.println(pb.getPassPlateNames().size()+" l3");
                     Mailer.sendMessage(i_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                    "elena_taycher@hms.harvard.edu", "Uploaded plates.",
                    "\n Processing of requested for the following plates:\n"+requested_plates +"\n The follwing plates have been sucessfully uploaded from FLEX into BEC"+
                    Algorithms.convertStringArrayToString(pb.getPassPlateNames(),"\n"));
                }
                 if (pb.getFailedPlateNames().size() != 0)
                 {
                    Mailer.sendMessage(i_user.getUserEmail(), "elena_taycher@hms.harvard.edu",
                    "elena_taycher@hms.harvard.edu", "Failed Plates.",
                    "\n Processing of requested for the following plates:\n"+requested_plates +"\nFailed plates\n"+
                    Algorithms.convertStringArrayToString(pb.getFailedPlateNames(),"\n"));
                 }

            }
            catch(Exception e){System.out.println(e.getMessage());}
        }
            
            
            
        
    }
    
   
    }
    
    
}

