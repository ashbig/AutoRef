/*
 * SelectProcessForSucRate.java
 *
 * Created on January 29, 2003, 1:57 PM
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
public class SelectProcessForSucRateAction extends FlexAction {
    
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
        
        int workflowid = ((ProcessForSucRateForm)form).getWorkflowid();
        String workflowname = ((ProcessForSucRateForm)form).getWorkflowname();
        int projectid = ((ProcessForSucRateForm)form).getProjectid();
        String projectname = ((ProcessForSucRateForm)form).getProjectname();
        String format = ((ProcessForSucRateForm)form).getCloneFormat();
        String process = ((ProcessForSucRateForm)form).getProcessname();
        String initial_month_from = ((ProcessForSucRateForm)form).getInitial_month_from();
        String initial_month_to = ((ProcessForSucRateForm)form).getInitial_month_to();
        String initial_day_from = ((ProcessForSucRateForm)form).getInitial_day_from();
        String initial_day_to = ((ProcessForSucRateForm)form).getInitial_day_to();
        String initial_year_from = ((ProcessForSucRateForm)form).getInitial_year_from();
        String initial_year_to = ((ProcessForSucRateForm)form).getInitial_year_to();
        String initial_date_from;
        String initial_date_to;
        
        initial_date_from = initial_day_from + "-" + initial_month_from + "-" + initial_year_from;
        initial_date_to = initial_day_to + "-" + initial_month_to + "-" + initial_year_to;
       
        if((initial_month_from.equalsIgnoreCase("month") && 
             initial_day_from.equalsIgnoreCase("day") &&
             initial_year_from.equalsIgnoreCase("year"))
           &&
           (initial_month_to.equalsIgnoreCase("month") && 
             initial_day_to.equalsIgnoreCase("day") &&
             initial_year_to.equalsIgnoreCase("year")))
        {
            initial_date_from = "";
            initial_date_to = "";        
        }
        
        else if((initial_month_from.equalsIgnoreCase("month") && 
                 initial_day_from.equalsIgnoreCase("day") &&
                 initial_year_from.equalsIgnoreCase("year")))
        {
            initial_date_from = "01-Jan-1997";
        }
        else if((initial_month_to.equalsIgnoreCase("month") && 
                 initial_day_to.equalsIgnoreCase("day") &&
                 initial_year_to.equalsIgnoreCase("year")))
        {
            initial_date_to = getCurrentDate();
        }                 
                    
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("projectname", projectname);
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("workflowname", workflowname);    
        request.setAttribute("cloneFormat", format);
        request.setAttribute("processname", process);
        request.setAttribute("initial_date_from", initial_date_from);
        request.setAttribute("initial_date_to", initial_date_to);
        return (mapping.findForward("require_success_criteria"));
            
    }
    
    
    public String getCurrentDate(){
        
        Calendar calendar = new GregorianCalendar();
        java.util.Date d = new java.util.Date();
        calendar.setTime(d);      
        String month = "";
        switch(calendar.get(Calendar.MONTH) + 1){
            case 1 : month = "JAN"; break;
            case 2 : month = "FEB"; break; 
            case 3 : month = "MAR"; break;
            case 4 : month = "APR"; break;
            case 5 : month = "MAY"; break;
            case 6 : month = "JUN"; break;
            case 7 : month = "JUL"; break;
            case 8 : month = "AUG"; break;
            case 9 : month = "SEP"; break;
            case 10 : month = "OCT"; break;
            case 11 : month = "NOV"; break;
            case 12 : month = "DEC"; break;           
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH);       
        String date = (day < 10 ? "0"+day : ""+day) + "-" + month + "-" + calendar.get(Calendar.YEAR);
        return date;
    }
    
}
