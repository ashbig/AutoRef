/*
 * QueryStorageCloneForm.java
 *
 * Created on June 28, 2004, 11:00 AM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class QueryStorageCloneForm extends ActionForm {
    private String cloneidList;
    
    /** Creates a new instance of QueryStorageCloneForm */
    public QueryStorageCloneForm() {
    }
    
    public String getCloneidList() {return cloneidList;}
    
    public void setCloneidList(String s) {this.cloneidList = s;}
        
    public ActionErrors validate(ActionMapping mapping,
    HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if(cloneidList == null || cloneidList.trim().length() < 1) {
            errors.add("cloneidList", new ActionError("error.searchTerm.required"));
        }
        
        return errors;        
    }
}
