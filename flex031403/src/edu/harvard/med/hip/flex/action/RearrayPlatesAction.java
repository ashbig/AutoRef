/*
 * RearrayPlatesAction.java
 *
 * Created on November 19, 2002, 12:27 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import org.apache.struts.util.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
/**
 *
 * @author  htaycher
 */
public class RearrayPlatesAction extends ResearcherAction
{
    public ActionForward flexPerform(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
               
        // Get the workflow and project from the form and store in request.
        int workflowid = ((MgcImportRequestForm)form).getWorkflowid();
        String workflowname = ((MgcImportRequestForm)form).getWorkflowname();
        int projectid = ((MgcImportRequestForm)form).getProjectid();
        String projectname = ((MgcImportRequestForm)form).getProjectname();
        int protocolid = Integer.parseInt((String)request.getAttribute("protocolid"));
        
        FormFile rearrayFile = ((MgcCloneInfoImportForm)form).getMgcCloneFile();
        String fileName =  ((MgcCloneInfoImportForm)form).getFileName();
        
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("workflowname", workflowname);
        request.setAttribute("projectname", projectname);
        request.setAttribute("protocolid", new Integer(protocolid));
        
        InputStream input = null;
        try
        {
            input = rearrayFile.getInputStream();
        } 
        catch (FileNotFoundException ex)
        {
            errors.add("rearrayFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } 
        catch (IOException ex)
        {
            errors.add("rearrayFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        Connection conn = null;
        try
        {
            String username = ((User)request.getSession().getAttribute(Constants.USER_KEY)).getUsername();
            
            conn = DatabaseTransaction.getInstance().requestConnection();
            RearrayerForPlates rearrayer = new RearrayerForPlates(conn,projectid, workflowid,protocolid,input,username);
            rearrayer.createNewPlates();
            request.getSession().setAttribute("EnterSourcePlateAction.newContainers", new Vector(rearrayer.getRearrayContainers() ) );
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
