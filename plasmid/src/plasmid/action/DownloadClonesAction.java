/*
 * DownloadClonesAction.java
 *
 * Created on June 23, 2005, 4:10 PM
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
import plasmid.form.DownloadClonesForm;
import plasmid.Constants;
import plasmid.query.coreobject.CloneInfo;

/**
 *
 * @author  DZuo
 */
public class DownloadClonesAction extends UserAction {
    
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
        
        boolean isWorkingStorage = false;
        if(User.INTERNAL.equals(user.getIsinternal())) {
            isWorkingStorage = true;
        }
        
        int orderid = ((DownloadClonesForm)form).getOrderid();
        String type = ((DownloadClonesForm)form).getType();
        OrderProcessManager manager = new OrderProcessManager();
        List clones = manager.getOrderClones(orderid, user, isWorkingStorage);
        
        if(clones == null) {
            if(Constants.DEBUG)
                System.out.println("Cannot get order clones from database.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general","Cannot get order clones from database."));
            return (mapping.findForward("error"));
        }
        
        response.setContentType("application/x-msexcel");
        response.setHeader("Content-Disposition", "attachment;filename=Clones.xls");
        PrintWriter out = response.getWriter();
        manager.writeCloneList(clones, out, isWorkingStorage);
        return null;
    }
}
