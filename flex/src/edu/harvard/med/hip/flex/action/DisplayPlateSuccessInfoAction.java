/*
 * DisplayPlateSuccessInfo.java
 *
 * Created on February 26, 2003, 11:24 AM
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
public class DisplayPlateSuccessInfoAction extends FlexAction{
    
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
         
        FlexGeneMonitor m = new FlexGeneMonitor();
        
        int projectid = project_success_rate.getProjectid();
        int workflowid = project_success_rate.getWorkflowid();
        String construct_type = project_success_rate.getCloneFormat();
        
        String sample_type = "";
        String[] criteria = null;
        int min = 1;               // minium number of successful samples for a sequence in a particular plate
        switch(step){
            case 2 :                 
                sample_type = "GEL"; 
                Vector pcr = (Vector)(request.getSession().getAttribute("pcr_succ_criteria"));
                criteria = new String[pcr.size()];
                for(int i = 0; i < pcr.size(); i++)
                    criteria[i] = (String)(pcr.elementAt(i));
                break;
            case 3 : 
                sample_type = "AGAR"; 
                int agar = ((Integer)(request.getSession().getAttribute("agar_plate_succ_criteria"))).intValue();
                criteria = new String[agar+1];
                for(int i = 0; i < agar; i++)
                    criteria[i] = new Integer(i).toString();      
                criteria[agar] = "none";
                break;
            case 4 :                 
                sample_type = "ISOLATE";                 
                min = ((Integer)(request.getSession().getAttribute("culture_plate_succ_criteria"))).intValue();
                break;               
        }
        
        Vector success_rates = project_success_rate.getSuccess_rates();
        SuccessRate sr = (SuccessRate)success_rates.elementAt(success_rates.size() - step);
        Vector success = sr.getSuccess_samples();
        Vector failed = sr.getFailed_samples();
        int[] successful_seqs = new int[success.size()];
        int[] failed_seqs = new int[failed.size()];
        for(int i = 0; i < success.size(); i++)
            successful_seqs[i] = ((Integer)(success.elementAt(i))).intValue();
        for(int i = 0; i < failed.size(); i++)
            failed_seqs[i] = ((Integer)(failed.elementAt(i))).intValue();

        HashMap plate_success_info_hashmap = 
        m.MapSuccInfoToPlate(successful_seqs, failed_seqs, projectid, workflowid, construct_type, sample_type, criteria, min);
        
        TreeSet plates = new TreeSet(new PlateComparator());
        Iterator it = plate_success_info_hashmap.keySet().iterator();
        while(it.hasNext()){
            plates.add((PlateSuccessInfo)(plate_success_info_hashmap.get(it.next())));
        }
        
        request.setAttribute("sample_type", sample_type);
        request.setAttribute("plate_success_info", plates);
        request.setAttribute("workflowid", new Integer(workflowid));
        
        return (mapping.findForward("success"));    
        
    }
}
