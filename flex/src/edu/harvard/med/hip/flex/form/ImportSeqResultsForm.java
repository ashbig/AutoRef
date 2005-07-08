/*
 * ImportSeqResultsForm.java
 *
 * Created on January 31, 2005, 4:06 PM
 */

package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

/**
 *
 * @author  DZuo
 */
public class ImportSeqResultsForm extends ActionForm {
    private String importType = "nonfile";
    private String cloneList = null;
    private FormFile cloneListFile = null;
    private String cloneType = "Successful";
    
    /** Creates a new instance of ImportSeqResultsForm */
    public ImportSeqResultsForm() {
    }
        
    public String getImportType() {return importType;}
    public String getCloneList() {return cloneList;}
    public FormFile getCloneListFile() {return cloneListFile;}
    public String getCloneType() {return cloneType;}
    
    public void setImportType(String s) {this.importType=s;}
    public void setCloneList(String s) {this.cloneList=s;}
    public void setCloneListFile(FormFile s) {this.cloneListFile=s;}
    public void setCloneType(String s) {this.cloneType=s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        importType = "nonfile";
        cloneList = null;
        cloneListFile = null;
        cloneType = "Successful";
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
        if("nonfile".equals(importType)) {
            if(cloneList == null || cloneList.trim().length() < 1) {
                errors.add("cloneList", new ActionError("error.searchTerm.required"));
            }
        } else {
            if(cloneListFile.getFileName() == null || cloneListFile.getFileName().trim().length() < 1) {
                errors.add("cloneListFile", new ActionError("error.filename.required"));
            }
        }
        
        return errors;
    }
}
