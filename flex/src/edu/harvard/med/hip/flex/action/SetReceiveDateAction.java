/*
 * setReceiveDateAction.java
 *
 * Created on July 16, 2001, 2:20 PM
 */

package edu.harvard.med.hip.flex.action;
import edu.harvard.med.hip.flex.util.DateFormatter;
import edu.harvard.med.hip.flex.form.ReceiveOligoOrdersForm;
import java.io.IOException;
import java.util.*;
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

import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  Wendy
 * @version
 */
public class SetReceiveDateAction extends ResearcherAction {
    
    /** Creates new setReceiveDateAction */
    public SetReceiveDateAction() {
    }
    
    
    /**
     * get today's date in dd-mmm-yy format
     */
    private String getCurrentDate() {
        DateFormatter date = new DateFormatter();
        String currentDate = date.getDateString();
        return currentDate;
    }
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        ActionErrors errors = new ActionErrors();
        ReceiveOligoOrdersForm receiveOligoForm = new ReceiveOligoOrdersForm();
        String currentDate = getCurrentDate();
        receiveOligoForm.setReceiveDate(currentDate);
        //System.out.println("current date: " + currentDate);
        request.setAttribute("ReceiveOligoOrdersForm",receiveOligoForm);
        Protocol protocol = null;
        
        try{
            protocol = new Protocol("receive oligo plates");
            ContainerProcessQueue cpq = new ContainerProcessQueue();
            LinkedList items = cpq.getQueueItems(protocol);
            if(items.size() > 0) {
                request.setAttribute("SelectProtocolAction.queueItems", items);
            }
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            System.out.println(e);
        }
        
        return (mapping.findForward("success"));
    }
    
}
