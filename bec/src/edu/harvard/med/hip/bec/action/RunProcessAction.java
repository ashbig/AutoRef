/*
 * RunProcess.java
 *
 * Created on August 26, 2003, 3:27 PM
 */

package edu.harvard.med.hip.bec.action;

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
import edu.harvard.med.hip.bec.action_runners.*;
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

/**
 *
 * @author  HTaycher
 */
public class RunProcessAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException
    {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        Thread t = null;
         int forwardName = ((Seq_GetSpecForm)form).getForwardName();
         Connection conn = null;
        try
        {
            
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
        
    
        switch (forwardName)
        {
           case Constants.PROCESS_UPLOAD_PLATES : //upload plates
            {
                 String  container_labels = (String) request.getAttribute("plate_names");//get from form
                int     vectorid = ((Integer) request.getAttribute(Constants.VECTOR_ID_KEY)).intValue();//get from form
                int     linker3id = ((Integer) request.getAttribute("3LINKER")).intValue();//get from form
                int     linker5id = ((Integer) request.getAttribute("5LINKER")).intValue();//get from form
                int     put_plate_for_step = ((Integer) request.getAttribute("nextstep")).intValue(); //get from form 
                System.out.println(container_labels);
                System.out.println(vectorid);
                System.out.println(linker3id);
                System.out.println(linker5id);
                System.out.println(put_plate_for_step);
                /*
                //parse plate names
                ArrayList master_container_labels = Algorithms.splitString(container_labels);

                PlateUploaderRunner runner = new PlateUploaderRunner();
                runner.setContainerLabels(master_container_labels );
                runner.setVectorId(vectorid );
                runner.setLinker3Id(linker3id);
                runner.setLinker5Id(linker5id);
                runner.setNextStep(put_plate_for_step);
                runner.setPlateInfoType(PlateUploader.PLATE_NAMES);
                runner.setUser(user);
                t = new Thread(runner);
                t.start();
                 **/
                request.setAttribute(Constants.JSP_TITLE,"processing Request for Plates Upload");
                request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates:\n"+container_labels);
                break;
           }

            case Constants.PROCESS_RUN_END_READS : //run sequencing for end reads
            {/*
                 ArrayList master_container_ids = new ArrayList();
    
                  master_container_ids.add(new Integer(96));

                tester_request_endreads runner = new tester_request_endreads();
                runner.setContainerIds(master_container_ids );
                runner.setForwardPrimerId( 5 );
                runner.setRevercePrimerId(6);
                runner.setUser(user);
                t = new Thread();           t.start();
                break;
              **/
            }

            case Constants.PROCESS_RUN_ISOLATE_RUNKER : //run isolate runker
            {
            }

            

            case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:
            {
            }
            case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER : // add new internal primer
            {
            }

            case Constants.PROCESS_VIEW_INTERNAL_PRIMERS : //view internal primers
            {
            }

            case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS : //approve internal primers
            {
            }

            case Constants.PROCESS_RUN_PRIMER3: //run primer3
            {
            }

            case Constants.PROCESS_RUNPOLYMORPHISM_FINDER: //run polymorphism finder
            {
            }

            case Constants.PROCESS_RUN_DISCREPANCY_FINDER: //run discrepancy finder
            {
            }

            case Constants.PROCESS_RUN_DESIGION_TOOL : //run decision tool
            {
        }


            case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE : //run decision tool
            {
            }
            case Constants.PROCESS_ACTIVATE_CLONES :
            case Constants.PROCESS_PUT_CLONES_ON_HOLD :  //put clones on hold
            {
                String chkStr[] = request.getParameterValues("chkClone");
                int istr_id = -1; String sql = null;
                if (chkStr != null) 
                {
                      for (int i = 0; i < chkStr.length; i++) 
                      {
                         istr_id = Integer.parseInt(chkStr[i]);
                         sql = "update isolatetracking set status=-status where isolatetrackingid="+ istr_id;
                         DatabaseTransaction.executeUpdate(sql, conn);
                         
                      }
                } 
                request.setAttribute(Constants.JSP_TITLE,"Request for clones status change" );
                request.setAttribute(Constants.ADDITIONAL_JSP,"For " +chkStr.length+ " clones from plate "+ request.getAttribute("containeLabel") +" status have been changes" );
                return mapping.findForward("proccessing");
            }

             
            

            case             Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE : 
            {
            }



        }
       
        return mapping.findForward("proccessing");
        }
        catch (Exception e)
        {
             try 
             {
                conn.rollback();
                System.out.println(e.getMessage());
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
             } catch (Exception e1) {           
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            } 
        }
        finally
        {
            if(conn != null)
                DatabaseTransaction.closeConnection(conn);
        }
    }
}
