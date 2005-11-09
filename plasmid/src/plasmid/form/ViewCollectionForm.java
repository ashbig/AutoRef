/*
 * ViewCollectionForm.java
 *
 * Created on November 7, 2005, 11:17 AM
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
public class ViewCollectionForm extends RefseqSearchForm {
    private String collectionName = null;
    
    /** Creates a new instance of ViewCollectionForm */
    public ViewCollectionForm() {
    }
        
    public String getCollectionName() {return collectionName;}
    
    public void setCollectionName(String s) {this.collectionName = s;}
}
