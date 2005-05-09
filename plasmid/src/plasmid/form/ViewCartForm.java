/*
 * ViewCartForm.java
 *
 * Created on May 6, 2005, 3:52 PM
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
public class ViewCartForm extends ActionForm {
    private int quantity[];
    
    /** Creates a new instance of ViewCartForm */
    public ViewCartForm() {
    }
    
    public int [] getQuantity() {return quantity;}
    
    public void setQuantity(int i) {
        int size = quantity.length;
        quantity[size] = i;
        System.out.println(size);
    }
}
