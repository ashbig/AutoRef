/*
 * MgcImportRequestForm.java
 *
 * Created on August 15, 2001, 4:45 PM
 */

package edu.harvard.med.hip.flex.form;


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

/**
 *
 * @author  dzuo
 * @version
 */
public class MgcImportRequestForm extends ActionForm
{
    protected String forwardName = null;
    protected int projectid;
    protected int workflowid;
    protected FormFile mgcRequestFile = null;
    protected String workflowname = null;
    protected String projectname = null;
    
    /**
     * Set the mgc request file to the given value.
     *
     * @param mgcrequestFIle The value to be set to.
     */
    public void setMgcRequestFile(FormFile mgcRequestFile)
    {
        this.mgcRequestFile = mgcRequestFile;
    }
    
    public FormFile getMgcRequestFile()
    {
        return mgcRequestFile;
    }
    
    
    public void setForwardName(String forwardName)
    {
        this.forwardName = forwardName;
    }
    
    public String getForwardName()
    {
        return forwardName;
    }
    
    public void setWorkflowname(String name)
    {
        this.workflowname = name;
    }
    
    public String getWorkflowname()
    {
        return workflowname;
    }
    
    public void setProjectname(String name)
    {
        projectname = name;
    }
    
    public String getProjectname()
    {
        return projectname;
    }
    
    public void setProjectid(int projectid)
    {
        this.projectid = projectid;
    }
    
    public int getProjectid()
    {
        return projectid;
    }
    
    public void setWorkflowid(int workflowid)
    {
        this.workflowid = workflowid;
    }
    
    public int getWorkflowid()
    {
        return workflowid;
    }
    
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
    HttpServletRequest request)
    {
        
        ActionErrors errors = new ActionErrors();
        if ((mgcRequestFile.getFileName() == null) || (mgcRequestFile.getFileName().trim().length() < 1))
            errors.add("mgcRequestFile", new ActionError("error.mgcRequestFile.required"));
        
        
        return errors;
    }
    
    
}
