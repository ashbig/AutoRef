/*
 * GetInputAction.java
 *
 * Created on June 12, 2001, 3:50 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.LinkedList;
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

import edu.harvard.med.hip.flex.core.Container;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.GetProcessPlateInputForm;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetInputAction extends ResearcherAction{

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
        String barcode = ((GetProcessPlateInputForm)form).getResearcherBarcode();
        String sourcePlate = ((GetProcessPlateInputForm)form).getSourcePlate();
        String sourceLocation = ((GetProcessPlateInputForm)form).getSourceLocation();
        String destLocation = ((GetProcessPlateInputForm)form).getDestLocation();
        LinkedList queueItems = (LinkedList)request.getSession().getAttribute("queueItems");
        Researcher researcher = null;

        // Validate the researcher barcode.
        try {
            researcher = new Researcher(barcode);
        } catch (FlexProcessException ex) {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", barcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {           
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
 
        if(!isValidPlate(queueItems, sourcePlate)) {
            errors.add("sourcePlate", new ActionError("error.plateId.invalid", sourcePlate));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
            
        return null;
    }
 
    // Validate the source plate barcode.
    private boolean isValidPlate(LinkedList queueItems, String sourcePlate) {        
        if(queueItems == null) {
            return false;
        }
        
        for(int i=0; i<queueItems.size(); i++) {
            QueueItem item = (QueueItem)queueItems.get(i);
            Container container = (Container)item.getItem();
            if(container.isSame(sourcePlate)) {
                return true;
            }
        }   
        
        return false;
    }
}
