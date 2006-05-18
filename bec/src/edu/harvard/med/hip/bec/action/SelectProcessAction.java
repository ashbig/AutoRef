//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
public class SelectProcessAction extends BecAction
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
         request.setAttribute(Constants.JSP_TITLE,getProcessTitle(forwardName));
        request.setAttribute(Constants.JSP_CURRENT_LOCATION, getPageLocation(forwardName));
                   
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
                    spec_names.add("5p primer:");
                    control_names.add("5p_primerid");
                    spec_collection.add(  primers);
                    spec_names.add("3p primer:");
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
                     return (mapping.findForward("scan_label"));
                }
                case Constants.PROCESS_VIEW_OLIGO_PLATE:
                {
                     request.setAttribute("forwardName", new Integer(forwardName));
                     return (mapping.findForward("scan_label"));
                }
            /*    case Constants.PROCESS_RUN_ISOLATE_RUNKER://run isolate runker
                {
                    //get all specs for each of the three types and all plates where
                    ArrayList spec_collection = new ArrayList();
                    ArrayList specs = null;
                    ArrayList spec_names = new ArrayList();
                    ArrayList control_names = new ArrayList();
                    spec_collection.add( FullSeqSpec.getAllSpecNames() );
                    spec_names.add("Clone acceptance criteria:");
                    control_names.add(Spec.FULL_SEQ_SPEC);
                    spec_collection.add(  EndReadsSpec.getAllSpecNames());
                     spec_names.add("Clone Ranking");
                     control_names.add(Spec.END_READS_SPEC);
                    request.setAttribute(Constants.SPEC_COLLECTION, spec_collection);
                    request.setAttribute(Constants.SPEC_TITLE_COLLECTION, spec_names);
                     request.setAttribute(Constants.SPEC_CONTROL_NAME_COLLECTION,control_names);
                     return (mapping.findForward("initiate_process"));
                }*/
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
                            file_description = "This process deletes trace files from hard drive. They will never be available for future use.  For source of files to delete, user must supply full path and filename. To get this information, user should use 'Get Trace File Names', under Process -> Delete Data."; 
                            file_title =  "Please select the file:";
                            break;
                        }
                        case  Constants.PROCESS_MOVE_TRACE_FILES:
                        {
                            file_description = "The process moves trace files out of the active directory into a temporary directory in order to save them allowing future use. For source of files to move, user must supply full path and filename. To get this information, user should use 'Get Trace File Names', under Process -> Delete Data. (In order for this to work, the application administrator will have had to set up the temporary directory during setup).";
                            file_title =  "Please select the file:";
                            break;
                        }
                    }
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
                case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE:    
                     case Constants.PROCESS_RUN_ISOLATE_RUNKER://run isolate runker
                
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
                         case Constants.PROCESS_RUN_ISOLATE_RUNKER://run isolate runker
                        {
                             spec_collection.add( FullSeqSpec.getAllSpecNames() );
                            spec_names.add("Clone acceptance criteria:");
                            control_names.add(Spec.FULL_SEQ_SPEC);
                            spec_collection.add(  EndReadsSpec.getAllSpecNames());
                             spec_names.add("Clone Ranking");
                             control_names.add(Spec.END_READS_SPEC);
                             break;
                            
                        }
                        case Constants.PROCESS_RUN_DECISION_TOOL:
                        case Constants.PROCESS_RUN_DECISION_TOOL_NEW:
                        {
                            spec_collection.add( FullSeqSpec.getAllSpecNames()  );
                            spec_names.add("Clone acceptance criteria:");
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
                             spec_names.add("Sliding window algorithm parameters:");
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
                            
                    return (mapping.findForward("initiate_process"));
                }
                
               
                case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE:
                {
                    String file_description = "<I>You are about to run Discrepancy Finder as a stand aloan application. The <B>requested file format</b> is: sequence Id, reference sequence (Cds only), experimental sequence."
                    +"The process will take some time. The e-mail report will be sent to you upon completion."; 
                    String file_title = "Please select the sequence information file:";
                    String additional_jsp = "<INPUT TYPE=CHECKBOX NAME='send_needle_results' value=0>Attach needle aligment files.";
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
           // System.out.println(e.getMessage());
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
            case Constants.PROCESS_UPLOAD_PLATES:return "Upload Plates ";
            case Constants.PROCESS_SELECT_VECTOR_FOR_END_READS :return "Request End Reads Sequencing -> Select Vector";
            case Constants.PROCESS_SELECT_PLATES_FOR_END_READS: return "Request End Reads Sequencing -> Select Primers and Plates";
            case Constants.PROCESS_RUN_ISOLATE_RUNKER: return "Run Isolate Ranker";
            case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE: return "Submit Sequence Data for Set of Clones";
           
               
             case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  : return"Get order list for resequencing of internal reads";
            case Constants.PROCESS_RUN_END_READS_WRAPPER: return"Check Quality and Distribute End Reads";
            case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS: return"Run Assembler for End Reads";
            case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :return"Add New Internal Primer";
            case Constants.PROCESS_PROCESS_OLIGO_PLATE:return "Track Oligo Plate"; 
            case Constants.PROCESS_SET_CLONE_FINAL_STATUS:return "Set Final Clone  Status";
            case Constants.PROCESS_RUN_PRIMER3:return"Run Primer Designer ";
             case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:return "Run Polymorphism Finder";
            case Constants.PROCESS_RUN_DISCREPANCY_FINDER:  return "Run Discrepancy Finder";
            case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS:return"Approve Internal Primers";
            case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:  return"Order Internal Primers";
            case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:  return"Assemble Clone Sequences";  
             case  Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE: return "Run Low Quality Regions Finder in Clone Sequences";
            case  Constants.PROCESS_FIND_GAPS:return"Run Gap Mapper";
            case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE:return "Run Discrepancy Finder";
            
            
              
              case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID:return"View Oligo Order(s) for Clone(s)";
             case Constants.PROCESS_VIEW_OLIGO_PLATE:return "View Oligo Plate";
           case Constants.STRETCH_COLLECTION_REPORT_INT: return"View Latest Contig Collection";
            case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:   return"View All Contig Collections";
            case Constants.LQR_COLLECTION_REPORT_INT:  return"View Low Quality Regions for Clone Sequences";
            case Constants.PROCESS_VIEW_INTERNAL_PRIMERS:     return"View Internal Primers";
           
        
              case Constants.PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY: return "View Available End Reads";
           
              
        case Constants.PROCESS_NOMATCH_REPORT:return"Mismatched Clones";
        case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY: return"Trace File Quality";
        case Constants.PROCESS_RUN_DECISION_TOOL:return "Quick Decision Tool";
        case Constants.PROCESS_RUN_DECISION_TOOL_NEW:return "Detailed Decision Tool";
        case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :  return"End Read";
        
          case  Constants.PROCESS_DELETE_TRACE_FILES : return "Delete Trace Files from Hard Drive";
            case  Constants.PROCESS_MOVE_TRACE_FILES: return"Move Trace Files from Clone Directory into Temporary Directory";
             case Constants.PROCESS_DELETE_PLATE :  return "Delete Plate";
            case Constants.PROCESS_DELETE_CLONE_READS : return "Delete Clone Forward and Reverse End Reads from Database";
            case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :return"Delete Clone Forward End Reads  from Database";
            case Constants.PROCESS_DELETE_CLONE_REVERSE_READ : return"Delete Clone Reverse End Reads from Database";
            case Constants.PROCESS_DELETE_CLONE_SEQUENCE : return"Delete Clone Sequences";
            case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:return"Reanalyze Clone Sequence"; 
            case  Constants.PROCESS_GET_TRACE_FILE_NAMES :return"Get Trace File Names";
              case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE  : return "Clean-up hard drive ";  
              default: return "";
          }
   }
          
          
    public String      getPageLocation(int forwardName)
   {
          switch  (forwardName)
          {
            case Constants.PROCESS_CREATE_REPORT:return "";
            case Constants.PROCESS_UPLOAD_PLATES:return "Home > Process > Upload Plates";
            case Constants.PROCESS_SELECT_VECTOR_FOR_END_READS :return "Home > Process > Read Manipulation > Request End Reads Sequencing";
            case Constants.PROCESS_SELECT_PLATES_FOR_END_READS: return "Home > Process > Read Manipulation > Request End Reads Sequencing";
             case Constants.PROCESS_RUN_ISOLATE_RUNKER: return "Home > Process > Evaluate Clones > Run Isolate Ranker";
            case Constants.PROCESS_SUBMIT_ASSEMBLED_SEQUENCE: return "Submit Sequence data for set of clones";
           
             
              case Constants.PROCESS_CREATE_ORDER_LIST_FOR_INTERNAL_RESEQUENCING  : return"Get order list for resequencing of internal reads";
             case Constants.PROCESS_RUN_END_READS_WRAPPER: return"Home > Process > Read Manipulation  > Check Quality and Distribute End Reads";
            case Constants.PROCESS_RUN_ASSEMBLER_FOR_END_READS: return"Home > Process > Read Manipulation  > Run Assembler for End Reads";
            case Constants.PROCESS_ADD_NEW_INTERNAL_PRIMER :return"Home > Process > Internal Primer Design and Order > Add New Internal Primer";
            case Constants.PROCESS_PROCESS_OLIGO_PLATE:return "Home > Process >  Internal Primer Design and Order  > Track Oligo Plate"; 
            case Constants.PROCESS_SET_CLONE_FINAL_STATUS:return "Home > Process > Set Final Clones  Status";
            case Constants.PROCESS_RUN_PRIMER3:return"Home > Process >  Internal Primer Design and Order  > Run Primer Designer";
             case Constants.PROCESS_RUNPOLYMORPHISM_FINDER:return "Home > Process > Evaluate Clones > Run Polymorphism Finder";
            case Constants.PROCESS_RUN_DISCREPANCY_FINDER:  return "Home > Process > Evaluate Clones > Run Discrepancy Finder";
            case Constants.PROCESS_APPROVE_INTERNAL_PRIMERS:return"Home > Process >  Internal Primer Design and Order  > Approve Internal Primers";
            case Constants.PROCESS_ORDER_INTERNAL_PRIMERS:  return"Home > Process >  Internal Primer Design and Order  > Order Internal Primers";
            case Constants.PROCESS_RUN_ASSEMBLER_FOR_ALL_READS:  return"Home > Process > Read Manipulation > Assemble Clone Sequences";  
             case  Constants.PROCESS_FIND_LQR_FOR_CLONE_SEQUENCE: return "Home > Process > Evaluate Clones > Run Low Quality Regions Finder in Clone Sequences";
            case  Constants.PROCESS_FIND_GAPS:return"Home > Process > Evaluate Clones > Run Gap Mapper";
             case Constants.PROCESS_RUN_DISCREPANCY_FINDER_STANDALONE:return "Home > Process > Evaluate Clones > Run Discrepancy Finder";
           
             case Constants.PROCESS_SELECT_PLATES_TO_CHECK_READS_AVAILABILITY: return "Home > Process > View Process Results > View Available End Reads";
            case Constants.PROCESS_VIEW_OLIGO_PLATE:return "Home > Process > View Process Results > View Oligo Plate";
              case Constants.STRETCH_COLLECTION_REPORT_INT: return"Home > Process > View Process Results > View Lastest Contigs Collection";
            case Constants.STRETCH_COLLECTION_REPORT_ALL_INT:   return"Home > Process > View Process Results > View All Contig Collections";
            case Constants.LQR_COLLECTION_REPORT_INT:  return"Home > Process > View Process Results > View Low Quality Regions for Clone Sequences";
           case Constants.PROCESS_VIEW_INTERNAL_PRIMERS:     return"Home > Process > View Process Results > View Internal Primers";
           case Constants.PROCESS_VIEW_OLIGO_ORDER_BY_CLONEID:return"Home > Process > View Process Results > View Oligo Order(s) for Clone(s)";
           
             
             
            case Constants.PROCESS_NOMATCH_REPORT:return"Home > Reports > Mismatch Clones";
            case Constants.PROCESS_CREATE_ORDER_LIST_FOR_ER_RESEQUENCING  :  return"Home > Reports > End Reads Report";
           case Constants.PROCESS_CREATE_REPORT_TRACEFILES_QUALITY: return"Home > Reports > Trace Files Quality Report";
           case Constants.PROCESS_RUN_DECISION_TOOL:return "Home > Reports > Quick Decision Tool";
            case Constants.PROCESS_RUN_DECISION_TOOL_NEW:return "Home > Reports > Detailed Desicion Tool";
           
              case  Constants.PROCESS_DELETE_TRACE_FILES : return "Home > Process > Delete data  > Delete Trace Files from Hard Drive";
            case  Constants.PROCESS_MOVE_TRACE_FILES: return"Home > Process > Delete data > Move Trace Files from Clone Directory into Temporary Directory";
            case Constants.PROCESS_DELETE_PLATE :  return "Home > Process > Delete data > Delete Plate";
            case Constants.PROCESS_DELETE_CLONE_READS : return "Home > Process > Delete data > Delete Clones Forward and Reverse End Reads";
            case Constants.PROCESS_DELETE_CLONE_FORWARD_READ :return"Home > Process > Delete data > Delete Clones Forward End Reads";
            case Constants.PROCESS_DELETE_CLONE_REVERSE_READ : return"Home > Process > Delete data > Delete Clones Reverse End Reads";
            case Constants.PROCESS_DELETE_CLONE_SEQUENCE : return"Home > Process > Delete data > Delete Clone Sequence";
            case Constants.PROCESS_REANALYZE_CLONE_SEQUENCE:return"Home > Process > Delete data > Reanalyze Clone Sequence "; 
            case  Constants.PROCESS_GET_TRACE_FILE_NAMES :return"Home > Process > Delete data > Get Trace File Names";
              case Constants.PROCESS_CLEANUP_INTERMIDIATE_FILES_FROM_HARD_DRIVE: return "Home > Process > Delete data > Clean-up hard drive ";
               
              
              default: return "";
          }
          
   }
       
         

    
}
