/*
 * MgcOrderOligoForm.java
 *
 * Created on June 3, 2002, 2:50 PM
 */

package edu.harvard.med.hip.flex.form;

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
public class MgcOrderOligoForm extends ProjectWorkflowForm
{
    private boolean isFullPlate = true;
    private boolean isCleanupDuplicates = true;
    private boolean isCheckForResearchBarcode = true;
    private String  processname = null;
    private String  researcherBarcode = null;
    private String  workflowname = null;
    
    public void         setIsFullPlate(boolean isFullPlate)    {        this.isFullPlate = isFullPlate;    }
    public boolean      getIsFullPlate()    {        return isFullPlate;    }
    
    public void setIsCleanupDuplicates(boolean isCleanupDuplicates)
    {
        this.isCleanupDuplicates = isCleanupDuplicates;
        isCheckForResearchBarcode = false;
    }
    
    public boolean      getIsCleanupDuplicates()    {        return isCleanupDuplicates;    }
    
    public void         setProcessname(String processname)    {        this.processname = processname;    }
    public String       getProcessname()    {        return processname;    }
    
    public void         setWorkflowname(String workflowname)    {        this.workflowname = workflowname;    }
    public String       getWorkflowname()    {        return workflowname;    }
    
    public void         setResearcherBarcode(String researcherBarcode)    {        this.researcherBarcode = researcherBarcode;    }
    public String       getResearcherBarcode()    {        return researcherBarcode;    }
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        isFullPlate = true;
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
        if ( !isCheckForResearchBarcode ) return errors;
        if( researcherBarcode == null || researcherBarcode.trim().length()<1 )
        {
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", researcherBarcode));
        }
        
        return errors;
    }
}
