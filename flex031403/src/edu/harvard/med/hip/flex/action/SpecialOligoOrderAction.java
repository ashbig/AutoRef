/*
 * SpecialOligoOrderAction.java
 *
 * Created on June 5, 2002, 3:26 PM
 */

package edu.harvard.med.hip.flex.action;

/**
 *
 * @author  dzuo
 * @version
 */

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.*;
import org.apache.struts.action.*;

public class SpecialOligoOrderAction extends WorkflowAction {
    
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
    public synchronized ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        
        int projectid=((SpecialOligoOrderForm)form).getProjectid();
        int workflowid=((SpecialOligoOrderForm)form).getWorkflowid();
        boolean isFullPlate=((SpecialOligoOrderForm)form).getIsFullPlate();
        boolean isGroupBySize=((SpecialOligoOrderForm)form).getIsGroupBySize();
        
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            OligoPlateManager om = new OligoPlateManager(conn, project, workflow, 94, isFullPlate, isGroupBySize, null);
            om.orderOligo();
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",ex));
            saveErrors(request,errors);
            return mapping.findForward("error");
        } finally {
            if(conn != null)
                DatabaseTransaction.closeConnection(conn);
        }

        return mapping.findForward("success");
    }
} 
