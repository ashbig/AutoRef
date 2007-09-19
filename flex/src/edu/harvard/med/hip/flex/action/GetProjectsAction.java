/*
 * GetProjectsAction.java
 *
 * This action gets all the projects from the database and save to the request.
 *
 * Created on August 15, 2001, 3:21 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.LinkedList;
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

/**
 *
 * @author  dzuo
 * @version
 */
public class GetProjectsAction extends ResearcherAction {
    
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
        String forwardName = ((ProjectWorkflowForm)form).getForwardName();
        
        try {
            Vector projects = null;
            
            if(Constants.MGC_PLATE_HANDLE.equals(forwardName)) {
                Project p = new Project(Project.MGC_PROJECT);
                projects = new Vector();
                projects.add(p);
            } else if(Constants.PERIMETER_REARRAY.equals(forwardName)
                || Constants.CREATE_EXP_DNA.equals(forwardName)) {
                Project p = new Project(Project.HUMAN);
                projects = new Vector();
                projects.add(p);
            } else if(Constants.NEW_PLATE_LABELS.equals(forwardName))
                 {
                 projects = Project.getAllProjects();
                 for(int i=0; i<projects.size(); i++)
                 {
                    Project proj = (Project)projects.elementAt(i);
                     if( proj.getId() == -1)
                        projects.remove(i);
                }
                 
            } else {
                projects = Project.getAllProjects();
                for(int i=0; i<projects.size(); i++) {
                    Project proj = (Project)projects.elementAt(i);
                    if(Project.MGC_PROJECT == (proj.getId()))
                        projects.remove(i);
                    if( proj.getId() == -1)
                        projects.remove(i);
                }
            }
            
            request.setAttribute("projects", projects);
            
            if(Constants.IMPORT_SEQUENCES.equals(forwardName)) {
                return (mapping.findForward("success_import_sequence"));
            }
             
            request.setAttribute("forwardName", forwardName);
            
            if(Constants.PLATE_CONDENSATION.equals(forwardName)) {
                return (mapping.findForward("success_plate_condensation"));
            }
            
            return (mapping.findForward("success"));
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
}
