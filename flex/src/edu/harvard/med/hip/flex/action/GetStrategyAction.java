/*
 * GetStrategyAction.java
 *
 * Created on August 6, 2003, 2:28 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.workflow.Project;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.form.CreateExpressionPlateForm;

/**
 *
 * @author  DZuo
 */
public class GetStrategyAction extends ResearcherAction {
    
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
        
        String sourcePlate = ((CreateExpressionPlateForm)form).getSourcePlate();
         
        if ((sourcePlate == null) || (sourcePlate.trim().length() < 1)) {
            errors.add("sourcePlate", new ActionError("error.plate.invalid.master", sourcePlate));
        } else {
            if(!sourcePlate.substring(1, 3).equals("MG")) {
                errors.add("sourcePlate", new ActionError("error.plate.invalid.master", sourcePlate));
            }
        }       
                
        List containers = null;
        try {
            containers = CloneContainer.findContainers(sourcePlate);
        } catch (Exception ex) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", sourcePlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(containers == null || containers.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", sourcePlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        Vector samples = ((CloneContainer)containers.get(0)).getSamples();
        CloneSample sample = (CloneSample)samples.get(0);
        int strategyid = sample.getStrategyid();
        CloningStrategy strategy = null;
        try {
            strategy = CloningStrategy.findStrategyById(strategyid);
        } catch (Exception ex) {            
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        if(strategy == null) {          
            request.setAttribute(Action.EXCEPTION_KEY, "strategy is null");
            return (mapping.findForward("error"));
        }
            
        String sourceVector = strategy.getClonevector().getName();
        List destVectors = null;
        try {
            destVectors = CloneVector.findDestVectors(sourceVector);
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, "strategy is null");
            return (mapping.findForward("error"));
        }
        
        if(destVectors == null || destVectors.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.expression.novector", sourcePlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }            
            
        request.setAttribute("vectors", destVectors);
        request.setAttribute("sourcePlate", sourcePlate);
        
        return (mapping.findForward("success"));
    }
}

