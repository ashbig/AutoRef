/*
 * ProjectWorkflowForm.java
 *
 * Created on August 15, 2001, 4:45 PM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ProjectWorkflowForm extends ActionForm {
    protected String forwardName = null;
    protected int projectid;
    protected int workflowid;
    
    public void setForwardName(String forwardName) {
        this.forwardName = forwardName;
    }
    
    public String getForwardName() {
        return forwardName;
    }
    
    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }
    
    public int getProjectid() {
        return projectid;
    }
    
    public void setWorkflowid(int workflowid) {
        this.workflowid = workflowid;
    }
    
    public int getWorkflowid() {
        return workflowid;
    }
}
