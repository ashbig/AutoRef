/*
 * ViewOrderDetailForm.java
 *
 * Created on June 8, 2005, 4:22 PM
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
public class ViewOrderDetailForm extends ActionForm {
    private String orderid;
    
    /** Creates a new instance of ViewOrderDetailForm */
    public ViewOrderDetailForm() {
    }
    
    public String getOrderid() {return orderid;}
    
    public void setOrderid(String s) {this.orderid = s;}
}
