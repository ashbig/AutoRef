/*
 * ExportSearchResultsAction.java
 *
 * Created on March 25, 2004, 12:19 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.form.QueryFlexForm;
import edu.harvard.med.hip.flex.query.handler.*;
import edu.harvard.med.hip.flex.query.bean.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  DZuo
 */
public class ExportSearchResultsAction extends Action {
    
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
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
                
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        boolean retValue = false;
        if(user != null) {
            retValue = AccessManager.getInstance().isUserAuthorize(user, Constants.RESEARCHER_GROUP);
        }
        
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=cloneinfo.xls");
        PrintWriter out = response.getWriter();
        
        if(retValue) {
            out.println("Search Term\tMatch Genbank Accession\tMatch Genbank GI\tLocus ID\tMatch FLEXGene\tClone Status\tVersion\tProject\tWorkflow\tConstruct Status\tClone ID\tClone Name\tClone Type\tCloning Strategy\tVector\tStatus");
        } else {
            out.println("Search Term\tMatch Genbank Accession\tMatch Genbank GI\tLocus ID\tMatch FLEXGene\tClone Status\tVersion\tConstruct Status\tClone ID\tClone Name\tClone Type\tCloning Strategy\tVector\tStatus");
        }
        
        List clones = (ArrayList)request.getAttribute("selectedClones");
        for(int i=0; i<clones.size(); i++) {
            String value = (String)clones.get(i);
            if(value != null) {
                String output = value.substring(value.indexOf("!")+1);
                output = output.replace('|', ' ');
                output = output.replace('!', '\t');
                out.println(output);
            }
        }
        
        return null;       
    }
}

