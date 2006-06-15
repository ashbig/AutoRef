/*
 * ViewOrderClonesAction.java
 *
 * Created on June 9, 2005, 3:52 PM
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
import plasmid.util.CloneInfoTargetPlateWellComparator;

/**
 *
 * @author  DZuo
 */
public class ViewOrderClonesAction extends UserAction {
    
    /** Creates a new instance of ViewOrderClonesAction */
    public ViewOrderClonesAction() {
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
        String type = ((ViewOrderClonesForm)form).getType();
        String collectionName = ((ViewOrderClonesForm)form).getCollectionName();
        String isBatch = ((ViewOrderClonesForm)form).getIsBatch();
        OrderProcessManager manager = new OrderProcessManager();
        List clones = null;
        
        if(Constants.ORDER_CLONE.equals(type)) {
            clones = manager.getOrderClones(orderid, user, false, isBatch);
        }
        if(Constants.ORDER_COLLECTION.equals(type)) {
            clones = manager.getOrderClonesForCollection(collectionName, user, false);
        }
       
        if(clones == null) {
            if(Constants.DEBUG)
                System.out.println("Cannot get order clones from database.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general","Cannot get order clones from database."));
            return (mapping.findForward("error"));
        }
        
        if("Y".equals(isBatch))
            Collections.sort(clones, new CloneInfoTargetPlateWellComparator());
        else
            isBatch = "N";
        
        request.setAttribute("orderid", new Integer(orderid));
        request.setAttribute("type", type);
        request.setAttribute("collectionName", collectionName);
        request.setAttribute("orderClones", clones);
        request.setAttribute("isBatch", isBatch);
        return mapping.findForward("success");
    }
}
