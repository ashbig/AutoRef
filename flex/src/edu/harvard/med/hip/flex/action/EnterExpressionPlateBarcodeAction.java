/*
 * EnterExpressionPlateBarcodeAction.java
 *
 * Created on August 19, 2003, 3:09 PM
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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.form.EnterResultForm;
import edu.harvard.med.hip.flex.file.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.MasterToExpressionContainerMapper;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.core.CloningStrategy;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

/**
 *
 * @author  DZuo
 */
public class EnterExpressionPlateBarcodeAction extends ResearcherAction {
    
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
        
        String newPlate = ((EnterResultForm)form).getNewPlate();   
        String researcherBarcode = ((EnterResultForm)form).getResearcherBarcode();
                
        List containers = null;
        try {
            containers = ExpressionCloneContainer.findContainers(newPlate);
        } catch (Exception ex) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", newPlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(containers == null || containers.size()==0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.barcode", newPlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        //create expression plate, sample and clones, and insert into database
        ExpressionCloneContainer container = (ExpressionCloneContainer)containers.get(0);
        if(!ExpressionCloneContainer.EXPRESSION_CONTAINER_TYPE.equals(container.getType())) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.plate.invalid.expression", newPlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
         // Validate the researcher barcode.
        Researcher r = null;
        try {
            r = new Researcher(researcherBarcode);
        } catch (FlexProcessException ex) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", researcherBarcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }          
                   
        request.setAttribute("newPlate", newPlate);
        request.getSession().setAttribute("newExpressionPlate", container);
             
        ((EnterResultForm)form).setResearcherObject(r);
        ((EnterResultForm)form).setWell(true);
        ((EnterResultForm)form).setSampleid(true);
        ((EnterResultForm)form).setGeneSymbol(false);
        ((EnterResultForm)form).setPa(false);
        ((EnterResultForm)form).setSgd(false);
        ((EnterResultForm)form).setMasterClone(false);
        ((EnterResultForm)form).setResearcher(true);
        ((EnterResultForm)form).setRestriction(false);
        ((EnterResultForm)form).setPcr(false);
        ((EnterResultForm)form).setColony(false);
        ((EnterResultForm)form).setFlorescence(false);
        ((EnterResultForm)form).setProtein(false);
        ((EnterResultForm)form).setStatus(true);
        ((EnterResultForm)form).setDate(false);
        ((EnterResultForm)form).setNextForward("enterResult");
        
        return (mapping.findForward("success"));
    }
}

