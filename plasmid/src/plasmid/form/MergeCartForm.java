/*
 * MergeCartForm.java
 *
 * Created on May 18, 2005, 12:16 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class MergeCartForm extends ActionForm {
    private String merge = "merge";
    
    public String getMerge() {return merge;}
    public void setMerge(String s) {this.merge = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.merge = "merge";
    }
}
