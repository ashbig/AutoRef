/*
 * MgcDeleteDuplicatedSequencesAction.java
 *
 * Created on June 25, 2002, 2:04 PM
 */

package edu.harvard.med.hip.flex.action;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;

import org.apache.struts.action.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  htaycher
 */
public class MgcDeleteDuplicatedSequencesAction extends ResearcherAction
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
        
        // The form holding the status changes made by the user
        MgcOrderOligoForm requestForm = (MgcOrderOligoForm)form;
        int workflowid = requestForm.getWorkflowid();
        int projectid = requestForm.getProjectid();
        String processname = requestForm.getProcessname();
        boolean isCleanupDuplicates = requestForm.getIsCleanupDuplicates();
        
        
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        LinkedList seqList = null;
        LinkedList seqNoDuplicate = new LinkedList();
        Connection conn = null;
        Project project = null;
        Workflow workflow = null;
        Protocol protocol  = null;
        
        try
        {
            project = new Project(projectid);
            workflow = new Workflow(workflowid);
            protocol  = new Protocol(Protocol.MGC_DESIGN_CONSTRUCTS);
            if ( ! isCleanupDuplicates )
            {
                seqList = seqQueue.getQueueItems(protocol, project, workflow);
            }
            else
            {
                seqList = (LinkedList)request.getSession().getAttribute("not_duplicated_sequences");
            }
        }
        catch(Exception ex)
        {
            errors.add(Action.EXCEPTION_KEY, new ActionError(Action.EXCEPTION_KEY,ex));
            saveErrors(request,errors);
            return mapping.findForward("error");
        }
        
       
        //constract
        ArrayList plates = new ArrayList();
        //get total number of genes in queue
        if (seqList.size() != 0)
        {
            try
            {
                Rearrayer ra = new Rearrayer(new ArrayList(seqList), 94);
                plates = ra.getPlates();
            }
            catch(Exception e)
            {
                request.setAttribute(Action.EXCEPTION_KEY, e);
                return (mapping.findForward("error"));
            }
        }
        request.setAttribute("workflowid", new Integer(workflowid));
        request.setAttribute("projectid", new Integer(projectid));
        request.setAttribute("platesInfo", plates);
        request.setAttribute("full_plates", new Integer( plates.size() ));
        request.setAttribute("sequences_count", new Integer(seqList.size() ));
        request.setAttribute("projectname", project.getName());
        request.setAttribute("processname", processname);
        request.setAttribute("workflowname", workflow.getName());
        return mapping.findForward("success");
        
    }
    
    
    
}

