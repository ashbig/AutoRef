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
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;


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
        ArrayList specs = new ArrayList();
        
         System.out.println( forwardName);
        try
        {
            
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
                case Constants.PROCESS_RUN_END_READS://run sequencing for end reads
                {
                }
                case Constants.PROCESS_RUN_END_READS_WRAPPER://run end reads wrapper
                case Constants.PROCESS_RUN_ASSEMBLER://run assembly wrapper
                {
                  //  RunProcess process = new RunProcess(forwardName, null);
                  //  process.run();
                }
                case Constants.PROCESS_CHECK_READS_AVAILABILITY://check reads
                {
                }
                case Constants.PROCESS_RUN_ISOLATE_RUNKER://run isolate runker
                {
                }
                case Constants.PROCESS_APROVE_ISOLATE_RANKER://approve isolate ranker
                {
                    request.setAttribute(Constants.JSP_TITLE, "approve Isolate Ranking");
                    return (mapping.findForward("scan_lable"));
                }
                case Constants.PROCESS_PUT_CLONES_ON_HOLD: //put clones on hold
                {
                    request.setAttribute(Constants.JSP_TITLE, "activate/deactivate Clones");
                    return (mapping.findForward("scan_lable"));
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
                    ArrayList plates =  new ArrayList();
                    ArrayList specNames = new ArrayList();
                    ArrayList specLists = new ArrayList();
                    ArrayList specParamNames = new ArrayList();
                    
                    if (forwardName == 10)
                    {
                        ArrayList primer3_specs = Primer3Spec.getAllSpecs();
                        specNames.add("Primer designer");
                        specLists.add(primer3_specs);
                        specParamNames.add("primer3_specid");
                        request.setAttribute("title", "Design Primers");
                        //plates = getPlates();
                    }
                    if (forwardName == 14)//run polymorphism finder
                    {
                        ArrayList polym_specs = PolymorphismSpec.getAllSpecs();
                        specNames.add("Polymorphism finder");
                        specLists.add(polym_specs);
                        specParamNames.add("polym_specid");
                         request.setAttribute("title", "Run Polymorphism Finder");
                        //plates = getPlates();
                    }
                    if (forwardName == 13)//run discrepancy finder
                    {
                         request.setAttribute("title", "Run Discrepancy Finder");
                        //plates = getPlates();
                    }
                    
                    request.setAttribute("plates", plates);
                    request.setAttribute("specLists", specLists);
                    request.setAttribute("specNames", specNames);
                    request.setAttribute("specParamNames", specParamNames);
                    return (mapping.findForward("run_process"));
                }
                
                case Constants.PROCESS_RUN_DESIGION_TOOL://run decision tool
                {
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
