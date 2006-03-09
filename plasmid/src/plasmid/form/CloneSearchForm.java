/*
 * CloneSearchForm.java
 *
 * Created on February 23, 2006, 5:30 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

import plasmid.Constants;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class CloneSearchForm extends ActionForm {    
    private String searchType;
    private String searchString;
    protected int page;
    protected String forward;
    
    /** Creates a new instance of CloneSearchForm */
    public CloneSearchForm() {
    }
   
    public String getSearchType() {return searchType;}
    public void setSearchType(String s) {this.searchType = s;}
    
    public String getSearchString() {return searchString;}
    public void setSearchString(String s) {this.searchString = s;}
    
    public String getForward() {return forward;}
    public void setForward(String s) {this.forward = s;}
    
    public int getPage() {return page;}
    public void setPage(int i) {this.page = i;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {     
    }    
}
