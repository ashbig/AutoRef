/*
 * ViewOrderCollectionsAction.java
 *
 * Created on November 9, 2005, 2:44 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
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

import plasmid.coreobject.*;
import plasmid.process.*;
import plasmid.form.ViewOrderClonesForm;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class ViewOrderCollectionsAction extends UserAction {
    
    /** Creates a new instance of ViewOrderCollectionsAction */
    public ViewOrderCollectionsAction() {
    }
    
    /** Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     *
     */
    public ActionForward userPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        int orderid = ((ViewOrderClonesForm)form).getOrderid();
        OrderProcessManager manager = new OrderProcessManager();
        List clones = manager.getOrderCollections(orderid, user, false);
        
        if(clones == null) {
            if(Constants.DEBUG)
                System.out.println("Cannot get order clones from database.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general","Cannot get order clones from database."));
            return (mapping.findForward("error"));
        }
        
        request.setAttribute("orderid", new Integer(orderid));
        request.setAttribute("orderClones", clones);
        return mapping.findForward("success");    
    }   
}
