/*
 * QueryFlexForm.java
 *
 * Created on February 11, 2004, 2:09 PM
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

/**
 *
 * @author  DZuo
 */
public class QueryFlexForm extends ActionForm {
    protected int searchid;
    protected String condition;
    protected String outputFile;
    protected int currentPage = 1;
    protected int pageSize = 10;
    
    public void setSearchid(int i) {this.searchid = i;}
    public void setCondition(String s) {this.condition = s;}
    public void setOutputFile(String s) {this.outputFile = s;}
    public void setCurrentPage(int i) {this.currentPage = i;}
    public void setPageSize(int i) {this.pageSize = i;}
    
    public int getSearchid() {return searchid;}
    public String getCondition() {return condition;}
    public String getOutputFile() {return outputFile;}
    public int getCurrentPage() {return currentPage;}
    public int getPageSize() {return pageSize;}
    
    /** Creates a new instance of QueryFlexForm */
    public QueryFlexForm() {
    }
    
}
