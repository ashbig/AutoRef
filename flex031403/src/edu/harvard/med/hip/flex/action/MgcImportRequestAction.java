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
public class MgcImportRequestAction extends AdminAction
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
        } catch (FlexDatabaseException ex)
        {
            errors.add("projectid", new ActionError("error.workflow.project.invalid", ex.getMessage()));
            saveErrors(request,errors);
            return new ActionForward(mapping.getInput());
        }
        
        String username = ((User)request.getSession().getAttribute(Constants.USER_KEY)).getUsername();
        
        ImportInformationRunner import_info =
        new ImportInformationRunner(input, project, workflowid, username);
        Thread t = new Thread(import_info);
        t.start();
        return mapping.findForward("proccessing");
    }
    
    class ImportInformationRunner implements Runnable
    {
        private InputStream m_Input = null;
        private Project         m_project = null;
        private int         m_workflowid = -1;
        private String      m_username = null;
        
        
        
        public ImportInformationRunner(InputStream in, Project project, int wfid, String username)
        {
            m_Input = in;
            m_project = project;
            m_workflowid = wfid;
            m_username = username;
        }
        
        public void run()
        {
            ArrayList containers = new ArrayList();
            DatabaseTransaction t = null;
            Connection conn = null;
            
            MgcRequestImporter importer = new MgcRequestImporter( m_project, m_username);
            try
            {
                t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
                //read request file, get all sequences for request, insert request
                // return all sequences from request
                ArrayList sequences = importer.performImport(m_Input, conn) ;
                //get all containers for the request
                Rearrayer r = new Rearrayer();
                r.findMgcContainers( sequences);
                containers = r.getContainers();
                
                //get next protocol 
                
                Workflow wf = new Workflow(m_workflowid);
                Protocol current_protocol = new Protocol(Protocol.IMPORT_MGC_REQUEST);
                Vector protocols = wf.getNextProtocol(current_protocol);
                Protocol next_protocol = null;
                
                
                //put containers on queue
                ArrayList containers_items = new ArrayList();
                
                for (int count = 0; count < containers.size(); count++)
                {
                    for (int count_protocol = 0; count_protocol < protocols.size(); count_protocol++)
                    {
                        next_protocol = (Protocol) protocols.get(count_protocol);
                        String next_protocol_name = next_protocol.getProcessname();
                        if ( next_protocol_name != null && next_protocol_name.equals(Protocol.CREATE_CULTURE_FROM_MGC) )
                        {//
                            QueueItem q_i = new  QueueItem(containers.get(count), next_protocol, m_project, wf);
                            containers_items.add(q_i);
                        }
                    }
                }
                ContainerProcessQueue cont_q = new ContainerProcessQueue();
                cont_q.addQueueItems( containers_items, conn);
                
                
                //put sequences on queue
                ArrayList sequences_items = new ArrayList();
                for (int count_sequences = 0; count_sequences < sequences.size(); count_sequences++)
                {
                    for (int count_protocol = 0; count_protocol < protocols.size(); count_protocol++)
                    {
                        next_protocol = (Protocol) protocols.get(count_protocol);
                        String next_protocol_name = next_protocol.getProcessname();
                        if ( next_protocol_name != null && next_protocol_name.equals(Protocol.DESIGN_CONSTRUCTS) )
                        {//
                            QueueItem q_i = new  QueueItem(sequences.get(count_sequences), next_protocol, m_project, wf);
                             sequences_items.add(q_i);
                        }
                    }
                }
                SequenceProcessQueue sequences_q = new SequenceProcessQueue();
                sequences_q.addQueueItems( sequences_items, conn);
                
            }
            catch (Exception e){}
         
            finally
            { DatabaseTransaction.closeConnection(conn); }
            
        }
    }
    
}


