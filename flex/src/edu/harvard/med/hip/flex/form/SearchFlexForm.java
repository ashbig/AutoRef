/*
 * SearchFlexForm.java
 *
 * Created on February 10, 2004, 11:46 AM
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
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.query.bean.*;

/**
 *
 * @author  DZuo
 */
public class SearchFlexForm extends ActionForm {
    protected String searchName = null;
    protected String searchType = null;
    protected String searchTermType = "nonfile";
    protected String searchTerm = null;
    protected FormFile filename = null;
    protected int pid = 90;
    protected int length = 100;
    protected int hits = 5;
    protected String searchDatabase = SearchDatabase.HUMAN;
    
    public String getSearchName() {return searchName;}
    public String getSearchType() {return searchType;}
    public String getSearchTermType() {return searchTermType;}
    public String getSearchTerm() {return searchTerm;}
    public FormFile getFilename() {return filename;}
    public int getPid() {return pid;}
    public int getLength() {return length;}
    public String getSearchDatabase() {return searchDatabase;}
    public int getHits() {return hits;}
    
    public void setSearchName(String s) {this.searchName = s;}
    public void setSearchType(String s) {this.searchType = s;}
    public void setSearchTermType(String s) {this.searchTermType = s;}
    public void setSearchTerm(String s) {this.searchTerm = s;}
    public void setFilename(FormFile s) {this.filename = s;}
    public void setPid(int i) {this.pid = i;}
    public void setLength(int i) {this.length = i;}
    public void setSearchDatabase(String s) {this.searchDatabase = s;}
    public void setHits(int i) {this.hits = i;}
    
    /** Creates a new instance of SearchFlexForm */
    public SearchFlexForm() {
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        searchName = null;
        searchType = null;
        searchTermType = "nonfile";
        searchTerm = null;
        filename = null;
        pid = 90;
        length = 100;
        hits = 5;
        searchDatabase = SearchDatabase.HUMAN;
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
        if(searchName == null || searchName.trim().length() < 1) {
            errors.add("searchName", new ActionError("error.searchName.required"));
        }
        
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
