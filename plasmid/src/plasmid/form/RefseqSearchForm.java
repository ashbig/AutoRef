/*
 * RefseqSearchForm.java
 *
 * Created on April 19, 2005, 2:41 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class RefseqSearchForm extends ActionForm {
    private String species;
    private String refseqType;    
    private String searchType;
    private String searchString;
    private String pages;
   
    public String getSpecies() {return species;}
    public String getRefseqType() {return refseqType;}
    public String getSearchType() {return searchType;}
    public String getSearchString() {return searchString;}
    public String getPages() {return pages;}
    
    public void setSpecies(String s) {this.species = s;}
    public void setRefseqType(String s) {this.refseqType = s;}
    public void setSearchType(String s) {this.searchType = s;}
    public void setSearchString(String s) {this.searchString = s;}
    public void setPages(String s) {this.pages = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        species = null;
        refseqType = null;
        searchType = null;
        searchString = null;
        pages = "25";
    }  
}

