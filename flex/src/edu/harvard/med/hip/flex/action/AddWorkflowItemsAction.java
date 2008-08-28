/*
 * AddItemsAction.java
 *
 * Created on August 22, 2007, 3:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action;

/**
 *
 * @author htaycher
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.crypto.NullCipher;
import javax.servlet.*;
import javax.servlet.http.*;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.workflow.*;

import edu.harvard.med.hip.flex.infoimport.*;
import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.PROCESS_NTYPE;
import static edu.harvard.med.hip.flex.workflow.Workflow.WORKFLOW_TYPE;
import edu.harvard.med.hip.flex.infoimport.bioinfo.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
 import org.hibernate.*;
import org.hibernate.cfg.*;
/**
 *
 * @author  dzuo
 * @version
 */
public class AddWorkflowItemsAction extends ResearcherAction
{
    
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
    public synchronized ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
             
        ActionErrors errors = new ActionErrors();
        AddWorkflowItemsForm requestForm= ((AddWorkflowItemsForm)form);
        String forwardName = requestForm.getForwardName();
        PROCESS_NTYPE cur_process = PROCESS_NTYPE.valueOf(forwardName);
       
        User user = ((User)request.getSession().getAttribute(Constants.USER_KEY));
      Connection conn = null; Comparator genericObject;
        try 
        {
             request.setAttribute("forwardName", String.valueOf(forwardName));
             AccessManager manager = AccessManager.getInstance();
             user.setUserEmail( manager.getEmail(user.getUsername()));
             DatabaseTransaction t = DatabaseTransaction.getInstance();
             conn = t.requestConnection();
               switch (cur_process)
             {
               
                  case   CREATE_PROJECT_INPUT:
                  {
                     return (mapping.findForward(""));
                  }
                  case CREATE_NEW_WORKFLOW_INPUT:
                  {
                        return (mapping.findForward(""));
                    }
                  case CREATE_NEW_WORKFLOW_FROM_TEMPLATE_INPUT :
                  {
                     // WORKFLOW_TYPE[] workflowtypes = {WORKFLOW_TYPE.TRANSFER_TO_EXPRESSION};
                   //   request.setAttribute("workflowtypes", workflowtypes);
                      List<Workflow> workflowtemplates = Workflow.getAllWorkflows(
                              WORKFLOW_TYPE.TRANSFER_TO_EXPRESSION,
                              ProjectWorkflowProtocolInfo.getInstance().getWorkflows().values() );
                       genericObject = new BeanClassComparator("name");
 
                       Collections.sort(workflowtemplates, genericObject);
                      request.setAttribute("workflowtemplates", workflowtemplates);
                      List vectors = CloneVector.getAllVectorsByVectorType("%destination%");
                       genericObject = new BeanClassComparator("name");
 
                       Collections.sort(vectors, genericObject);
           
                      request.setAttribute("vectors", vectors);
                      return (mapping.findForward("add_workflow_items"));
                  }
                   case CREATE_NEW_WORKFLOW_FROM_TEMPLATE_CONFIRM:
                   {
                      int workflow_id = requestForm.getWorkflowid();
                      Workflow workflow = new Workflow(workflow_id);
                      request.setAttribute("workflowid", new Integer(workflow_id));
                      request.setAttribute("workflowtemplatename", workflow.getName());
                      int vectorid = requestForm.getVectorid();
                      request.setAttribute("vectorid", new Integer(vectorid));
                      CloneVector vector =  CloneVector.getCloneVectorByID(vectorid);
                      request.setAttribute("vectorname", vector.getName());
                      
                      request.setAttribute("workflowname", requestForm.getWorkflowname());
                      
                      request.setAttribute("workflowtype",cur_process.toString());
                       
                      return (mapping.findForward("add_workflow_items"));
              
                   }
                    case CREATE_NEW_WORKFLOW_FROM_TEMPLATE:
                   {
                      int template_workflow_id = requestForm.getWorkflowid();
                      int vectorid = requestForm.getVectorid();
                      String workflowname=requestForm.getWorkflowname();
                      String workflowtype = requestForm.getWorkflowtype();
                      
                      Workflow new_workflow=createNewWorkflowFromTemplate
                              (template_workflow_id,vectorid,workflowname,workflowtype,conn);
                      DatabaseTransaction.commit(conn);
                      request.setAttribute("workflow", new_workflow);
                      return (mapping.findForward("confirm_add_items"));
              
                   }
                   case DISPLAY_WORKFLOW:
                   {
                       Workflow w = new Workflow(requestForm.getWorkflowid());
                       request.setAttribute("workflow", w);
                  //  System.out.println(w.getId());
                       return (mapping.findForward("display_workflow"));
              
                   }
                    case ADD_WORKFLOW_TO_PROJECT_INPUT :
                  {
                       List<Project> projects = Project.getAllProjects();
                       for(Project pr: projects)
                        {
                            if( pr.getId() == -1){  projects.remove(pr);break;}
                        }
                         genericObject = new BeanClassComparator("name");
 
                       Collections.sort(projects, genericObject);
           
                       List <Workflow> workflows = Workflow.getAllWorkflows();
                       for(Workflow wf: workflows)
                        {
                            if( wf.getId() == -1){  workflows.remove(wf);break;}
                        }
                          genericObject = new BeanClassComparator("name");
 
                       Collections.sort(workflows, genericObject);
           
                      request.setAttribute("workflowtemplates", workflows);
                      request.setAttribute("projects", projects);
             
                      return (mapping.findForward("add_workflow_items"));
                  }
                  case ADD_WORKFLOW_TO_PROJECT_CONFIRM :
                  {
                      int workflowid = requestForm.getWorkflowid();
                      int projectid = requestForm.getProjectid();
                      String project_workflow_code = requestForm.getProjectworkflowcode();
                      
                      Workflow w = new Workflow(workflowid);
                      Project p = new Project(projectid);
                      request.setAttribute("workflowid", workflowid);
                      request.setAttribute("projectid", projectid);
                      request.setAttribute("workflowname", w.getName());
                      request.setAttribute("projectname", p.getName());
                      request.setAttribute("projectworkflowcode",project_workflow_code);
             
                      return (mapping.findForward("add_workflow_items"));
                  }
                  case ADD_WORKFLOW_TO_PROJECT :
                  {
                      int workflowid = requestForm.getWorkflowid();
                      int projectid = requestForm.getProjectid();
                        String project_workflow_code = requestForm.getProjectworkflowcode();
                   
                       Workflow w = new Workflow(workflowid);
                      Project p = new Project(projectid);
                       synchronized(this)
                      {//verify that this workflow does not belong to the project yet
                         String sql_connection = "insert into projectworkflow values("
                                     +projectid +","+workflowid+",'"+project_workflow_code+"')";
                           DatabaseTransaction.executeUpdate(sql_connection,conn);
                          conn.commit();
                           p.addWorkflow(w);
                      }
                       request.setAttribute("workflowid", w.getId());
                     
                      request.setAttribute("workflowname", w.getName());
                      request.setAttribute("projectname", p.getName());
                      request.setAttribute("projectworkflowcode",project_workflow_code);
              return (mapping.findForward("confirm_add_items"));
                    }

                 default: return null;
             }
           
        } catch (Exception e)
        {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
            
        
    }
    
  public static synchronized Workflow createNewWorkflowFromTemplate
          (int template_workflow_id,int vectorid,
          String workflowname, String workflowtype,
          Connection conn)throws Exception
  {
      
      
       
      Workflow template = new Workflow(template_workflow_id);
      Workflow ww = new Workflow( template, workflowname,  vectorid);
      ww.insert(conn);
      ProjectWorkflowProtocolInfo.addWorkflows(ww);
      return ww;
  }
                      
}


