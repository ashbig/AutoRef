/*
 * MgcImportRequestAction.java
 *
 * Created on May 24, 2002, 10:12 AM
 */


package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  htaycher
 * @version
 */
public class MgcImportRequestAction extends WorkflowAction
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
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        
        FormFile mgcRequestFile = ((MgcImportRequestForm)form).getMgcRequestFile();
        int      workflowid = ((MgcImportRequestForm)form).getWorkflowid();
        int      projectid      = ((MgcImportRequestForm)form).getProjectid();
       
        Project project = null;
        Workflow workflow = null;
        InputStream input = null;
        
        //put them back to request
        request.setAttribute("workflowid", Integer.toString(workflowid));
        request.setAttribute("projectid",Integer.toString(projectid));
        request.setAttribute("workflowname", ((MgcImportRequestForm)form).getWorkflowname());
        request.setAttribute("projectname",((MgcImportRequestForm)form).getProjectname());
        
        try
        {
            input = mgcRequestFile.getInputStream();
        } catch (FileNotFoundException ex)
        {
            errors.add("mgcRequestFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        } catch (IOException ex)
        {
            errors.add("mgcRequestFile", new ActionError("flex.infoimport.file", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        try
        {
            project = new Project(projectid);
            workflow = new Workflow(workflowid);
        } catch (FlexDatabaseException ex)
        {
            errors.add("projectid", new ActionError("error.workflow.project.invalid", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        String username = ((User)request.getSession().getAttribute(Constants.USER_KEY)).getUsername();
        
        ImportInformationRunner import_info =
        new ImportInformationRunner(input, project, workflow, username);
        Thread t = new Thread(import_info);
        t.start();
        return mapping.findForward("proccessing");
    }
    
    class ImportInformationRunner implements Runnable
    {
        private InputStream     m_Input = null;
        private Project         m_project = null;
        private Workflow        m_workflow = null;
        private String          m_username = null;
        
        
        
        public ImportInformationRunner(InputStream in, Project project, Workflow wf, String username)
        {
            m_Input = in;
            m_project = project;
            m_workflow = wf;
            m_username = username;
        }
        
        public void run()
        {
            ArrayList containers = new ArrayList();
            DatabaseTransaction t = null;
            Connection conn = null;
          
            MgcRequestImporter importer = new MgcRequestImporter( m_project,  m_workflow, m_username);
            try
            {
                t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
                //read request file, get all sequences for request, insert request
                // return all sequences from request
                importer.performImport(m_Input, conn) ;
            }
            catch (Exception e){}
         
            finally
            { DatabaseTransaction.closeConnection(conn); }
            
        }
    }
    
}


