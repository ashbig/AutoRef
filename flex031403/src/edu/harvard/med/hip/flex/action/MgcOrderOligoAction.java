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
public class MgcOrderOligoAction extends ResearcherAction
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
        String username = user.getUsername();
        
        // The form holding the status changes made by the user
        MgcOrderOligoForm requestForm = (MgcOrderOligoForm)form;
        int workflowid = requestForm.getWorkflowid();
        int projectid = requestForm.getProjectid();
        String processname = requestForm.getProcessname();
        boolean isFullPlatesOnly = requestForm.getIsFullPlate();
        MgcOligoPlateManager om  = null;
        
        
        Connection conn = null;
        try
        {
            Project project = new Project(projectid);
            Workflow workflow = new Workflow(workflowid);
            Protocol protocol  = new Protocol(Protocol.MGC_DESIGN_CONSTRUCTS);
            conn = DatabaseTransaction.getInstance().requestConnection();
            //check if attribute not_duplicated_sequences is set in session
            //if yes queue has duplicates
            if ( request.getSession().getAttribute("not_duplicated_sequences") != null)
            {
                 LinkedList seqList = (LinkedList)request.getSession().getAttribute("not_duplicated_sequences");
                 om = new MgcOligoPlateManager(seqList, conn,project, workflow,
                                                                isFullPlatesOnly, protocol, username);
                 request.getSession().removeAttribute("not_duplicated_sequences")         ;                                      
            }
            else
            {
                om = new MgcOligoPlateManager(conn,project, workflow,
                                                                isFullPlatesOnly, protocol, username);
            }
            om.orderOligo();
            
            return mapping.findForward("success");
        } catch (FlexDatabaseException fde)
        {
            System.out.println("FDE " + fde.getMessage());
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error",fde));
            saveErrors(request,errors);
            DatabaseTransaction.rollback(conn);
                return mapping.findForward("error");
        }
        catch(Exception ex)
        {
            System.out.println("E " + ex.getMessage());
            errors.add(Action.EXCEPTION_KEY,
            new ActionError(Action.EXCEPTION_KEY,ex));
            saveErrors(request,errors);
            DatabaseTransaction.rollback(conn);
                return mapping.findForward("error");
        }
        finally
        {
            
            if (conn != null)
                DatabaseTransaction.closeConnection(conn);
            
        }
        
        
    }
    
    
  
    
}

