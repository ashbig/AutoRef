/*
 * VectorDetailForm.java
 *
 * Created on May 3, 2005, 10:42 AM
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
public class VectorDetailForm extends ActionForm {
    private int vectorid;
    
    public VectorDetailForm() {
    }
    
    public int getVectorid() {return vectorid;}
    public void setVectorid(int id) {this.vectorid = id;}
}

