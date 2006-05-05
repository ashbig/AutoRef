/*
 * CancelOrderForm.java
 *
 * Created on May 2, 2006, 12:41 PM
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
public class CancelOrderForm extends ActionForm {
    private int orderid;
    
    /** Creates a new instance of CancelOrderForm */
    public CancelOrderForm() {
    }
    
    public int getOrderid() {return orderid;}
    
    public void setOrderid(int id) {this.orderid = id;}
}
