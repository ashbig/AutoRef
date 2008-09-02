/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.form;

import java.util.*;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.PROCESS_NTYPE;
import static edu.harvard.med.hip.flex.workflow.Workflow.WORKFLOW_TYPE;
import  edu.harvard.med.hip.flex.workflow.*;
import  edu.harvard.med.hip.flex.core.*;
/**
 *
 * @author htaycher
 */
public class AddWorkflowItemsForm extends   ProjectWorkflowForm 
{
    private String forwardName = PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE_INPUT.toString();
    private List workflowtemplates = new ArrayList();
    private List projects = new ArrayList();
   
    private String workflowtemplatename ="";
    private List vectors = new ArrayList();
    private int vectorid = -1;
    private String vectorname ="";
    private String workflowtype = WORKFLOW_TYPE.TRANSFER_TO_EXPRESSION.toString();
    private String projectworkflowcode="";
    
    public String getForwardName() {        return forwardName;    }
    public void setForwardName(String b) {        forwardName = b;    }
   
    public String getWorkflowtype() {        return workflowtype;    }
    public void setWorkflowtype(String b) {        workflowtype = b;    }
   
    public List   getWorkflowtemplates(){ return workflowtemplates;}
    public void     setWorkflowtemplates(List l){ workflowtemplates=l;}
     
    public List     getProjects(){ return projects;}
    public void     setProjects(List l ){ projects = l;}
    
    public String getWorkflowtemplatename(){ return workflowtemplatename;}
    public void     setWorkflowtemplatename(String g){ workflowtemplatename = g;}        
   
    public List             getVectors(){ return vectors;}
    public void             setVectors(List v){ vectors = v;}
    
    public void             setVectorName(String v){ vectorname=v;}
    public String           getVectorName(){ return vectorname;}
    
    public void             setVectorid(int v){ vectorid=v;}
    public int              getVectorid(){ return vectorid;}
    
    public void setProjectworkflowcode(String v){ projectworkflowcode = v;}
    public String getProjectworkflowcode(){ return projectworkflowcode;}
    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        PROCESS_NTYPE cur_pr_type = PROCESS_NTYPE.valueOf(forwardName);
        switch(cur_pr_type)
        {
            case   CREATE_PROJECT:
            case  CREATE_NEW_WORKFLOW:
            {break;}
            
            case  CREATE_NEW_WORKFLOW_FROM_TEMPLATE_CONFIRM:
            { 
                if ( workflowname == null || workflowname.trim().equals(""))
                errors.add("workflowname", new ActionError("error.parameter.invalid", "<br>Workflow name is empty. "+workflowname));
                break;
            }
            case CREATE_NEW_WORKFLOW_FROM_TEMPLATE:
            {
                //check that name unique
      
                Iterator<Workflow> iter = ProjectWorkflowProtocolInfo.getInstance().getWorkflows().values().iterator();
                Workflow temp;String vid_property=null;PWPItem vitem=null;
              while (iter.hasNext())
              {
                  temp = iter.next();
                  if ( temp.getName().equalsIgnoreCase(workflowname))
                  {
                      errors.add("workflowname", new ActionError("error.parameter.invalid", "<br>Workflow named "+ workflowname+" already exists."));
                  }
                  if ( temp.getWorkflowType() == WORKFLOW_TYPE.TRANSFER_TO_EXPRESSION)
                  {
                      vitem = PWPItem.getItem("VECTOR_ID",  temp.getProperties()                  );
                      if ( vitem != null && vitem.getValue().equals(String.valueOf(vectorid)))
                      {
                           vitem = PWPItem.getItem("VECTOR_NAME",  temp.getProperties() )   ;
                           errors.add("workflowname", new ActionError("error.parameter.invalid", "<br>Transfer workflow for vector "+ vitem.getValue()+" already exists. <P> Workflow name: "+temp.getName()));
                      }
                  }
              }
            }
            case   ADD_WORKFLOW_TO_PROJECT_CONFIRM:{break;}
            case ADD_WORKFLOW_TO_PROJECT:
            {
                 if ( projectworkflowcode == null || projectworkflowcode.trim().length()==0)
                 {
                     errors.add("projectworkflowcode", new ActionError("error.parameter.invalid", "<br>Please provide code for plates."));
                  break;
                 }
                  try
                  {
                     Workflow w = new Workflow(workflowid);
                     Project p = new Project(projectid);
                     Workflow ww = p.getWorkflow(workflowid);
                     if (ww != null)
                     {
                        errors.add("workflowname", new ActionError("error.parameter.invalid", "<br>Selected project already has selected workflow "+ww.getName()));
                     }
                  }catch(Exception e){}
                break;
            }
           
        }
        if ( errors.size() > 0 ) forwardName = cur_pr_type.getPreviousProcess().toString();
        return errors;
    }        
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        forwardName = PROCESS_NTYPE.CREATE_NEW_WORKFLOW_FROM_TEMPLATE_INPUT.toString();
       // workflowtypes = new WORKFLOW_TYPE[1];
        workflowtype=WORKFLOW_TYPE.TRANSFER_TO_EXPRESSION.toString();
    }
}

