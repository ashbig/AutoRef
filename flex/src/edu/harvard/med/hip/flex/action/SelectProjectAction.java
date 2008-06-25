/*
 * SelectProjectAction.java
 *
 * Created on August 15, 2001, 4:36 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.IOException;
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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.workflow.*;
import static edu.harvard.med.hip.flex.workflow.Workflow.WORKFLOW_TYPE;

import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class SelectProjectAction extends ResearcherAction {
    
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
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
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        int projectid = ((ProjectWorkflowForm)form).getProjectid();
        String forwardName = ((ProjectWorkflowForm)form).getForwardName();        
        request.setAttribute("projectid", new Integer(projectid));
                
        try {
            Project project = new Project(projectid);
            List workflows = new ArrayList();
            
            if(Constants.PERIMETER_REARRAY.equals(forwardName)) {
                Workflow wf = new Workflow(Workflow.EXPRESSION_WORKFLOW);
                workflows.add(wf);
            } 
            else if(Constants.NEW_PLATE_LABELS.equals(forwardName))
            {
                //get plate labels by project
                ArrayList plate_data =  getUserPlatesByProject(project);
                request.setAttribute("LABELS", plate_data);
                request.setAttribute("projectname", project.getName());
                request.setAttribute("forwardName", forwardName);
                
                return (mapping.findForward("display_user_plates_for_project")) ;
            } 
             else
             {
                workflows = project.getWorkflows();
                Workflow wf;
                for ( int count = 0; count < workflows.size(); count++)
                {
                    wf=(Workflow)workflows.get(count);
                    if (wf.getWorkflowType()== WORKFLOW_TYPE.REARRAY)
                    {
                        workflows.remove(wf);
                    }
                }
                
            }
            
            request.setAttribute("projectname", project.getName());
            ((ProjectWorkflowForm)form).setProjectname(project.getName());     
            request.setAttribute("workflows", workflows);
            request.setAttribute("forwardName", forwardName);
            
            return (mapping.findForward("success"));
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
    
    
     private  ArrayList getUserPlatesByProject(Project project) throws Exception
    {
        ArrayList labels = new ArrayList();
        String sql = null;
        Protocol p = new Protocol(Protocol.UPLOAD_CONTAINERS_FROM_FILE);
        if ( project.getName().equalsIgnoreCase("MGC Project") )
        {
            sql = "select c.containerid as containerid, mc.oricontainer as USER_ID, mc.marker as marker, c.label as label \n " + 
        " from mgccontainer mc , containerheader c where  mc.mgccontainerid =  c.containerid  order by c.label";        
        }
        else
             sql = "select label, namevalue as USER_ID, c.containerid as containerid "
+" from containerheader c, containerheader_name n where  c.containerid=n.containerid and  n.nametype='USER_ID' and "
+" c.containerid in (select containerid from processobject where executionid in "
+" ( select executionid from processexecution where protocolid="+p.getId()+" and projectid= "+project.getId()+")) order by USER_ID" ;        
        String[] plate_description = null;
        CachedRowSet crs = null;
        try 
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
                plate_description= new String[5];
                plate_description[0] = String.valueOf( crs.getInt("CONTAINERID")); // containerid
                plate_description[1] = crs.getString("USER_ID");// user name
                plate_description[2] = crs.getString("LABEL");// flex name
                labels.add ( plate_description );
             }
             return labels;
        } catch (Exception ex) 
        {
               throw new Exception("Error during extracting mgc labels");
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        
       
    }
    
}
