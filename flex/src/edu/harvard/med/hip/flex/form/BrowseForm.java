/*
 * BrowseForm.java
 *
 * Created on June 18, 2003, 1:32 PM
 */

package edu.harvard.med.hip.flex.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

/**
 * Form bean for the user profile page.  This form has the following fields,
 * with default values in square brackets:
 * <ul>
 * <li><b>password</b> - Entered password value
 * <li><b>username</b> - Entered username value
 * </ul>
 *
 * @author $Author: dzuo $
 * @version $Revision: 1.1 $ $Date: 2003-07-07 13:08:38 $
 */

public class BrowseForm extends ActionForm {
    protected String browseSelect = "human";
    protected String vectorname = null;
    protected String sortby = "cloneid";
    protected int pageindex = 1;
    protected int pagerecord = 100;
    protected int isSort = 0;
    protected String prevButton = null;
    protected int totalClones = 0;
    
    public String getBrowseSelect() {return browseSelect;}
    public String getVectorname() {return vectorname;}
    public String getSortby() {return sortby;}
    public int getPageindex() {return pageindex;}
    public int getPagerecord() {return pagerecord;}
    public int getIsSort() {return isSort;}
    public String getPrevButton() {return prevButton;}
    public int getTotalClones() {return totalClones;}
    
    public void setBrowseSelect(String s) {this.browseSelect = s;}
    public void setVectorname(String s) {this.vectorname = s;}
    public void setSortby(String sortby) {this.sortby = sortby;}
    public void setPageindex(int i) {this.pageindex = i;}
    public void setPagerecord(int i) {this.pagerecord = i;}
    public void setIsSort(int isSort) {this.isSort = isSort;}
    public void setPrevButton(String prevButton) {this.prevButton = prevButton;}
    public void setTotalClones(int totalClones) {this.totalClones = totalClones;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        browseSelect = "human";
        vectorname = null;
        sortby = "cloneid";
        pageindex = 1;
        pagerecord = 100;
        isSort = 0;
        prevButton = null;
        totalClones = 0;
    }
}
