/*
 * DisplayExpSampleByPageAction.java
 *
 * Created on March 23, 2003, 10:50 PM
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

public class DisplayExpSampleByPageAction extends FlexAction{    
        
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        ActionErrors errors = new ActionErrors();
        ProjectSuccessRate project_success_rate = (ProjectSuccessRate)(request.getSession().getAttribute("project_success_rate"));        
        int projectid = project_success_rate.getProjectid();
        int workflowid = project_success_rate.getWorkflowid();
        String construct_type = project_success_rate.getCloneFormat();
        Vector success_rates = project_success_rate.getSuccess_rates();
        
        int curr_page = ((DisplayExpSampleByPageForm)form).getCurr_page();
        String sample_type = ((DisplayExpSampleByPageForm)form).getSample_type().trim();
        String sample_status = ((DisplayExpSampleByPageForm)form).getSample_status().trim();
        String display_format = ((DisplayExpSampleByPageForm)form).getDisplay_format().trim();
        int page_size = ((DisplayExpSampleByPageForm)form).getPage_size();
        int total_pages = ((DisplayExpSampleByPageForm)form).getTotal_pages();
        
        // the collection of sequences to be displayed is determined by sample_type and sample_status
        SuccessRate sr = null;
        if(sample_type.equalsIgnoreCase("INIT")){
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-1);
        }
        else if(sample_type.equalsIgnoreCase("GEL")){            
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-2);
        }
        else if (sample_type.equalsIgnoreCase("AGAR")){
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-3);
        }
        else if (sample_type.equalsIgnoreCase("ISOLATE")){
            sr = (SuccessRate)success_rates.elementAt(success_rates.size()-4);
        }
        
        request.setAttribute("sample_type", sample_type);       // setAttribute, important
        
        Vector seqids = new Vector();
        if(sample_status.equalsIgnoreCase("input"))
            seqids = sr.getTotal_samples();
        else if(sample_status.equalsIgnoreCase("success"))
            seqids = sr.getSuccess_samples();
        else if(sample_status.equalsIgnoreCase("fail"))
            seqids = sr.getFailed_samples();
        // if sample status in current step is 'notdone', then the sample information
        // in the previous step will be displayed
        else if(sample_status.equalsIgnoreCase("notdone")){
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
        //
        
        
        Vector pages_prev = new Vector();
        Vector pages_next = new Vector();
        for(int i = 1; i < curr_page; i++)
            pages_prev.add("" + i);
        for(int i = curr_page+1; i <= total_pages; i++)
            pages_next.add("" + i);

        int display_number = (page_size * curr_page) < seqids.size() ? page_size : seqids.size()- page_size*(curr_page-1);
        int[] sequence_ids = new int[display_number];
        for(int i = 0; i < display_number; i++)            
            sequence_ids[i] = ((Integer)(seqids.elementAt(i + page_size*(curr_page-1)))).intValue();
                

        FlexGeneMonitor m = new FlexGeneMonitor();        
        TreeSet seqSampleInfo = m.getSampleInfoBySeqids(sequence_ids, projectid, workflowid, construct_type, sample_type);
                
        request.setAttribute("pages_prev", pages_prev);
        request.setAttribute("pages_next", pages_next);
        request.setAttribute("curr_page", new Integer(curr_page));            
        request.setAttribute("display_format", display_format);         
        request.setAttribute("sample_status", sample_status);        
        request.setAttribute("page_size", new Integer(page_size));     
        request.setAttribute("total_pages", new Integer(total_pages));      
        request.setAttribute("workflowid", new Integer(workflowid));
        
        request.setAttribute("seqSampleInfo", seqSampleInfo);
        
        return (mapping.findForward("success"));
    }
}
