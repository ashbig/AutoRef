/*
 * MgcOrderOligoAction.java
 *
 * Created on June 3, 2002, 3:08 PM
 */

package edu.harvard.med.hip.flex.action;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.database.*;

import org.apache.struts.action.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  htaycher
 *Action process oligo order for request
 */
public class MgcOrderOligoAction extends AdminAction
{
    
    
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
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form, HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException
    {
        
        // place to store errors
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        // The form holding the status changes made by the user
        MgcOrderOligoForm requestForm = (MgcOrderOligoForm)form;
        int workflowid = requestForm.getWorkflowid();
        int projectid = requestForm.getProjectid();
        String processname = requestForm.getProcessname();
        boolean isFullPlatesOnly = requestForm.getIsFullPlate();
        Connection conn = null;
        try
        {
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            Protocol protocol  = new Protocol(Protocol.MGC_DESIGN_CONSTRUCTS);
            conn = DatabaseTransaction.getInstance().requestConnection();
            MgcOligoPlateManager om = new MgcOligoPlateManager(conn,project, workflow, 
                                         isFullPlatesOnly, protocol);
            om.orderOligo();
        } catch (FlexDatabaseException fde)
        {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",fde));
        }
        catch(Exception ex)
        {
            errors.add(Action.EXCEPTION_KEY,
            new ActionError(Action.EXCEPTION_KEY,ex));
            
        } finally
        {
            if(errors.size() > 0)
            {
                saveErrors(request,errors);
                DatabaseTransaction.rollback(conn);  
                return mapping.findForward("error");
                
            }
            else
            {
                return mapping.findForward("success");
            }
        }
        
    }
    
}
