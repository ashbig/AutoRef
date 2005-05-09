/*
 * CloneDetailForm.java
 *
 * Created on May 2, 2005, 2:51 PM
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
public class CloneDetailForm extends ActionForm {
    private int cloneid;
    private String species;
    
    /** Creates a new instance of CloneDetailForm */
    public CloneDetailForm() {
    }
    
    public int getCloneid() {return cloneid;}
    public void setCloneid(int id) {this.cloneid = id;}
    public String getSpecies() {return species;}
    public void setSpecies(String s) {this.species = s;}
}
