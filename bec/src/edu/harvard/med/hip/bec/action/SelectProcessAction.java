/*
 * SelectProcessAction.java
 *
 * Created on March 11, 2003, 3:51 PM
 */

package edu.harvard.med.hip.bec.action;

import java.util.*;
import java.text.*;
import java.sql.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.action_runners.*;
import edu.harvard.med.hip.bec.util.*;

/**
 *
 * @author  htaycher
 */
public class SelectProcessAction extends ResearcherAction
{
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward becPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        ActionErrors errors = new ActionErrors();
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
      
        Thread t = null;
        try
        {
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
            request.setAttribute("forwardName", new Integer(forwardName));
            switch  (forwardName)
            {
                case Constants.PROCESS_UPLOAD_PLATES://upload plates
                {
                   
                    ArrayList biolinkers = BioLinker.getAllLinkers();
                    ArrayList vectors = BioVector.getAllVectors();
                   
                    request.setAttribute(Constants.VECTOR_COL_KEY, vectors);
                    request.setAttribute(Constants.LINKER_COL_KEY, biolinkers);
                    return (mapping.findForward("upload_plates"));
                }
                case Constants.PROCESS_SELECT_VECTOR_FOR_END_READS ://allows to select vector fr plates
                {
                    ArrayList vectors = BioVector.getAllVectors();
                    request.setAttribute(Constants.VECTOR_COL_KEY, vectors);
                    request.setAttribute("forwardName", new Integer(Constants.PROCESS_SELECT_PLATES_FOR_END_READS));
                    return (mapping.findForward("select_vector"));
                }
                case Constants.PROCESS_SELECT_PLATES_FOR_END_READS://run sequencing for end reads
                {
                     //get all specs for each of the three types and all plates where
                    int vector_id = Integer.parseInt( (String) request.getParameter(Constants.VECTOR_ID_KEY));
                    ArrayList spec_collection = new ArrayList();
                    ArrayList primers = BioVector.getVectorPrimers(vector_id);
                    ArrayList spec_names = new ArrayList();
                    ArrayList control_names = new ArrayList();
                    
                    if ( primers == null || primers.size() == 0)
                    {
                          errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("error.container.querry.parameter", 
                                "There are no primers assosiated with this vector. \nPlease contact BEC development team."));
                            saveErrors(request,errors);
                             
                            return new ActionForward(mapping.getInput());
                    }
                    spec_collection.add( primers);
                    spec_names.add("5p primer");
                    control_names.add("5p_primerid");
                    spec_collection.add(  primers);
                    spec_names.add("3p primer");
                    control_names.add("3p_primerid");
                    request.setAttribute(Constants.SPEC_COLLECTION, spec_collection);
                    request.setAttribute(Constants.SPEC_TITLE_COLLECTION, spec_names);
                    request.setAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION,control_names);
                    // there are isolates subject to isolateranking
                    ArrayList plateNames = Container.findContainerLabelsForProcess(Constants.PROCESS_SELECT_PLATES_FOR_END_READS,  vector_id);
                    request.setAttribute(Constants.PLATE_NAMES_COLLECTION, plateNames);
                    
                    request.setAttribute("process_name", "Request End Reads");
                      request.setAttribute("forwardName", new Integer(Constants.PROCESS_RUN_END_READS));
                    return (mapping.findForward("select_plates"));
                }
                case Constants.PROCESS_RUN_END_READS_WRAPPER://run end reads wrapper
                {
                     EndReadsWrapperRunner runner = new EndReadsWrapperRunner();
                    runner.setUser(user);
                    t = new Thread(runner);
                     request.setAttribute(Constants.JSP_TITLE,"Request for end read wrapper invocation" );
                   request.setAttribute(Constants.ADDITIONAL_JSP,"The system will send you notification when finish");
                   // t.start();
                   return mapping.findForward("processing");
                }
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS://run assembly wrapper
                {
                    AssemblyRunner runner = new AssemblyRunner();
    
                    runner.setUser(user);
                    runner.setResultType( String.valueOf(IsolateTrackingEngine.PROCESS_STATUS_ER_PHRED_RUN));
                    t = new Thread(runner);
                      request.setAttribute(Constants.JSP_TITLE,"Request for end read assembler invocation." );
                   request.setAttribute(Constants.ADDITIONAL_JSP,"The system will send you notification when finish");
                   // t.start();
                   return mapping.findForward("processing");
                    
                }
                case Constants.PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY:
                {
                     request.setAttribute("forwardName", new Integer(Constants.PROCESS_CHECK_READS_AVAILABILITY));
                    request.setAttribute(Constants.JSP_TITLE,"select Plate to Check Clone Status");
                     return (mapping.findForward("scan_label"));
                }
                case Constants.PROCESS_RUN_ISOLATE_RUNKER://run isolate runker
                {
                    //get all specs for each of the three types and all plates where
                    ArrayList spec_collection = new ArrayList();
                    ArrayList specs = null;
                    ArrayList spec_names = new ArrayList();
                    ArrayList control_names = new ArrayList();
                    spec_collection.add( FullSeqSpec.getAllSpecNames() );
                    spec_names.add("Bio Evaluation of Clones");
                    control_names.add(Spec.FULL_SEQ_SPEC);
                    spec_collection.add(  EndReadsSpec.getAllSpecNames());
                     spec_names.add("End reads Evaluation");
                     control_names.add(Spec.END_READS_SPEC);
                    specs =  PolymorphismSpec.getAllSpecNames();
                    Spec spec = new PolymorphismSpec(null,Spec.NONE_SPEC , BecIDGenerator.BEC_OBJECT_ID_NOTSET,BecIDGenerator.BEC_OBJECT_ID_NOTSET) ;
                    specs.add(spec);
                    
                    spec_collection.add( specs);
                     spec_names.add("Polymorphism Finder");
                     control_names.add(Spec.POLYMORPHISM_SPEC);
                    request.setAttribute(Constants.SPEC_COLLECTION, spec_collection);
                    request.setAttribute(Constants.SPEC_TITLE_COLLECTION, spec_names);
                     request.setAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION,control_names);
                    // there are isolates subject to isolateranking
                    ArrayList plateNames = Container.findContainerLabelsForProcess(Constants.PROCESS_RUN_ISOLATE_RUNKER,-1);
                    request.setAttribute(Constants.PLATE_NAMES_COLLECTION, plateNames);
                    request.setAttribute("process_name", "Run Isolate Ranker");
                    return (mapping.findForward("select_plates"));
                }
                 case Constants.PROCESS_APROVE_ISOLATE_RANKER:
                {
                    
                    request.setAttribute(Constants.JSP_TITLE,"select Plate to Approve Isolate Ranker Results");
                     return (mapping.findForward("scan_label"));
                }
               
                case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER: // add new internal primer
                case Constants.PROCESS_VIEW_INTERNAL_PRIMERS://view internal primers
                {
                   
                }
                case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS://approve internal primers
                {
                }
                case Constants.PROCESS_RUN_PRIMER3://run primer3
                case Constants.PROCESS_RUNPOLYMORPHISM_FINDER: //run polymorphism finder
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER://run discrepancy finder
                {
                    //requesred to submit spec/specs
                    //file with sequence ids
                    // type of sequence ids
                     ArrayList spec_collection = new ArrayList();
                     ArrayList spec_names = new ArrayList();
                    ArrayList control_names = new ArrayList();
                    String title = null;
                     if (forwardName == Constants.PROCESS_RUN_PRIMER3)
                    {
                        spec_collection.add( Primer3Spec.getAllSpecNames() );
                        spec_names.add("Primer3 ");
                        control_names.add(Spec.PRIMER3_SPEC);
                        title = "run Primer Designer for the set of clones";
                     }
                     else if (forwardName == Constants.PROCESS_RUNPOLYMORPHISM_FINDER)
                     {
                        spec_collection.add( PolymorphismSpec.getAllSpecNames());
                        spec_names.add("Polymorphism Finder");
                        control_names.add(Spec.POLYMORPHISM_SPEC);
                        title = "run Polymorphism Finder for the set of clones";
                     }
                     else if (forwardName == Constants.PROCESS_RUN_DISCREPANCY_FINDER)
                     {
                         title = "run Discrepancy Finder for the set of clones";
                     }
                   
                    
                    request.setAttribute(Constants.SPEC_COLLECTION, spec_collection);
                    request.setAttribute(Constants.SPEC_TITLE_COLLECTION, spec_names);
                    request.setAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION,control_names);
                    request.setAttribute(Constants.JSP_TITLE,title);
                   
                    return (mapping.findForward("run_process"));
                }
                
                case Constants.PROCESS_RUN_DECISION_TOOL://run decision tool
                {
                    ArrayList spec_collection =  FullSeqSpec.getAllSpecNames() ;
                     request.setAttribute(Constants.SPEC_COLLECTION, spec_collection);
                   
                    return (mapping.findForward("run_desicion_tool"));
                }
                
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE:
                {
                    String file_description = "<I>You are about to run Discrepancy Finder as a stand aloan application. The <B>requested file format</b> is: sequence Id, reference sequence (Cds only), experimental sequence."
                    +"The process will take some time. The e-mail report will be sent to you upon completion."; 
                    String file_title = "Please select the sequence information file:";
                    String additional_jsp = "<INPUT TYPE=CHECKBOX NAME='send_needle_results' value=0>Attach needle aligment files.";
                    request.setAttribute(Constants.JSP_TITLE, "run Discrepancy Finder for set of sequences");
                    request.setAttribute(Constants.FILE_DESCRIPTION, file_description);
                    request.setAttribute(Constants.FILE_TITLE, file_title);
                    request.setAttribute(Constants.FILE_NAME,Constants.FILE_NAME);
                    request.setAttribute(Constants.ADDITIONAL_JSP, additional_jsp);
                    return (mapping.findForward("submit_data_file"));
                }
            
            case   Constants.PROCESS_PUT_CLONES_ON_HOLD :
            {
             //show label scan form  
                request.setAttribute(Constants.JSP_TITLE, "put Active Clones on Hold");
                return (mapping.findForward("scan_label"));
            }
            case Constants.PROCESS_ACTIVATE_CLONES:
            {
              //show label scan form  
                request.setAttribute(Constants.JSP_TITLE, "activate Clones");
                return (mapping.findForward("scan_label"));
            }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("error"));
    }
    
    
    
}
