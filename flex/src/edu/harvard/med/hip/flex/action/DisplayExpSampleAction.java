/*
 * DisplayExpSampleAction.java
 *
 * Created on February 19, 2003, 1:58 PM
 */

package edu.harvard.med.hip.flex.action;


import java.util.*;
import java.sql.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;

import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.query.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  hweng
 */
public class DisplayExpSampleAction extends FlexAction{
    
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        ActionErrors errors = new ActionErrors();
     
        ProjectSuccessRate project_success_rate = (ProjectSuccessRate)(request.getSession().getAttribute("project_success_rate"));
        int step = ((DisplayExpSampleForm)form).getStep();
        String sampleStatus = ((DisplayExpSampleForm)form).getSampleStatus();
        
        FlexGeneMonitor m = new FlexGeneMonitor();
        
        int projectid = project_success_rate.getProjectid();
        int workflowid = project_success_rate.getWorkflowid();
        String construct_type = project_success_rate.getCloneFormat();
        Vector success_rates = project_success_rate.getSuccess_rates();        
        String sample_type = "";                
        SuccessRate sr = null;        

        // the collection of sequences to be displayed is determined by step and sampleStatus
        if(step == 1){
            sample_type = "INIT";
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-1);
        }
        else if(step == 2){            
            sample_type = "GEL";
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-2);
        }
        else if (step == 3){
            sample_type = "AGAR";
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-3);
        }
        else if (step == 4){
            sample_type = "ISOLATE";
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-4);
        }

        // set the attribute of sample_type (important)
        // sample_type will be changed in codes followed.
        request.setAttribute("sample_type", sample_type);               
        
        Vector seqids = new Vector();
        if(sampleStatus.equalsIgnoreCase("input"))
            seqids = sr.getTotal_samples();
        else if(sampleStatus.equalsIgnoreCase("success"))
            seqids = sr.getSuccess_samples();
        else if(sampleStatus.equalsIgnoreCase("fail"))
            seqids = sr.getFailed_samples();
        // if sample status in current step is 'notdone', then the sample information
        // in the previous step will be displayed
        else if(sampleStatus.equalsIgnoreCase("notdone")){  
            seqids = sr.getNotDone_samples();
            if(sample_type.equalsIgnoreCase("ISOLATE"))
                sample_type = "AGAR";
            else if(sample_type.equalsIgnoreCase("AGAR")){
                if(workflowid == 4 || workflowid == 11){    // previous step is 'INIT'
                    sample_type = "INIT";
                }
                else{   // previous step is 'GEL'
                    sample_type = "GEL";
                }
            }
            else if(sample_type.equalsIgnoreCase("GEL"))
                sample_type = "INIT";
        }        
        
        int total_pages;
        int page_size;
        if(sample_type.equalsIgnoreCase("INIT")){
            request.setAttribute("display_format", "SPEICIAL");     // setAttribute
            page_size = 300;
            total_pages = (seqids.size()- 1)/page_size + 1;            
        }
        else if(sample_type.equalsIgnoreCase("ISOLATE")){
            request.setAttribute("display_format", "REGULAR");      // setAttribute
            page_size = 50;
            total_pages = (seqids.size()- 1)/page_size + 1;            
        }
        else{
            request.setAttribute("display_format", "REGULAR");      // setAttribute
            page_size = 300;
            total_pages = (seqids.size()- 1)/page_size + 1; 
        }
        
        int curr_page = 1;
        Vector pages_prev = new Vector();
        Vector pages_next = new Vector();
        for(int i = 1; i < curr_page; i++)
            pages_prev.add("" + i);
        for(int i = curr_page+1; i <= total_pages; i++)
            pages_next.add("" + i);
       
        int display_number = page_size < seqids.size() ?  page_size : seqids.size();
        int[] sequence_ids = new int[display_number];
        for(int i = 0; i < display_number; i++)
            sequence_ids[i] = ((Integer)(seqids.elementAt(i))).intValue();
        
        
        TreeSet seqSampleInfo = m.getSampleInfoBySeqids(sequence_ids, projectid, workflowid, construct_type, sample_type);

        request.setAttribute("sample_status", sampleStatus);
        request.setAttribute("pages_prev", pages_prev);
        request.setAttribute("pages_next", pages_next);
        request.setAttribute("curr_page", new Integer(curr_page));
        request.setAttribute("page_size", new Integer(page_size));     
        request.setAttribute("total_pages", new Integer(total_pages)); 
        request.setAttribute("workflowid", new Integer(workflowid));
        
        request.setAttribute("seqSampleInfo", seqSampleInfo);
        
        return (mapping.findForward("success"));
        
    }
    
}
