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
import edu.harvard.med.hip.bec.coreobjects.spec.*;
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
         
         ArrayList master_container_ids = null;
         ArrayList master_container_labels = null;
        try
        {
            
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            DatabaseTransaction dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
        
    
        switch (forwardName)
        {
           case Constants.PROCESS_UPLOAD_PLATES : //upload plates
            {
            
                 String  container_labels = (String) request.getParameter("plate_names");//get from form
                  String     start_codon =  (String)request.getParameter("start_codon");//get from form
                  String     fusion_stop_codon =  (String)request.getParameter("fusion_stop_codon");//get from form
                   String    closed_stop_codon =  (String)request.getParameter("closed_stop_codon");//get from form
                  
                  int     vectorid = Integer.parseInt( (String)request.getParameter(Constants.VECTOR_ID_KEY));//get from form
                 int     linker3id = Integer.parseInt( (String) request.getParameter("3LINKERID"));//get from form
                 int     linker5id = Integer.parseInt( (String)request.getParameter("5LINKERID"));//get from form
                 int     put_plate_for_step = Integer.parseInt( (String) request.getParameter("nextstep")); //get from form 
                 
                 //validate input
                if (container_labels == null || container_labels.trim().equals("") )
                {
                    errors.add(ActionErrors.GLOBAL_ERROR,  new ActionError("error.container.querry.parameter", "Please enter plate labels"));
                    saveErrors(request,errors);
                    return new ActionForward(mapping.getInput());
                }
                 
                 request.setAttribute(Constants.JSP_TITLE,"processing Request for Plates Upload");
                request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates:\n"+container_labels);
            
                //parse plate names
                 master_container_labels = Algorithms.splitString(container_labels.toUpperCase());
               

                PlateUploadRunner runner = new PlateUploadRunner();
                runner.setContainerLabels(master_container_labels );
                runner.setVectorId(vectorid );
                runner.setLinker3Id(linker3id);
                runner.setLinker5Id(linker5id);
                runner.setStartCodon(start_codon);
                runner.setFusionStopCodon(fusion_stop_codon);
                runner.setClosedStopCodon(closed_stop_codon);
                runner.setNextStep(put_plate_for_step);
                runner.setPlateInfoType(PlateUploader.PLATE_NAMES);
                runner.setUser(user);
                t = new Thread(runner);
                t.start();
                 
              
                break;
           }

            case Constants.PROCESS_RUN_END_READS : //run sequencing for end reads
            {
                  master_container_ids = new ArrayList();
                   master_container_labels = new ArrayList();
                 String[] labels = request.getParameterValues("chkLabel");
                    String plate_names = "";
                if(labels != null) 
                {
                  for (int i = 0; i < labels.length; i ++)
                  {
                    master_container_labels.add(labels[i] );
                    plate_names += labels[i] + "\n";
                  }  
                }
                 master_container_ids = Container.findContainerIdsFromLabel(master_container_labels);
                
                 
                 int forward_primer_id = Integer.parseInt( (String) request.getParameter("5p_primerid"));
                int reverse_primer_id = Integer.parseInt( (String) request.getParameter("3p_primerid"));
              
                request.setAttribute(Constants.JSP_TITLE,"Request for end read sequencing request" );
                request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates "+ plate_names);
                
                EndReadsRequestRunner runner = new EndReadsRequestRunner();
                runner.setContainerIds(master_container_ids );
                runner.setForwardPrimerId( forward_primer_id );
                runner.setRevercePrimerId(reverse_primer_id);
                runner.setUser(user);
                t = new Thread();           t.start();
 
                return mapping.findForward("processing");
              
            }

            case Constants.PROCESS_RUN_ISOLATE_RUNKER : //run isolate runker
            {
                 master_container_labels = new ArrayList();
                 String[] labels = request.getParameterValues("chkLabel");
                    String plate_names = "";
                if(labels != null) 
                {
                  for (int i = 0; i < labels.length; i ++)
                  {
                    master_container_labels.add(labels[i] );
                    plate_names += labels[i] + "\n";
                  }  
                }

                master_container_ids = Container.findContainerIdsFromLabel(master_container_labels);
                request.setAttribute(Constants.JSP_TITLE,"Request for isolate ranker run" );
                request.setAttribute(Constants.ADDITIONAL_JSP,"Processing plates "+ plate_names);
    
                int bioeval_spec_id = Integer.parseInt( (String) request.getParameter(Spec.FULL_SEQ_SPEC));
                int endread_spec_id = Integer.parseInt( (String) request.getParameter(Spec.END_READS_SPEC));
                int polymorphism_spec_id = Integer.parseInt( (String) request.getParameter(Spec.POLYMORPHISM_SPEC));
 
                    /*
        * 
                IsolateRankerRunner runner = new IsolateRankerRunner();
                runner.setContainerIds(master_container_ids );
                runner.setCutoffValuesSpec( (FullSeqSpec)Spec.getSpecById(bioeval_spec_id, Spec.FULL_SEQ_SPEC_INT));
                runner.setPenaltyValuesSpec( (EndReadsSpec)Spec.getSpecById(endread_spec_id, Spec.END_READS_SPEC_INT));
                if (polymorphism_spec_id != -1)
                    runner.setPolymorphismSpec((PolymorphismSpec)Spec.getSpecById(polymorphism_spec_id, Spec.POLYMORPHISM_SPEC_INT));
                runner.setUser(user);
                t = new Thread();           t.start();
                 */
                break;
            }
            case Constants.PROCESS_APROVE_ISOLATE_RANKER:
            {
                
                int clone_number = 0;
                int isolate_id = -1;
                Enumeration e = request.getParameterNames(); 
                while (e.hasMoreElements()) 
                { 
                    String param_name = (String)e.nextElement(); 
          
                    if ( !param_name.equalsIgnoreCase("forwardName") )
                    {
                        int rank = Integer.parseInt( (String) request.getParameter(param_name)); 
                        isolate_id = Integer.parseInt(param_name);
                        clone_number++;
                        IsolateTrackingEngine.updateRankUserChangerId(rank,user.getId(),isolate_id,conn);
                    }
                } 
                request.setAttribute(Constants.JSP_TITLE,"request for change of clone ranking is in process" );
                request.setAttribute(Constants.ADDITIONAL_JSP,"Processing "+clone_number +"  clones");
                break;
            }
            case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:
            {
            }
            case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER : // add new internal primer
            {
            }
/*
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
*/
            case Constants.PROCESS_RUN_DECISION_TOOL : //run decision tool
            {
                //get spec
                 int bioeval_spec_id = Integer.parseInt( (String) request.getParameter(Spec.FULL_SEQ_SPEC));
                //get plate label or clonids
                String data_type =  (String) request.getParameter("data_type");
                String plate_name = null;String  clone_ids = null;
              
                if ( data_type.equalsIgnoreCase("PLATE"))
                {
                    plate_name = (String) request.getParameter("plate_name");
                }
                else if ( data_type.equalsIgnoreCase("CLONE"))
                {
                     clone_ids = (String) request.getParameter("clone_collection");
                }
                if ( (plate_name == null ||  plate_name.trim().equals("")  ) &&
                    (clone_ids == null || clone_ids.trim().equals("")) )
                {
                    errors.add(ActionErrors.GLOBAL_ERROR,  new ActionError("error.container.querry.parameter", "Please enter plate labels"));
                    saveErrors(request,errors);
                    return (mapping.findForward("error"));
                }
                 
                //run 
                DesicionTool ds = new DesicionTool();
                ds.setSpecId(bioeval_spec_id);
                if ( data_type.equalsIgnoreCase("PLATE"))
                {
                    ds.setPlateName(plate_name);
                }
                else if ( data_type.equalsIgnoreCase("CLONE"))
                {
                     ds.setCLoneIds( Algorithms.splitString(clone_ids) );
                }
                ds.run();
                ArrayList clone_data = ds.getCloneData();
                request.setAttribute(Constants.JSP_TITLE,"report Clone Sequence Qualities" );
                request.setAttribute("clone_data",clone_data );
                return mapping.findForward("desicion_tool_report");
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
                request.setAttribute(Constants.ADDITIONAL_JSP,"For " +chkStr.length+ " clones from plate "+ request.getParameter("containerLabel") +" status have been changes" );
                return mapping.findForward("processing");
            }

             
            

            case             Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE : 
            {
            }



        }
       
        return mapping.findForward("processing");
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
