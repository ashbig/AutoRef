/*
 * SequenceQueryForm.java
 *
 * Created on September 16, 2002, 2:33 PM
 */

package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class SequenceQueryForm extends ActionForm {
    private String searchType = null;
    private String searchTermType = "nonfile";
    private String searchTerm = null;
    private FormFile filename = null;
    private String flexstatus = null;
    private int project = -1;
    private int workflow = -1;
    private String plate = null;
    private boolean isResultDisplay = false;
    
    public String getSearchType() {
        return searchType;
    }
    
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    
    public String getSearchTermType() {
        return searchTermType;
    }
    
    public void setSearchTermType(String searchTermType) {
        this.searchTermType = searchTermType;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    public FormFile getFilename() {
        return filename;
    }
    
    public void setFilename(FormFile filename) {
        this.filename = filename;
    }
    
    public String getFlexstatus() {
        return flexstatus;
    }
    
    public void setFlexstatus(String flexstatus) {
        this.flexstatus = flexstatus;
    }
    
    public int getProject() {
        return project;
    }
    
    public void setProject(int project) {
        this.project = project;
    }
    
    public int getWorkflow() {
        return workflow;
    }
    
    public void setWorkflow(int workflow) {
        this.workflow = workflow;
    }
    
    public String getPlate() {
        return plate;
    }
    
    public void setPlate(String plate) {
        this.plate = plate;
    }
    
    public boolean getIsResultDisplay() {
        return isResultDisplay;
    }
    
    public void setIsResultDisplay(boolean isResultDisplay) {
        this.isResultDisplay = isResultDisplay;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        searchType = null;
        searchTermType = "nonfile";
        searchTerm = null;
        filename = null;
        flexstatus = null;
        project = -1;
        workflow = -1;
        plate = null;
        isResultDisplay = false;
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
    HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        if("nonfile".equals(searchTermType)) {
            if(searchTerm == null || searchTerm.trim().length() < 1) {
                errors.add("searchTerm", new ActionError("error.searchTerm.required"));
            }
        } else {
            if(filename.getFileName() == null || filename.getFileName().trim().length() < 1) {
                errors.add("filename", new ActionError("error.filename.required"));
            }
        }
        
        return errors;
    }
}
