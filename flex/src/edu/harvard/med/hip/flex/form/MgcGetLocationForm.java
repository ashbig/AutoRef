/*
 * MgcGetLocationsForm.java
 *
 * Created on July 17, 2002, 1:19 PM
 */

package edu.harvard.med.hip.flex.form;


import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.ArrayList;
/**
 *
 * @author  htaycher
 */
public class MgcGetLocationForm extends ProjectWorkflowForm
{
    private String              m_forward = null;
    private String              m_workflowname = null; 
    private String              m_processname = null; 
    private String              m_projectname = null; 
    private int[]               m_locations = null;
    
    public String               getForward(){ return m_forward;}
    public String               getProjectname(){ return m_projectname;}
    public String               getWorkflowname(){ return m_workflowname;}
    public String               getProcessname(){ return m_processname;}
    public int[]                getLocation(){ return m_locations;}
   
    public void               setForward(String p){  m_forward = p;}
    public void               setProjectname(String p){  m_projectname = p;}
    public void               setWorkflowname(String p){  m_workflowname = p;}
    public void               setProcessname(String p){  m_processname = p;}
    public void               setLocation(int[] p){  m_locations =p;}
   
    
}
