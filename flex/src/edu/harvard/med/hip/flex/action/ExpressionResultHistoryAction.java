/*
 * ExpressionResultHistoryAction.java
 *
 * Created on September 11, 2003, 3:09 PM
 */

package edu.harvard.med.hip.flex.action;

import java.io.IOException;
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

import edu.harvard.med.hip.flex.form.ExpressionResultHistoryForm;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.core.Sample;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ExpressionResultHistoryAction extends ResearcherAction {
    
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
        int sampleid = ((ExpressionResultHistoryForm)form).getSampleid();
        String resulttype = ((ExpressionResultHistoryForm)form).getResulttype();

        List results = null;
        try {
            results = Result.findResults(sampleid, resulttype);
            
            for(int i=0; i<results.size(); i++) {
                Result result = (Result)results.get(i);
            }
            
            if(results==null || results.size()==0) {
                return (mapping.findForward("noresult"));
            }
            
            Result result = (Result)results.get(0);
            Sample sample = result.getSample();
            request.setAttribute("sample", sample);
            request.setAttribute("results", results);
            request.setAttribute("resulttype", resulttype);
        } catch (Exception ex) {
            return (mapping.findForward("fail"));
        }
        
        return (mapping.findForward("success"));
    }
}

