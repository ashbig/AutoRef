/*
 * SelectProcessAction.java
 *
 * Created on March 11, 2003, 3:51 PM
 */

package edu.harvard.med.hip.bec.action;

import java.util.*;
import java.text.*;
import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import edu.harvard.med.hip.bec.programs.blast.*;

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
                case Constants.PROCESS_CREATE_REPORT:
                    return (mapping.findForward("run_report"));

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
                             
                            ArrayList vectors = BioVector.getAllVectors();
                            request.setAttribute(Constants.VECTOR_COL_KEY, vectors);
                            request.setAttribute("forwardName", new Integer(Constants.PROCESS_SELECT_PLATES_FOR_END_READS));
                            return (mapping.findForward("select_vector"));
                    }
                    //allow to select not to run this end read
                    Oligo ol = new Oligo();
                    ol.setId(-1)   ;
                    ol.setName("-----") ;
               
                    primers.add(ol);
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
                    
                    request.setAttribute("process_name", getProcessTitle(forwardName));
                      request.setAttribute("forwardName", new Integer(Constants.PROCESS_RUN_END_READS));
                    return (mapping.findForward("select_plates"));
                 
                }
                
                case Constants.PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY:
                {
                     request.setAttribute("forwardName", new Integer(Constants.PROCESS_CHECK_READS_AVAILABILITY));
                    request.setAttribute(Constants.JSP_TITLE,getProcessTitle(forwardName));
                     return (mapping.findForward("scan_label"));
                }
                case Constants.PROCESS_VIEW_OLIGO_PLATE:
                {
                     request.setAttribute("forwardName", new Integer(forwardName));
                    request.setAttribute(Constants.JSP_TITLE,getProcessTitle(forwardName));
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
                    request.setAttribute(Constants.SPEC_COLLECTION, spec_collection);
                    request.setAttribute(Constants.SPEC_TITLE_COLLECTION, spec_names);
                     request.setAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION,control_names);
                    // there are isolates subject to isolateranking
                    ArrayList plateNames = Container.findContainerLabelsForProcess(Constants.PROCESS_RUN_ISOLATE_RUNKER,-1);
                    request.setAttribute(Constants.PLATE_NAMES_COLLECTION, plateNames);
                    request.setAttribute("process_name", getProcessTitle(forwardName));
                    return (mapping.findForward("select_plates"));
                }
          /*       case Constants.PROCESS_APROVE_ISOLATE_RANKER:
                {
                    
                    request.setAttribute(Constants.JSP_TITLE,getProcessTitle(forwardName));
                     return (mapping.findForward("scan_label"));
                }
           **/
                case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE:
                case  Constants.PROCESS_DELETE_TRACE_FILES :
                case  Constants.PROCESS_MOVE_TRACE_FILES:
             
                {
                     String file_description = "";
                     String file_title = "";  String additional_jsp = "";
                    switch (forwardName)
                    { 
                        case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE:
                        {
                            file_description = "<I>You are about to submit sequence data to BEC. The <B>requested file format</b> is: <p> <i> ></i>FLEX SAMPLE ID <P> sequence in fasta format."
                            +"The process will take some time. The e-mail report will be sent to you upon completion."; 
                            file_title =  "Please select the sequence information file:";
                            break;
                        }
                        case  Constants.PROCESS_DELETE_TRACE_FILES :
                        {
                            file_description = "<I>You are about to delete trace files from clone directories. The <B>requested file format</b> is: <p> <i> absolute file name (file path information included - c:/dirname)</i>."
                            +" The process will take some time. E-mail report will be sent to you upon completion."; 
                            file_title =  "Please select the file with trace files' information:";
                            break;
                        }
                        case  Constants.PROCESS_MOVE_TRACE_FILES:
                        {
                            file_description = "<I>You are about to move trace files from clone directories into common directory. The <B>requested file format</b> is: <p> <i> absolute file name (file path information included - c:/dirname)</i>."
                            +" The process will take some time. E-mail report will be sent to you upon completion."; 
                            file_title =  "Please select the file with trace files' information:";
                            break;
                        }
                    }
                    request.setAttribute(Constants.JSP_TITLE,getProcessTitle(forwardName) );
                    request.setAttribute(Constants.FILE_DESCRIPTION, file_description);
                    request.setAttribute(Constants.FILE_TITLE, file_title);
                    request.setAttribute(Constants.FILE_NAME,Constants.FILE_NAME);
                    request.setAttribute(Constants.ADDITIONAL_JSP, additional_jsp);
                    return (mapping.findForward("submit_data_file"));
                    
                }
                case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER: // add new internal primer
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS :
                case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS:
                case Constants.PROCESS_VIEW_INTERNAL_PRIMERS://view internal primers
                case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:
                case Constants.PROCESS_RUN_PRIMER3://run primer3
                case Constants.PROCESS_RUNPOLYMORPHISM_FINDER: //run polymorphism finder
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER://run discrepancy finder
                case Constants.PROCESS_NOMATCH_REPORT:
                case Constants.PROCESS_RUN_DECISION_TOOL:
                case Constants.PROCESS_RUN_DECISION_TOOL_NEW:
                case Constants.PROCESS_FIND_GAPS:
                case Constants.STRETCH_COLLECTION_REPORT_INT:
                case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:
                case Constants.LQR_COLLECTION_REPORT_INT:
                case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:
                case Constants.PROCESS_RUN_END_READS_WRAPPER:
                case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS:
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :
                case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  :
                case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY:
                
                case Constants.PROCESS_DELETE_PLATE :
                case Constants.PROCESS_DELETE_CLONE_READS :
                case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :
                case Constants.PROCESS_DELETE_CLONE_REVERSE_READ ://
                case Constants.PROCESS_DELETE_CLONE_SEQUENCE ://
                case  Constants.PROCESS_GET_TRACE_FILE_NAMES :
                case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID:
                case Constants.PROCESS_PROCESS_OLIGO_PLATE:
                case Constants.PROCESS_SET_CLONE_FINAL_STATUS:
                    case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:
                
               {
                    ArrayList spec_collection = new ArrayList();
                     ArrayList spec_names = new ArrayList();
                    ArrayList control_names = new ArrayList();
                    String title = null;
                    switch( forwardName)
                    {
                        case Constants.PROCESS_RUN_PRIMER3:
                        {
                            spec_collection.add( Primer3Spec.getAllSpecNames() );
                            spec_names.add("Primer3 ");
                            control_names.add(Spec.PRIMER3_SPEC);
                            break;
                         }
                        case Constants.PROCESS_RUN_DECISION_TOOL:
                        case Constants.PROCESS_RUN_DECISION_TOOL_NEW:
                        {
                            spec_collection.add( FullSeqSpec.getAllSpecNames()  );
                            spec_names.add("Bio Evaluation of Clones: ");
                            control_names.add(Spec.FULL_SEQ_SPEC);
                            break;
                         }
                        case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:
                         {
                            spec_collection.add( PolymorphismSpec.getAllSpecNames());
                            spec_names.add("Polymorphism Finder");
                            control_names.add(Spec.POLYMORPHISM_SPEC);
                            break;
                          }
                         case Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE:
                         case Constants.PROCESS_FIND_GAPS:
                         {
                             spec_collection.add( SlidingWindowTrimmingSpec.getAllSpecNames() );
                             spec_names.add("Sliding Window Algorithm Parameters:");
                             control_names.add(Spec.TRIM_SLIDING_WINDOW_SPEC);
                             break;
                         }
                    }
                    if ( spec_names != null )
                    {
                        request.setAttribute(Constants.SPEC_COLLECTION, spec_collection);
                        request.setAttribute(Constants.SPEC_TITLE_COLLECTION, spec_names);
                        request.setAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION,control_names);
                    }
                    request.setAttribute(Constants.JSP_TITLE,getProcessTitle(forwardName));
                    
                    return (mapping.findForward("initiate_process"));
                }
                
               
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE:
                {
                    String file_description = "<I>You are about to run Discrepancy Finder as a stand aloan application. The <B>requested file format</b> is: sequence Id, reference sequence (Cds only), experimental sequence."
                    +"The process will take some time. The e-mail report will be sent to you upon completion."; 
                    String file_title = "Please select the sequence information file:";
                    String additional_jsp = "<INPUT TYPE=CHECKBOX NAME='send_needle_results' value=0>Attach needle aligment files.";
                    request.setAttribute(Constants.JSP_TITLE, getProcessTitle(forwardName));
                    request.setAttribute(Constants.FILE_DESCRIPTION, file_description);
                    request.setAttribute(Constants.FILE_TITLE, file_title);
                    request.setAttribute(Constants.FILE_NAME,Constants.FILE_NAME);
                    request.setAttribute(Constants.ADDITIONAL_JSP, additional_jsp);
                    return (mapping.findForward("submit_data_file"));
                }
            
         /*   case   Constants.PROCESS_PUT_CLONES_ON_HOLD :
            {
             //show label scan form  
                request.setAttribute(Constants.JSP_TITLE, "Put Active Clones on Hold");
                return (mapping.findForward("scan_label"));
            }
            case Constants.PROCESS_ACTIVATE_CLONES:
            {
              //show label scan form  
                request.setAttribute(Constants.JSP_TITLE, "Activate Clones");
                return (mapping.findForward("scan_label"));
            }
          **/
            
            
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
    
   private String       getProcessTitle(int forwardName)
   {
          switch  (forwardName)
          {
            case Constants.PROCESS_CREATE_REPORT:return "";
            case Constants.PROCESS_UPLOAD_PLATES:return "";
            case Constants.PROCESS_SELECT_VECTOR_FOR_END_READS :return "";
            case Constants.PROCESS_SELECT_PLATES_FOR_END_READS: return "Request End Reads";
            case Constants.PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY: return "Select Plate to Check Clone Status";
            case Constants.PROCESS_VIEW_OLIGO_PLATE:return "Select Oligo Plate";
            case Constants.PROCESS_RUN_ISOLATE_RUNKER: return "Run Isolate Ranker";
            case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE: return "Submit Sequence data for set of clones";
            case  Constants.PROCESS_DELETE_TRACE_FILES : return "Submit list of trace files to delete";
            case  Constants.PROCESS_MOVE_TRACE_FILES: return"Submit list of trace files to move";
            case Constants.PROCESS_DELETE_PLATE :  return "Delete plates";
            case Constants.PROCESS_DELETE_CLONE_READS : return "Delete clone end reads (forward and reverse";
            case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :return"Delete clone forward end reads";
            case Constants.PROCESS_DELETE_CLONE_REVERSE_READ : return"Delete clone reverse end reads";
            case Constants.PROCESS_DELETE_CLONE_SEQUENCE : return"Delete clone sequences";
            case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:return"Reanalyze clone sequesnce"; 
            case  Constants.PROCESS_GET_TRACE_FILE_NAMES :return"Get trace files' names";
            case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :  return"Get order list for resequencing of end reads";
            case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  : return"Get order list for resequencing of internal reads";
            case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY: return"Create trace files quality report";
            case Constants.PROCESS_RUN_END_READS_WRAPPER: return"Run end reads wrapper";
            case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS: return"Run assembler for end reads";
            case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :return"Add new internal primer";
            case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID:return"View gene specific oligos ordered for clone";
            case Constants.PROCESS_PROCESS_OLIGO_PLATE:return "Update oligo plates status"; 
            case Constants.PROCESS_SET_CLONE_FINAL_STATUS:return "Set clones final status";
            case Constants.PROCESS_RUN_PRIMER3:return"Run primer designer for the set of clones";
            case Constants.PROCESS_RUN_DECISION_TOOL:return "Run desicion tool";
            case Constants.PROCESS_RUN_DECISION_TOOL_NEW:return "Run new desicion tool";
            case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:return "Run polymorphism finder for the set of clones";
            case Constants.PROCESS_RUN_DISCREPANCY_FINDER:  return "Run discrepancy finder for the set of clones";
            case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS:return"Approve internal primers";
            case Constants.PROCESS_VIEW_INTERNAL_PRIMERS:     return"View internal primers";
            case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:  return"Order internal primers";
            case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:  return"Assemble clone sequences";  
            case Constants.STRETCH_COLLECTION_REPORT_INT: return"View last set of contigs for clone";
            case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:   return"View all sets of contigs for contigs for clone";
            case Constants.LQR_COLLECTION_REPORT_INT:  return"View low quality regions for clone sequences";
             case  Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE: return "Run low quality regions findeer for clone sequences";
            case  Constants.PROCESS_FIND_GAPS:return"Run gap mapper";
            case Constants.PROCESS_NOMATCH_REPORT:return"Run 'NO MATCH' report";
            case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE:return "Run Discrepancy Finder for set of sequences";
            default: return "";
          }
   }
       
         

    
}
