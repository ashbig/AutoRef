/*
 * SelectSuccCriteria.java
 *
 * Created on February 11, 2003, 3:03 PM
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
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.query.*;

/**
 *
 * @author  hweng
 */
public class SelectSucCriteriaAction extends FlexAction {
    
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
        
        int workflowid = ((SuccessCriteriaForm)form).getWorkflowid();
        String workflowname = ((SuccessCriteriaForm)form).getWorkflowname();
        int projectid = ((SuccessCriteriaForm)form).getProjectid();
        String projectname = ((SuccessCriteriaForm)form).getProjectname();
        String format = ((SuccessCriteriaForm)form).getCloneFormat();
        String process = ((SuccessCriteriaForm)form).getProcessname();
        String init_date_from = ((SuccessCriteriaForm)form).getInitial_date_from();
        String init_date_to = ((SuccessCriteriaForm)form).getInitial_date_to();
        String[] _pcr_succ_criteria = ((SuccessCriteriaForm)form).getPcr_succ_criteria(); 
        
        // build PCR success criteria
        String[] pcr_succ_criteria = new String[_pcr_succ_criteria.length + 2];
        for(int i = 0; i < pcr_succ_criteria.length; i++)
            pcr_succ_criteria[i] = "";
        for(int i = 0; i < _pcr_succ_criteria.length; i++){
            pcr_succ_criteria[i] = _pcr_succ_criteria[i];
            if(_pcr_succ_criteria[i].equalsIgnoreCase("Multiple w/ Correct"))
                pcr_succ_criteria[pcr_succ_criteria.length-2] = "Multiple with correct";
            if(_pcr_succ_criteria[i].equalsIgnoreCase("No product"))
                pcr_succ_criteria[pcr_succ_criteria.length-1] = "NoProduct";
        } 
        Vector v_pcr_succ_criteria = new Vector();
        for(int i = 0; i < pcr_succ_criteria.length; i++)
            v_pcr_succ_criteria.add(pcr_succ_criteria[i]);
        //
        
        int agar_plate_succ_criteria = ((SuccessCriteriaForm)form).getAgar_plate_succ_criteria();
        int culture_plate_succ_criteria = ((SuccessCriteriaForm)form).getCulture_plate_succ_criteria();                
        
        
        FlexGeneMonitor m = new FlexGeneMonitor();
        ProjectSuccessRate psr = m.calSuccessRate(projectid, workflowid, format, process, 
                                                  pcr_succ_criteria, agar_plate_succ_criteria, culture_plate_succ_criteria, 
                                                  init_date_from, init_date_to);    
        psr.setProjectname(projectname);
        psr.setWorkflowname(workflowname);

        request.getSession().setAttribute("project_success_rate", psr);
        request.getSession().setAttribute("processname", process);
        request.getSession().setAttribute("init_date_from", init_date_from);
        request.getSession().setAttribute("init_date_to", init_date_to);    
        
        request.getSession().setAttribute("pcr_succ_criteria", v_pcr_succ_criteria);
        request.getSession().setAttribute("agar_plate_succ_criteria", new Integer(agar_plate_succ_criteria));
        request.getSession().setAttribute("culture_plate_succ_criteria", new Integer(culture_plate_succ_criteria));
        
        return (mapping.findForward("success"));
        
    }
}
